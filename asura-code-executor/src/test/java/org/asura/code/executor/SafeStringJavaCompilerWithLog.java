package org.asura.code.executor;

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
 * 支持日志捕获的线程安全字符串Java编译器（JDK 21+）
 */
public class SafeStringJavaCompilerWithLog {
    // 线程本地存储：每个线程的日志缓存（保证多线程日志隔离）
    private static final ThreadLocal<StringBuilder> THREAD_LOG_CACHE = ThreadLocal.withInitial(StringBuilder::new);

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
     * 编译并执行main方法，捕获所有输出（控制台+日志）
     */
    public String compileAndRunMainWithLog(String className, String javaCode, String[] mainArgs) throws Exception {
        String[] finalMainArgs = mainArgs == null ? new String[0] : mainArgs;

        Callable<String> task = () -> {
            // 1. 重置当前线程的日志缓存（避免线程复用导致的日志污染）
            THREAD_LOG_CACHE.get().setLength(0);
            StringBuilder logCache = THREAD_LOG_CACHE.get();

            // 2. 收集编译诊断信息
            List<Diagnostic<? extends JavaFileObject>> diagnostics = Collections.synchronizedList(new ArrayList<>());
            DiagnosticListener<JavaFileObject> diagnosticListener = diagnostic -> {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    String errorMsg = String.format("【编译错误】行%d: %s%n", diagnostic.getLineNumber(), diagnostic.getMessage(null));
                    logCache.append(errorMsg); // 编译错误也存入日志缓存
                    System.err.print(errorMsg);
                }
                diagnostics.add(diagnostic);
            };

            // 3. 内存编译
            JavaFileObject sourceFile = new StringJavaFileObject(className, javaCode);
            MemoryJavaFileManager fileManager = new MemoryJavaFileManager(
                    COMPILER.getStandardFileManager(null, null, StandardCharsets.UTF_8)
            );
            JavaCompiler.CompilationTask compilationTask = COMPILER.getTask(
                    null, fileManager, diagnosticListener, List.of("-parameters", "-Xlint:all"), null,
                    Collections.singletonList(sourceFile)
            );

            if (!compilationTask.call()) {
                String errorMsg = diagnostics.stream()
                        .filter(d -> d.getKind() == Diagnostic.Kind.ERROR)
                        .map(d -> String.format("行%d: %s", d.getLineNumber(), d.getMessage(null)))
                        .collect(Collectors.joining("\n"));
                throw new CompilationException("编译失败：\n" + errorMsg);
            }

            // 4. 自定义类加载器
            MemoryClassLoader classLoader = new MemoryClassLoader(fileManager.getClassBytes());
            Class<?> compiledClass = classLoader.loadClass(className);

            // 5. 保存原始流和日志处理器
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            Handler originalLoggingHandler = null;
            Logger rootLogger = Logger.getLogger("");

            try {
                // ========== 步骤1：重定向System.out/err到日志缓存 ==========
                PrintStream captureStream = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                        logCache.append((char) b); // 逐字符写入线程本地缓存
                    }

                    @Override
                    public void write(byte[] b, int off, int len) {
                        logCache.append(new String(b, off, len, StandardCharsets.UTF_8));
                    }
                }, true, StandardCharsets.UTF_8);
                System.setOut(captureStream);
                System.setErr(captureStream);

                // ========== 步骤2：捕获java.util.logging日志 ==========
                // 移除原有处理器，避免日志重复输出
                for (Handler handler : rootLogger.getHandlers()) {
                    rootLogger.removeHandler(handler);
                    originalLoggingHandler = handler; // 保存原有处理器
                }
                // 添加自定义日志处理器（输出到线程本地缓存）
                MemoryLogHandler logHandler = new MemoryLogHandler(logCache);
                logHandler.setLevel(Level.ALL);
                rootLogger.addHandler(logHandler);
                rootLogger.setLevel(Level.ALL);

                // ========== 步骤3：执行main方法 ==========
                compiledClass.getMethod("main", String[].class)
                        .invoke(null, (Object) finalMainArgs);

                // 刷新所有流，确保日志完整
                captureStream.flush();
                logHandler.flush();

            } finally {
                // ========== 关键：恢复所有原始配置 ==========
                // 恢复System.out/err
                System.setOut(originalOut);
                System.setErr(originalErr);
                // 恢复java.util.logging处理器
                if (originalLoggingHandler != null) {
                    rootLogger.removeHandler(rootLogger.getHandlers()[0]);
                    rootLogger.addHandler(originalLoggingHandler);
                }
                // 移除ThreadLocal，避免内存泄漏
                THREAD_LOG_CACHE.remove();
            }

            // 返回完整的日志/输出内容
            return logCache.toString().trim();
        };

        Future<String> future = EXECUTOR.submit(task);
        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new Exception("执行超时（30秒）", e);
        } catch (ExecutionException e) {
            throw new Exception("执行失败", e.getCause());
        }
    }

    /**
     * 简化重载：无参main方法
     */
    public String compileAndRunMainWithLog(String className, String javaCode) throws Exception {
        return compileAndRunMainWithLog(className, javaCode, new String[0]);
    }

    // 关闭线程池
    public void shutdown() {
        EXECUTOR.shutdown();
        try {
            if (!EXECUTOR.awaitTermination(5, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (Exception e) {
            EXECUTOR.shutdownNow();
        }
    }

    // ====================== 自定义日志处理器（捕获java.util.logging） ======================
    private static class MemoryLogHandler extends Handler {
        private final StringBuilder logCache;

        public MemoryLogHandler(StringBuilder logCache) {
            this.logCache = logCache;
        }

        @Override
        public void publish(LogRecord record) {
            if (!isLoggable(record)) return;
            // 格式化日志（级别 + 消息 + 异常）
            String logMsg = String.format("[%s] %s%n",
                    record.getLevel().getName(), record.getMessage());
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
            // 空实现，直接写入缓存无需刷新
        }

        @Override
        public void close() throws SecurityException {
            // 空实现
        }
    }

    // ====================== 复用原有辅助类 ======================
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

    // ====================== 测试示例（覆盖控制台+日志） ======================
    public static void main(String[] args) {
        SafeStringJavaCompilerWithLog compiler = new SafeStringJavaCompilerWithLog();
        try {
            // 测试代码：包含System.out + java.util.logging + 异常日志
            String logTestCode = """
                    import java.util.logging.Logger;
                    
                    public class LogTest {
                        private static final Logger logger = Logger.getLogger(LogTest.class.getName());
                    
                        public static void main(String[] args) {
                            // 控制台输出
                            System.out.println("=== 控制台输出 ===");
                            System.err.println("这是错误流输出");
                    
                            // JDK日志输出
                            logger.info("这是INFO级别日志");
                            logger.warning("这是WARNING级别日志");
                    
                            // 带参数的日志
                            logger.info("参数1：" + args[0] + "，参数2：" + args[1]);
                    
                            // 异常日志
                            try {
                                int a = 1 / 0;
                            } catch (Exception e) {
                                logger.severe("发生除零异常：" + e.getMessage());
                                logger.throwing("LogTest", "main", e);
                            }
                        }
                    }
                    """;

            // 执行并捕获所有日志/输出
            String[] mainArgs = {"日志捕获", "测试成功"};
            String logResult = compiler.compileAndRunMainWithLog("LogTest", logTestCode, mainArgs);

            // 打印捕获的结果
            System.out.println("=== 捕获的所有输出/日志 ===");
            System.out.println(logResult);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compiler.shutdown();
        }
    }
}
