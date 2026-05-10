package org.asura.code.executor;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 支持main方法执行的线程安全字符串Java编译器（JDK 21+）
 */
public class SafeStringJavaCompilerForMain {
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            r -> {
                Thread t = new Thread(r, "compiler-thread-" + new AtomicInteger(1).getAndIncrement());
                t.setDaemon(true);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    /**
     * 编译并执行main方法（支持参数传递+捕获控制台输出）
     * @param className 要编译的类名
     * @param javaCode  完整的Java代码字符串
     * @param mainArgs  main方法的参数列表（无参传空数组）
     * @return main方法执行的控制台输出结果
     * @throws Exception 编译/执行异常
     */
    public String compileAndRunMain(String className, String javaCode, String[] mainArgs) throws Exception {
        // 空参数处理
        String[] finalMainArgs = mainArgs == null ? new String[0] : mainArgs;

        Callable<String> task = () -> {
            // 1. 收集诊断信息
            List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.synchronizedList(new ArrayList<>());
            DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    System.err.printf("【编译错误】行%d: %s%n", diagnostic.getLineNumber(), diagnostic.getMessage(null));
                }
                diagnostics.add(diagnostic);
            };

            // 2. 内存编译核心逻辑
            JavaFileObject sourceFile = new StringJavaFileObject(className, javaCode);
            MemoryJavaFileManager fileManager = new MemoryJavaFileManager(
                    COMPILER.getStandardFileManager(null, null, StandardCharsets.UTF_8)
            );
            JavaCompiler.CompilationTask compilationTask = COMPILER.getTask(
                    null,
                    fileManager,
                    diagnosticListener,
                    List.of("-parameters", "-Xlint:all"),
                    null,
                    Collections.singletonList(sourceFile)
            );

            // 3. 执行编译
            boolean compileSuccess = compilationTask.call();
            if (!compileSuccess) {
                String errorMsg = diagnostics.stream()
                        .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                        .map(d -> String.format("行%d: %s", d.getLineNumber(), d.getMessage(null)))
                        .collect(Collectors.joining("\n"));
                throw new CompilationException("编译失败：\n" + errorMsg);
            }

            // 4. 创建独立类加载器（核心隔离逻辑不变）
            MemoryClassLoader classLoader = new MemoryClassLoader(fileManager.getClassBytes());
            Class<?> compiledClass = classLoader.loadClass(className);

            // 5. 捕获main方法的控制台输出（核心改造点）
            // 保存原系统输出流
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            // 创建字节数组输出流，捕获输出
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream captureOut = new PrintStream(bos, true, StandardCharsets.UTF_8);

            try {
                // 重定向系统输出/错误流到捕获流
                System.setOut(captureOut);
                System.setErr(captureOut);

                // 6. 调用main方法（固定签名：public static void main(String[] args)）
                compiledClass.getMethod("main", String[].class)
                        .invoke(null, (Object) finalMainArgs); // 注意：String[]需强转为Object避免拆箱

                // 刷新输出流，确保所有内容被捕获
                captureOut.flush();
                // 转换为字符串返回（即main方法的控制台输出）
                return bos.toString(StandardCharsets.UTF_8);
            } finally {
                // 恢复原系统输出流（关键：避免影响其他线程）
                System.setOut(originalOut);
                System.setErr(originalErr);
                // 关闭捕获流
                captureOut.close();
                bos.close();
            }
        };

        Future<String> future = EXECUTOR.submit(task);
        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new Exception("main方法执行超时（30秒）", e);
        } catch (ExecutionException e) {
            throw new Exception("main方法执行失败", e.getCause());
        }
    }

    /**
     * 简化重载：无参main方法调用
     */
    public String compileAndRunMain(String className, String javaCode) throws Exception {
        return compileAndRunMain(className, javaCode, new String[0]);
    }

    // 关闭线程池
    public void shutdown() {
        EXECUTOR.shutdown();
        try {
            if (!EXECUTOR.awaitTermination(5, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            EXECUTOR.shutdownNow();
        }
    }

    // ====================== 内部辅助类（复用原有实现） ======================
    private static class StringJavaFileObject extends SimpleJavaFileObject {
        private final String code;
        public StringJavaFileObject(String className, String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    private static class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        private final Map<String, ByteArrayOutputStream> classBytes = new HashMap<>();
        protected MemoryJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                return new MemoryOutputJavaFileObject(className, kind, classBytes);
            }
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
        public Map<String, byte[]> getClassBytes() {
            Map<String, byte[]> result = new HashMap<>();
            classBytes.forEach((k, v) -> result.put(k, v.toByteArray()));
            return result;
        }
    }

    private static class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
        private final String className;
        private final Map<String, ByteArrayOutputStream> classBytes;
        public MemoryOutputJavaFileObject(String className, Kind kind, Map<String, ByteArrayOutputStream> classBytes) {
            super(URI.create("memory:///" + className.replace('.', '/') + kind.extension), kind);
            this.className = className;
            this.classBytes = classBytes;
        }
        @Override
        public OutputStream openOutputStream() {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            classBytes.put(className, bos);
            return bos;
        }
    }

    private static class MemoryClassLoader extends SecureClassLoader {
        private final Map<String, byte[]> classBytes;
        public MemoryClassLoader(Map<String, byte[]> classBytes) {
            this.classBytes = classBytes;
        }
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classBytes.get(name);
            if (bytes == null) return super.findClass(name);
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    public static class CompilationException extends RuntimeException {
        public CompilationException(String message) {
            super(message);
        }
    }

    // ====================== 测试示例 ======================
    public static void main(String[] args) {
        SafeStringJavaCompilerForMain compiler = new SafeStringJavaCompilerForMain();
        try {
            // 测试1：无参main方法
            String noParamMainCode = """
                    public class NoParamMain {
                        public static void main(String[] args) {
                            System.out.println("无参main方法执行成功");
                            int a = 10, b = 20;
                            System.out.println("计算结果：" + (a + b));
                        }
                    }
                    """;
            String noParamResult = compiler.compileAndRunMain("NoParamMain", noParamMainCode);
            System.out.println("=== 无参main方法输出 ===");
            System.out.println(noParamResult); // 输出两行内容

            // 测试2：有参main方法
            String withParamMainCode = """
                    public class WithParamMain {
                        public static void main(String[] args) {
                            System.out.println("有参main方法执行成功");
                            System.out.println("参数个数：" + args.length);
                            for (int i = 0; i < args.length; i++) {
                                System.out.println("参数" + i + "：" + args[i]);
                            }
                        }
                    }
                    """;
            String[] mainArgs = {"Java", "编译器API", "main方法"};
            String withParamResult = compiler.compileAndRunMain("WithParamMain", withParamMainCode, mainArgs);
            System.out.println("=== 有参main方法输出 ===");
            System.out.println(withParamResult);

            // 测试3：多线程执行main方法（验证隔离性）
            System.out.println("测试3：多线程执行main方法（验证隔离性）");
            CountDownLatch latch = new CountDownLatch(2);
            // 线程1执行NoParamMain
            new Thread(() -> {
                try {
                    String result = compiler.compileAndRunMain("NoParamMain", noParamMainCode);
                    System.out.println("线程1执行结果：" + result.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
            // 线程2执行WithParamMain
            new Thread(() -> {
                try {
                    String result = compiler.compileAndRunMain("WithParamMain", withParamMainCode, mainArgs);
                    System.out.println("线程2执行结果：" + result.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
            latch.await();
            System.out.println("多线程main方法执行完成");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compiler.shutdown();
        }
    }
}
