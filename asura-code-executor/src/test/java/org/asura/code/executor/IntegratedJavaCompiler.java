package org.asura.code.executor;

import jakarta.annotation.Nonnull;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;
import java.util.stream.Collectors;

/**
 * 整合版：支持普通方法（参数+返回值）+main方法（日志捕获）的线程安全编译器
 * JDK 21+ 兼容，多线程安全，结果准确
 */
public class IntegratedJavaCompiler {
    // 线程池：核心线程数适配CPU，守护线程，避免阻塞程序退出
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                private final AtomicInteger threadNum = new AtomicInteger(1);

                @Override
                public Thread newThread(@Nonnull Runnable r) {
                    Thread t = new Thread(r, "compiler-thread-" + threadNum.getAndIncrement());
                    t.setDaemon(true);
                    t.setPriority(Thread.NORM_PRIORITY);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    // JDK编译器实例（线程安全，可复用）
    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    // 线程本地日志缓存：保证多线程日志隔离
    private static final ThreadLocal<StringBuilder> THREAD_LOG_CACHE = ThreadLocal.withInitial(StringBuilder::new);

    // ====================== 核心方法1：执行普通方法（带参数+类型安全返回） ======================

    /**
     * 编译并执行普通静态方法
     *
     * @param className  类名（需与代码中的类名一致）
     * @param javaCode   完整Java代码字符串
     * @param methodName 要执行的方法名
     * @param returnType 期望的返回值类型
     * @param paramTypes 方法参数类型数组（无参传空数组）
     * @param params     方法参数值数组（无参传空数组）
     * @return 类型安全的方法执行结果
     * @throws Exception 编译/执行异常
     */
    public <T> T executeMethod(
            String className,
            String javaCode,
            String methodName,
            Class<T> returnType,
            Class<?>[] paramTypes,
            Object[] params
    ) throws Exception {
        // 空参数处理
        Class<?>[] finalParamTypes = paramTypes == null ? new Class[0] : paramTypes;
        Object[] finalParams = params == null ? new Object[0] : params;

        Callable<T> task = () -> {
            // 1. 编译源码（复用统一编译逻辑）
            Map<String, byte[]> classBytes = compileSource(className, javaCode);

            // 2. 独立类加载器加载类（核心隔离）
            MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
            Class<?> compiledClass = classLoader.loadClass(className);

            // 3. 反射调用方法
            Object rawResult = compiledClass.getMethod(methodName, finalParamTypes).invoke(null, finalParams);

            // 4. 类型安全转换
            return convertReturnType(rawResult, returnType);
        };

        return executeTaskWithTimeout(task);
    }

    /**
     * 重载：无参普通方法调用
     */
    public <T> T executeMethod(String className, String javaCode, String methodName, Class<T> returnType) throws Exception {
        return executeMethod(className, javaCode, methodName, returnType, new Class[0], new Object[0]);
    }

    // ====================== 核心方法2：执行main方法（捕获所有输出/日志） ======================

    /**
     * 编译并执行main方法，捕获所有输出（System.out/err + java.util.logging）
     *
     * @param className 类名
     * @param javaCode  完整Java代码字符串
     * @param mainArgs  main方法参数数组（无参传空数组）
     * @return 完整的执行日志（控制台输出+日志）
     * @throws Exception 编译/执行异常
     */
    public String executeMain(String className, String javaCode, String[] mainArgs) throws Exception {
        String[] finalMainArgs = mainArgs == null ? new String[0] : mainArgs;

        Callable<String> task = () -> {
            // 重置线程本地日志缓存
            StringBuilder logCache = THREAD_LOG_CACHE.get();
            logCache.setLength(0);

            // 1. 编译源码
            Map<String, byte[]> classBytes = compileSource(className, javaCode);

            // 2. 独立类加载器加载类
            MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
            Class<?> compiledClass = classLoader.loadClass(className);

            // 3. 保存原始配置（System流 + 日志处理器）
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            Handler originalLoggingHandler = null;
            Logger rootLogger = Logger.getLogger("");

            try {
                // ========== 步骤1：重定向System.out/err到日志缓存 ==========
                PrintStream captureStream = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                        logCache.append((char) b);
                    }

                    @Override
                    public void write(byte[] b, int off, int len) {
                        logCache.append(new String(b, off, len, StandardCharsets.UTF_8));
                    }
                }, true, StandardCharsets.UTF_8);
                System.setOut(captureStream);
                System.setErr(captureStream);

                // ========== 步骤2：捕获java.util.logging日志 ==========
                // 保存并移除原有日志处理器
                if (rootLogger.getHandlers().length > 0) {
                    originalLoggingHandler = rootLogger.getHandlers()[0];
                    rootLogger.removeHandler(originalLoggingHandler);
                }
                // 添加自定义内存日志处理器
                MemoryLogHandler logHandler = new MemoryLogHandler(logCache);
                logHandler.setLevel(Level.ALL);
                rootLogger.addHandler(logHandler);
                rootLogger.setLevel(Level.ALL);

                // ========== 步骤3：执行main方法 ==========
                compiledClass.getMethod("main", String[].class).invoke(null, (Object) finalMainArgs);

                // 刷新确保内容完整
                captureStream.flush();
                logHandler.flush();

                // 返回完整日志
                return logCache.toString().trim();

            } finally {
                // ========== 强制恢复所有原始配置 ==========
                // 恢复System.out/err
                System.setOut(originalOut);
                System.setErr(originalErr);
                // 恢复日志处理器
                if (originalLoggingHandler != null) {
                    rootLogger.removeHandler(rootLogger.getHandlers()[0]);
                    rootLogger.addHandler(originalLoggingHandler);
                }
                // 清理ThreadLocal，避免内存泄漏
                THREAD_LOG_CACHE.remove();
            }
        };

        return executeTaskWithTimeout(task);
    }

    /**
     * 重载：无参main方法调用
     */
    public String executeMain(String className, String javaCode) throws Exception {
        return executeMain(className, javaCode, new String[0]);
    }

    // ====================== 底层通用方法 ======================

    /**
     * 统一编译逻辑：将字符串源码编译为内存字节码
     */
    private Map<String, byte[]> compileSource(String className, String javaCode) throws CompilationException {
        // 1. 构建内存源码文件
        JavaFileObject sourceFile = new StringJavaFileObject(className, javaCode);

        // 2. 收集编译诊断信息
        List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.synchronizedList(new ArrayList<>());
        DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                diagnostics.add(diagnostic);
            }
        };

        // 3. 内存文件管理器（存储字节码）
        MemoryJavaFileManager fileManager = new MemoryJavaFileManager(
                COMPILER.getStandardFileManager(null, null, StandardCharsets.UTF_8)
        );

        // 4. 执行编译
        JavaCompiler.CompilationTask compilationTask = COMPILER.getTask(
                null, fileManager, diagnosticListener,
                List.of("-parameters", "-Xlint:all"), null,
                Collections.singletonList(sourceFile)
        );

        boolean compileSuccess = compilationTask.call();
        if (!compileSuccess) {
            String errorMsg = diagnostics.stream()
                    .map(d -> String.format("行%d: %s", d.getLineNumber(), d.getMessage(null)))
                    .collect(Collectors.joining("\n"));
            throw new CompilationException("编译失败：\n" + errorMsg);
        }

        return fileManager.getClassBytes();
    }

    /**
     * 统一任务执行：带超时控制
     */
    private <T> T executeTaskWithTimeout(Callable<T> task) throws Exception {
        Future<T> future = EXECUTOR.submit(task);
        try {
            return future.get(30, TimeUnit.SECONDS); // 30秒超时
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new Exception("任务执行超时（30秒）", e);
        } catch (ExecutionException e) {
            throw new Exception("任务执行失败", e.getCause());
        }
    }

    /**
     * 类型转换：保证返回值类型安全
     */
    private <T> T convertReturnType(Object rawResult, Class<T> returnType) throws IllegalArgumentException {
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

        // 基础类型自动转换
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
                    String.format("无法将 %s 转换为 %s", rawResult.getClass().getName(), returnType.getName()), e);
        }

        throw new IllegalArgumentException(
                String.format("不支持的类型转换：%s -> %s", rawResult.getClass().getName(), returnType.getName()));
    }

    // ====================== 内部辅助类 ======================

    /**
     * 内存源码文件对象：字符串源码转编译器可识别的文件
     */
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

    /**
     * 内存文件管理器：编译后的字节码存储到内存
     */
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

    /**
     * 内存输出文件对象：存储编译后的字节码
     */
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

    /**
     * 自定义类加载器：从内存字节码加载类，保证线程隔离
     */
    private static class MemoryClassLoader extends SecureClassLoader {
        private final Map<String, byte[]> classBytes;

        public MemoryClassLoader(Map<String, byte[]> classBytes) {
            this.classBytes = classBytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = classBytes.get(name);
            if (bytes == null) {
                return super.findClass(name);
            }
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    /**
     * 自定义日志处理器：捕获java.util.logging日志
     */
    private static class MemoryLogHandler extends Handler {
        private final StringBuilder logCache;

        public MemoryLogHandler(StringBuilder logCache) {
            this.logCache = logCache;
        }

        @Override
        public void publish(LogRecord record) {
            if (!isLoggable(record)) return;
            String logMsg = String.format("[%s] %s%n", record.getLevel().getName(), record.getMessage());
            logCache.append(logMsg);
            // 捕获异常堆栈
            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(sw));
                logCache.append(sw.toString()).append("\n");
            }
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    /**
     * 编译异常封装
     */
    public static class CompilationException extends RuntimeException {
        public CompilationException(String message) {
            super(message);
        }
    }

    // ====================== 关闭线程池（程序退出时调用） ======================
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

    // ====================== 综合测试案例 ======================
    public static void main(String[] args) {
        IntegratedJavaCompiler compiler = new IntegratedJavaCompiler();
        try {
//            // -------------------- 测试1：执行普通方法（带参数+类型返回） --------------------
//            String normalMethodCode = """
//                    public class NormalMethodTest {
//                        public static Integer calculate(int a, int b) {
//                            System.out.println("执行calculate方法，参数：a=" + a + ", b=" + b);
//                            java.util.logging.Logger.getLogger("NormalMethodTest").info("计算：" + a + "+" + b);
//                            return a + b;
//                        }
//                    }
//                    """;
//            // 调用带参方法
//            Integer methodResult = compiler.executeMethod(
//                    "NormalMethodTest", normalMethodCode, "calculate",
//                    Integer.class,
//                    new Class[]{int.class, int.class},
//                    new Object[]{100, 200}
//            );
//            System.out.println("=== 普通方法执行结果 ===");
//            System.out.println("返回值：" + methodResult); // 输出 300
//
//            // -------------------- 测试2：执行main方法（带参数+捕获日志） --------------------
//            String mainMethodCode = """
//                    import java.util.logging.Logger;
//
//                    public class MainMethodTest {
//                        private static final Logger logger = Logger.getLogger(MainMethodTest.class.getName());
//
//                        public static void main(String[] args) {
//                            System.out.println("=== Main方法执行开始 ===");
//                            System.err.println("这是错误流输出");
//                            logger.info("Main方法参数个数：" + args.length);
//                            for (int i = 0; i < args.length; i++) {
//                                logger.warning("参数" + i + "：" + args[i]);
//                            }
//                            // 测试异常日志
//                            try {
//                                int a = 1 / 0;
//                            } catch (Exception e) {
//                                logger.severe("发生异常：" + e.getMessage());
//                            }
//                            System.out.println("=== Main方法执行结束 ===");
//                        }
//                    }
//                    """;
//            // 执行带参main方法，捕获所有日志
//            String mainLog = compiler.executeMain(
//                    "MainMethodTest", mainMethodCode,
//                    new String[]{"Java", "编译器API", "整合测试"}
//            );
//            System.out.println("\n=== Main方法执行日志 ===");
//            System.out.println(mainLog);
//
//            // -------------------- 测试3：多线程并发执行（验证线程安全） --------------------
//            CountDownLatch latch = new CountDownLatch(2);
//            // 线程1：执行普通方法
//            new Thread(() -> {
//                try {
//                    Integer result = compiler.executeMethod(
//                            "NormalMethodTest", normalMethodCode, "calculate",
//                            Integer.class, new Class[]{int.class, int.class}, new Object[]{10, 20});
//                    System.out.println("\n线程1执行结果：" + result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    latch.countDown();
//                }
//            }).start();
//
//            // 线程2：执行main方法
//            new Thread(() -> {
//                try {
//                    String log = compiler.executeMain("MainMethodTest", mainMethodCode, new String[]{"线程2测试"});
//                    System.out.println("\n线程2执行日志：" + log);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    latch.countDown();
//                }
//            }).start();
//
//            latch.await();
//            System.out.println("\n=== 多线程执行完成 ===");

//            test1(compiler);
            test2(compiler);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compiler.shutdown();
        }
    }

    public static void test1(IntegratedJavaCompiler compiler) throws Exception {
        // -------------------- 测试1：执行普通方法（带参数+类型返回） --------------------
        String normalMethodCode = """
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;
                
                public class NormalMethodTest {
                    private static final Logger log = LoggerFactory.getLogger(NormalMethodTest.class);
                
                    public static Integer calculate(int a, int b) {
                        System.out.println("执行calculate方法，参数：a=" + a + ", b=" + b);
                        log.info("计算：{}+{}", a, b);
                        return a + b;
                    }
                }
                """;
        // 调用带参方法
        Integer methodResult = compiler.executeMethod(
                "NormalMethodTest", normalMethodCode, "calculate",
                Integer.class,
                new Class[]{int.class, int.class},
                new Object[]{100, 200}
        );
        System.out.println("=== 普通方法执行结果 ===");
        System.out.println("返回值：" + methodResult); // 输出 300

    }

    /**
     * 如果代码存在包名，执行的时候需要传入全限定类名
     *
     * @param compiler
     * @throws Exception
     */
    public static void test2(IntegratedJavaCompiler compiler) throws Exception {
        // -------------------- 测试1：执行普通方法（带参数+类型返回） --------------------
        String normalMethodCode = """
                package com.spring.boot.code.runner;
                
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;
                
                public class NormalMethodTest {
                    private static final Logger log = LoggerFactory.getLogger(NormalMethodTest.class);
                
                    public static Integer calculate(int a, int b) {
                        System.out.println("执行calculate方法，参数：a=" + a + ", b=" + b);
                        log.info("计算：{}+{}", a, b);
                        return a + b;
                    }
                }
                """;
        // 调用带参方法
        Integer methodResult = compiler.executeMethod(
                "com.spring.boot.code.runner.NormalMethodTest", normalMethodCode, "calculate",
                Integer.class,
                new Class[]{int.class, int.class},
                new Object[]{100, 200}
        );
        System.out.println("=== 普通方法执行结果 ===");
        System.out.println("返回值：" + methodResult); // 输出 300

    }

    public static void test3(IntegratedJavaCompiler compiler) throws Exception {
        // -------------------- 测试2：执行main方法（带参数+捕获日志） --------------------
        String mainMethodCode = """
                import org.slf4j.Logger;
                import org.slf4j.LoggerFactory;
                
                public class MainMethodTest {
                    private static final Logger logger = LoggerFactory.getLogger(MainMethodTest.class.getName());
                
                    public static void main(String[] args) {
                        System.out.println("=== Main方法执行开始 ===");
                        System.err.println("这是错误流输出");
                        logger.info("Main方法参数个数：{}", args.length);
                        for (int i = 0; i < args.length; i++) {
                            logger.warn("参数{}：{}", i, args[i]);
                        }
                        // 测试异常日志
                        try {
                            int a = 1 / 0;
                        } catch (Exception e) {
                            logger.error("发生异常：{}", e.getMessage());
                        }
                        System.out.println("=== Main方法执行结束 ===");
                    }
                }
                """;
        // 执行带参main方法，捕获所有日志
        String mainLog = compiler.executeMain(
                "MainMethodTest", mainMethodCode,
                new String[]{"Java", "编译器API", "整合测试"}
        );
        System.out.println("\n=== Main方法执行日志 ===");
        System.out.println(mainLog);

    }
}
