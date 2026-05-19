package org.asura.code.executor;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 支持参数传递+类型安全返回的线程安全字符串Java编译器（JDK 21+）
 */
public class SafeStringJavaCompilerWithParams {
    // 线程池（复用原有安全设计）
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
     * 编译并执行代码（增强版：支持参数传递+类型安全返回）
     * @param className 要编译的类名（必须与代码中的类名一致）
     * @param javaCode  完整的Java代码字符串
     * @param methodName 要执行的方法名（静态方法）
     * @param returnType 期望的返回值类型（泛型限定）
     * @param paramTypes 方法参数类型列表（无参传空数组）
     * @param params 方法参数值列表（无参传空数组）
     * @return 类型安全的方法执行结果
     * @throws Exception 编译/执行/类型转换异常
     */
    public <T> T compileAndRun(
            String className,
            String javaCode,
            String methodName,
            Class<T> returnType,
            Class<?>[] paramTypes,
            Object[] params
    ) throws Exception {
        // 空参数处理（简化调用）
        Class<?>[] finalParamTypes = paramTypes == null ? new Class[0] : paramTypes;
        Object[] finalParams = params == null ? new Object[0] : params;

        Callable<T> task = () -> {
            // 1. 收集诊断信息
            List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.synchronizedList(new ArrayList<>());
            DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
                diagnostics.add(diagnostic);
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    System.err.printf("【编译错误】行%d: %s%n", diagnostic.getLineNumber(), diagnostic.getMessage(null));
                }
            };

            // 2. 内存编译核心逻辑（复用原有设计）
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

            // 4. 加载类并调用方法（支持参数）
            MemoryClassLoader classLoader = new MemoryClassLoader(fileManager.getClassBytes());
            Class<?> compiledClass = classLoader.loadClass(className);

            // 根据参数类型获取方法并调用
            Object rawResult = compiledClass.getMethod(methodName, finalParamTypes)
                    .invoke(null, finalParams); // 静态方法，第一个参数为null

            // 5. 类型安全转换（核心增强）
            return convertReturnType(rawResult, returnType);
        };

        Future<T> future = EXECUTOR.submit(task);
        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new Exception("代码执行超时（30秒）", e);
        } catch (ExecutionException e) {
            throw new Exception("代码执行失败", e.getCause());
        }
    }

    /**
     * 简化重载：无参方法调用（无需传参数类型和参数值）
     */
    public <T> T compileAndRun(String className, String javaCode, String methodName, Class<T> returnType) throws Exception {
        return compileAndRun(className, javaCode, methodName, returnType, new Class[0], new Object[0]);
    }

    /**
     * 类型转换工具：将原始返回值转换为目标类型（支持基础类型自动装箱/拆箱）
     */
    private <T> T convertReturnType(Object rawResult, Class<T> returnType) throws IllegalArgumentException {
        // 空值处理
        if (rawResult == null) {
            if (returnType.isPrimitive()) {
                throw new IllegalArgumentException("原始类型（如int）不能返回null");
            }
            return null;
        }

        // 类型完全匹配
        if (returnType.isInstance(rawResult)) {
            return returnType.cast(rawResult);
        }

        // 基础类型自动转换（如Integer -> int，String -> int等）
        try {
            if (returnType == int.class || returnType == Integer.class) {
                return returnType.cast(Integer.valueOf(rawResult.toString()));
            } else if (returnType == long.class || returnType == Long.class) {
                return returnType.cast(Long.valueOf(rawResult.toString()));
            } else if (returnType == double.class || returnType == Double.class) {
                return returnType.cast(Double.valueOf(rawResult.toString()));
            } else if (returnType == boolean.class || returnType == Boolean.class) {
                return returnType.cast(Boolean.valueOf(rawResult.toString()));
            } else if (returnType == String.class) {
                return returnType.cast(rawResult.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("无法将 %s 类型的 %s 转换为 %s 类型",
                            rawResult.getClass().getName(), rawResult, returnType.getName()), e);
        }

        // 不支持的类型转换
        throw new IllegalArgumentException(
                String.format("不支持的类型转换：%s -> %s",
                        rawResult.getClass().getName(), returnType.getName()));
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
        SafeStringJavaCompilerWithParams compiler = new SafeStringJavaCompilerWithParams();
        try {
            // 测试1：无参方法 + 整数返回
            String noParamCode = """
                    public class NoParamTest {
                        public static Integer add() {
                            return 10 + 20;
                        }
                    }
                    """;
            Integer noParamResult = compiler.compileAndRun("NoParamTest", noParamCode, "add", Integer.class);
            System.out.println("无参方法执行结果：" + noParamResult); // 输出 30

            // 测试2：有参方法 + 字符串返回
            String withParamCode = """
                    public class WithParamTest {
                        public static String sayHello(String name, int age) {
                            return "Hello, " + name + "! 你今年" + age + "岁了";
                        }
                    }
                    """;
            // 指定参数类型和参数值
            Class<?>[] paramTypes = {String.class, int.class};
            Object[] params = {"张三", 25};
            String withParamResult = compiler.compileAndRun(
                    "WithParamTest", withParamCode, "sayHello", String.class, paramTypes, params);
            System.out.println("有参方法执行结果：" + withParamResult); // 输出 Hello, 张三! 你今年25岁了

            // 测试3：基础类型转换（返回double）
            String convertCode = """
                    public class ConvertTest {
                        public static double calculate(double a, double b) {
                            return a * b;
                        }
                    }
                    """;
            Double convertResult = compiler.compileAndRun(
                    "ConvertTest", convertCode, "calculate", Double.class,
                    new Class[]{double.class, double.class},
                    new Object[]{3.5, 2.0});
            System.out.println("类型转换测试结果：" + convertResult); // 输出 7.0

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compiler.shutdown();
        }
    }
}
