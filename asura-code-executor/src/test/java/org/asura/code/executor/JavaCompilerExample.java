package org.asura.code.executor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.asura.code.executor.core.JavaCodeExecutor;
import org.asura.code.executor.core.JavaCodeFormatter;
import org.asura.code.executor.enums.CodeType;
import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureClassLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class JavaCompilerExample {
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

    // 编译结果缓存
    private final Cache<String, Object> compileCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    // 正则常量
    private static final Pattern MAIN_METHOD_PATTERN = Pattern.compile(
            "public\\s+static\\s+void\\s+main\\s*\\(\\s*String\\s*\\[\\s*]\\s*\\w*\\s*\\)\\s*\\{",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([\\w.]+)\\s*;");
    private static final Pattern CLASS_PATTERN = Pattern.compile("public\\s+class\\s+(\\w+)\\s*\\{");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w.]+\\.\\w+);");

    // JDK编译器实例（线程安全，可复用）
    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    // 线程本地日志缓存：保证多线程日志隔离
    private static final ThreadLocal<StringBuilder> THREAD_LOG_CACHE = ThreadLocal.withInitial(StringBuilder::new);

    /**
     * 统一执行入口
     */
    public Object execute(String javaCode, CodeType codeType, Map<String, Object> params, Class<?> returnType) throws Exception {
        if (javaCode == null || javaCode.isBlank()) {
            throw new IllegalArgumentException("Java代码不能为空");
        }
        if (returnType == null) {
            returnType = Object.class;
        }

        return switch (codeType) {
//            case WHOLE_CLASS -> executeWholeClass(javaCode, params, returnType);
            case WHOLE_CLASS -> executeClass(javaCode, params, returnType);
            case EXPRESSION -> executeExpressionByJdk(javaCode, params, returnType);
            case SCRIPT -> executeScriptByJdk(javaCode, params, returnType);
        };
    }

    // ------------------------------ 1. 完整类执行（修复文件名+自动补全import） ------------------------------
    private Object executeWholeClass(String code, Map<String, Object> params, Class<?> returnType) throws Exception {
        // 步骤1：自动补全必要的import（解决Arrays找不到符号）
        String completeCode = autoCompleteImports(code);

        // 步骤2：提取全限定类名和简单类名
        String fullClassName = extractClassName(completeCode);

        // 提取方法名称
        if (params == null || !params.containsKey("methodName")) {
            throw new IllegalArgumentException("params必须包含methodName");
        }
        String methodName = (String) params.get("methodName");
        params.remove("methodName");

        // 步骤4：执行main方法或普通方法
        if (Objects.equals(methodName, "main")) {
            String[] mainArgs = params.containsKey("mainArgs") ? (String[]) params.get("mainArgs") : new String[]{};

            return executeMain(fullClassName, code, mainArgs);
        } else {
            Object[] methodParams = params.values().toArray();
            Class<?>[] paramTypes = new Class[methodParams.length];
            for (int i = 0; i < methodParams.length; i++) {
                Object param = methodParams[i];
                paramTypes[i] = param == null ? Object.class : getPrimitiveType(param.getClass());
            }

            return executeMethod(fullClassName, code, methodName, returnType, paramTypes, methodParams);
        }
    }

    private Object executeClass(String javaCode, Map<String, Object> params, Class<?> returnType) throws Exception {
        // 步骤1：自动补全必要的import（解决Arrays找不到符号）
        String completeCode = autoCompleteImports(javaCode);

        // 步骤2：提取全限定类名和简单类名
        String fullClassName = extractClassName(completeCode);

        // 提取方法名称
        if (params == null || !params.containsKey("methodName")) {
            throw new IllegalArgumentException("params必须包含methodName");
        }
        String methodName = (String) params.get("methodName");
        params.remove("methodName");

        // 步骤4：执行main方法或普通方法
        if (Objects.equals(methodName, "main")) {
            String[] mainArgs = params.containsKey("mainArgs") ? (String[]) params.get("mainArgs") : new String[]{};

            return executeClass(fullClassName, javaCode, mainArgs, "main", Void.class, null, null);
        } else {
            Object[] methodParams = params.values().toArray();
            Class<?>[] paramTypes = new Class[methodParams.length];
            for (int i = 0; i < methodParams.length; i++) {
                Object param = methodParams[i];
                paramTypes[i] = param == null ? Object.class : getPrimitiveType(param.getClass());
            }

            return executeClass(fullClassName, javaCode, null, methodName, returnType, paramTypes, methodParams);
        }
    }

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
    public <T> T executeMethod(String className, String javaCode, String methodName, Class<T> returnType, Class<?>[] paramTypes, Object[] params) throws Exception {
        // 空参数处理
        Class<?>[] finalParamTypes = paramTypes == null ? new Class[0] : paramTypes;
        Object[] finalParams = params == null ? new Object[0] : params;

        // 定义执行任务
        Callable<T> task = () -> {
            // 步骤3：编译类（修复文件名匹配）
            String codeMd5 = DigestUtils.md5Hex(javaCode);
            Class<?> compiledClass = (Class<?>) compileCache.get(codeMd5, k -> {
                try {
                    // 1. 编译源码（复用统一编译逻辑）
                    Map<String, byte[]> classBytes = compileSource(className, javaCode);

                    // 2. 独立类加载器加载类（核心隔离）
                    MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
                    return classLoader.loadClass(className);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Method method = findMethod(compiledClass, methodName, paramTypes);
            if (method == null) {
                throw new NoSuchMethodException("未找到方法：" + methodName + "，参数类型：" + Arrays.toString(paramTypes));
            }

            // 3. 反射调用方法
            Object rawResult = compiledClass.getMethod(methodName, finalParamTypes).invoke(null, finalParams);

            // 4. 类型安全转换
            return convertReturnType(rawResult, returnType);
        };

        // 在线程池中执行任务并返回结果
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

        // 定义执行任务
        Callable<String> task = () -> {
            // 重置线程本地日志缓存
            StringBuilder logCache = THREAD_LOG_CACHE.get();
            logCache.setLength(0);

            // 步骤3：编译类（修复文件名匹配）
            String codeMd5 = DigestUtils.md5Hex(javaCode);
            Class<?> compiledClass = (Class<?>) compileCache.get(codeMd5, k -> {
                try {
                    // 1. 编译源码
                    Map<String, byte[]> classBytes = compileSource(className, javaCode);

                    // 2. 独立类加载器加载类
                    MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
                    return classLoader.loadClass(className);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

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
                    public void write(@Nonnull byte[] b, int off, int len) {
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

        // 在线程池中执行任务并返回结果
        return executeTaskWithTimeout(task);
    }

    /**
     * 重载：无参main方法调用
     */
    public String executeMain(String className, String javaCode) throws Exception {
        return executeMain(className, javaCode, new String[0]);
    }

    @Data
    public static class ExecuteResult<T> {
        private T data;
        private String log;
    }

    /**
     * 编译并执行main方法，捕获所有输出（System.out/err + java.util.logging）
     *
     * @param className 类名
     * @param javaCode  完整Java代码字符串
     * @param mainArgs  main方法参数数组（无参传空数组）
     * @return 完整的执行日志（控制台输出+日志）
     * @throws Exception 编译/执行异常
     */
    public <T> ExecuteResult<T> executeClass(String className, String javaCode, String[] mainArgs,
                                             String methodName, Class<T> returnType, Class<?>[] paramTypes, Object[] params) throws Exception {
        // main方法参数处理
        String[] finalMainArgs = mainArgs == null ? new String[0] : mainArgs;

        // 非main方法参数处理
        // 空参数处理
        Class<?>[] finalParamTypes = paramTypes == null ? new Class[0] : paramTypes;
        Object[] finalParams = params == null ? new Object[0] : params;

        ExecuteResult<T> result = new ExecuteResult<>();

        // 定义执行任务
        Callable<ExecuteResult<T>> task = () -> {
            // 重置线程本地日志缓存
            StringBuilder logCache = THREAD_LOG_CACHE.get();
            logCache.setLength(0);

            // 步骤3：编译类（修复文件名匹配）
            String codeMd5 = DigestUtils.md5Hex(javaCode);
            Class<?> compiledClass = (Class<?>) compileCache.get(codeMd5, k -> {
                try {
                    // 1. 编译源码
                    Map<String, byte[]> classBytes = compileSource(className, javaCode);

                    // 2. 独立类加载器加载类
                    MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
                    return classLoader.loadClass(className);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

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
                    public void write(@Nonnull byte[] b, int off, int len) {
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

                if (Objects.equals(methodName, "main")) {
                    compiledClass.getMethod("main", String[].class).invoke(null, (Object) finalMainArgs);
                } else {
                    Method method = findMethod(compiledClass, methodName, paramTypes);
                    if (method == null) {
                        throw new NoSuchMethodException("未找到方法：" + methodName + "，参数类型：" + Arrays.toString(paramTypes));
                    }

                    // 1. 核心判断：是否为静态方法
                    boolean isStatic = Modifier.isStatic(method.getModifiers());

                    if (isStatic) {
                        if (returnType.isInstance(Void.class)) {
                            compiledClass.getMethod(methodName, finalParamTypes).invoke(null, finalParams);
                        } else {
                            // 3. 反射调用方法
                            Object rawResult = compiledClass.getMethod(methodName, finalParamTypes).invoke(null, finalParams);

                            // 4. 类型安全转换
                            T res = convertReturnType(rawResult, returnType);

                            result.setData(res);
                        }
                    } else {
                        // 非静态方法时，创建实例
                        Object instance = compiledClass.getDeclaredConstructor().newInstance();
                        if (returnType.isInstance(Void.class)) {
                            instance.getClass().getMethod(methodName, finalParamTypes).invoke(instance, finalParams);
                        } else {
                            // 3. 反射调用方法
                            Object rawResult = instance.getClass().getMethod(methodName, finalParamTypes).invoke(instance, finalParams);

                            // 4. 类型安全转换
                            T res = convertReturnType(rawResult, returnType);

                            result.setData(res);
                        }
                    }
                }

                // 刷新确保内容完整
                captureStream.flush();
                logHandler.flush();

                result.setLog(logCache.toString().trim());

                return result;
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

        // 在线程池中执行任务并返回结果
        return executeTaskWithTimeout(task);
    }

    // ------------------------------ 2. 表达式执行（原有逻辑，执行成功） ------------------------------
    private Object executeExpressionByJdk(String expr, Map<String, Object> params, Class<?> returnType) throws Exception {
        String className = "Expr_" + DigestUtils.md5Hex(expr + params + returnType).substring(0, 8);

        // 定义执行任务
        Callable<Object> task = () -> {
            // 生成表达式执行动态类
            String dynamicCode = generateExprDynamicClass(className, expr, params, returnType);

//        // 格式化代码
//        completeCode = JavaCodeFormatter.formatJavaCode(completeCode);
//        System.out.println(completeCode);

            // 步骤3：编译类（修复文件名匹配）
            String codeMd5 = DigestUtils.md5Hex(dynamicCode);
            Class<?> compiledClass = (Class<?>) compileCache.get(codeMd5, k -> {
                try {
                    // 1. 编译源码
                    Map<String, byte[]> classBytes = compileSource(className, dynamicCode);

                    // 2. 独立类加载器加载类
                    MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
                    return classLoader.loadClass(className);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Object instance = compiledClass.getDeclaredConstructor().newInstance();
            Method method = compiledClass.getMethod("execute", paramsToTypes(params));
            return method.invoke(instance, params.values().toArray());
        };

        // 在线程池中执行任务并返回结果
        return executeTaskWithTimeout(task);
    }

    // ------------------------------ 3. 脚本执行（原有逻辑，执行成功） ------------------------------
    private Object executeScriptByJdk(String script, Map<String, Object> params, Class<?> returnType) throws Exception {
        String className = "Script_" + DigestUtils.md5Hex(script + params + returnType).substring(0, 8);

        // 定义执行任务
        Callable<Object> task = () -> {
            // 生成脚本执行动态类
            String dynamicCode = generateScriptDynamicClass(className, script, params, returnType);
//            System.out.println("生成脚本执行动态类：\n" + dynamicCode);

            // 格式化代码
            String formatedCode = JavaCodeFormatter.formatJavaCode(dynamicCode);
//            System.out.println("执行脚本代码：\n" + formatedCode);

            // 步骤3：编译类（修复文件名匹配）
            String codeMd5 = DigestUtils.md5Hex(formatedCode);
            Class<?> compiledClass = (Class<?>) compileCache.get(codeMd5, k -> {
                try {
                    // 1. 编译源码
                    Map<String, byte[]> classBytes = compileSource(className, formatedCode);

                    // 2. 独立类加载器加载类
                    MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
                    return classLoader.loadClass(className);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Object instance = compiledClass.getDeclaredConstructor().newInstance();
            Method method = compiledClass.getMethod("execute", paramsToTypes(params));
            return method.invoke(instance, params.values().toArray());
        };

        // 在线程池中执行任务并返回结果
        return executeTaskWithTimeout(task);
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


    // ------------------------------ 核心修复：自动补全import ------------------------------

    /**
     * 自动补全常用类的import（解决Arrays找不到符号）
     */
    private String autoCompleteImports(String code) {
        // 1. 提取已有的import
        Set<String> importedClasses = new HashSet<>();
        Matcher importMatcher = IMPORT_PATTERN.matcher(code);
        while (importMatcher.find()) {
            importedClasses.add(importMatcher.group(1));
        }

        // 2. 检查需要补全的import
        List<String> needImports = new ArrayList<>();
        if (code.contains("Arrays") && !importedClasses.contains("java.util.Arrays")) {
            needImports.add("import java.util.Arrays;");
        }
        if (code.contains("List") && !importedClasses.contains("java.util.List")) {
            needImports.add("import java.util.List;");
        }
        if (code.contains("Map") && !importedClasses.contains("java.util.Map")) {
            needImports.add("import java.util.Map;");
        }
        if (code.contains("HashMap") && !importedClasses.contains("java.util.HashMap")) {
            needImports.add("import java.util.HashMap;");
        }
        if (code.contains("ArrayList") && !code.contains("java.util.ArrayList")) {
            needImports.add("import java.util.ArrayList;");
        }
        if (code.contains("Stream") && !code.contains("java.util.stream.Stream")) {
            needImports.add("import java.util.stream.Stream;");
        }

        // 3. 将import插入到package之后，类定义之前
        Matcher packageMatcher = PACKAGE_PATTERN.matcher(code);
        if (packageMatcher.find()) {
            int packageEnd = packageMatcher.end();
            StringBuilder sb = new StringBuilder(code);
            for (String imp : needImports) {
                sb.insert(packageEnd, imp + "\n");
                packageEnd += imp.length() + 1;
            }
            return sb.toString();
        } else {
            // 无package时，插入到类定义之前
            Matcher classMatcher = CLASS_PATTERN.matcher(code);
            if (classMatcher.find()) {
                int classStart = classMatcher.start();
                StringBuilder sb = new StringBuilder(code);
                for (int i = needImports.size() - 1; i >= 0; i--) {
                    sb.insert(classStart, needImports.get(i) + "\n");
                }
                return sb.toString();
            }
        }

        return code;
    }

    // ------------------------------ 通用工具方法 ------------------------------
    private String extractClassName(String code) {
        Matcher packageMatcher = PACKAGE_PATTERN.matcher(code);
        String packageName = packageMatcher.find() ? packageMatcher.group(1) + "." : "";

        Matcher classMatcher = CLASS_PATTERN.matcher(code);
        String simpleClassName = classMatcher.find() ? classMatcher.group(1) : "DynamicClass";

        return packageName + simpleClassName;
    }

    private boolean hasMainMethod(String code) {
        return MAIN_METHOD_PATTERN.matcher(code).find();
    }

    private Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName) && method.getParameterCount() == paramTypes.length) {
                    Parameter[] parameters = method.getParameters();
                    boolean match = true;
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> paramType = paramTypes[i];
                        Class<?> methodParamType = parameters[i].getType();
                        if (!methodParamType.isAssignableFrom(paramType) &&
                                !getWrapperType(methodParamType).isAssignableFrom(paramType)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        return method;
                    }
                }
            }
            return null;
        }
    }

    private String generateExprDynamicClass(String className, String expr, Map<String, Object> params, Class<?> returnType) {
        StringBuilder code = new StringBuilder();
        code.append("public class ").append(className).append(" {\n");
        code.append("    public ").append(returnType.getSimpleName()).append(" execute(");

        if (params != null && !params.isEmpty()) {
            List<String> paramDeclares = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                Class<?> type = value == null ? Object.class : getPrimitiveType(value.getClass());
                paramDeclares.add(type.getSimpleName() + " " + name);
            }
            code.append(String.join(", ", paramDeclares));
        }

        code.append(") {\n");
        code.append("        return ").append(expr).append(";\n");
        code.append("    }\n");
        code.append("}\n");

        // 关键修复：补全import
        return autoCompleteDynamicImports(code.toString());
    }

    private String generateScriptDynamicClass(String className, String script, Map<String, Object> params, Class<?> returnType) {
        StringBuilder code = new StringBuilder();
        code.append("public class ").append(className).append(" {\n");
        code.append("    public ").append(returnType.getSimpleName()).append(" execute(");

        if (params != null && !params.isEmpty()) {
            List<String> paramDeclares = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                Class<?> type = value == null ? Object.class : getPrimitiveType(value.getClass());
                paramDeclares.add(type.getSimpleName() + " " + name);
            }
            code.append(String.join(", ", paramDeclares));
        }

        code.append(") {\n");
        code.append("        ").append(script);
        code.append("    }\n");
        code.append("}\n");

        // 关键修复：补全import
        return autoCompleteDynamicImports(code.toString());
    }

    // ------------------------------ 新增：自动补全脚本/表达式的import ------------------------------

    /**
     * 检测代码中用到的类，自动补全import语句
     */
    private String autoCompleteDynamicImports(String code) {
        Set<String> needImports = new HashSet<>();

        // 检测需要import的类
        if (code.contains("List") && !code.contains("java.util.List")) {
            needImports.add("import java.util.List;");
        }
        if (code.contains("ArrayList") && !code.contains("java.util.ArrayList")) {
            needImports.add("import java.util.ArrayList;");
        }
        if (code.contains("Map") && !code.contains("java.util.Map")) {
            needImports.add("import java.util.Map;");
        }
        if (code.contains("HashMap") && !code.contains("java.util.HashMap")) {
            needImports.add("import java.util.HashMap;");
        }
        if (code.contains("Arrays") && !code.contains("java.util.Arrays")) {
            needImports.add("import java.util.Arrays;");
        }
        if (code.contains("Stream") && !code.contains("java.util.stream.Stream")) {
            needImports.add("import java.util.stream.Stream;");
        }

        // 将import插入到类定义之前
        if (!needImports.isEmpty()) {
            int classStart = code.indexOf("public class");
            if (classStart >= 0) {
                StringBuilder sb = new StringBuilder(code);
                sb.insert(classStart, String.join("\n", needImports) + "\n");
                return sb.toString();
            }
        }
        return code;
    }

    private Class<?> getPrimitiveType(Class<?> clazz) {
        if (clazz == Integer.class) return int.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Double.class) return double.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Short.class) return short.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Character.class) return char.class;
        return clazz;
    }

    private Class<?> getWrapperType(Class<?> clazz) {
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == double.class) return Double.class;
        if (clazz == float.class) return Float.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == short.class) return Short.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == char.class) return Character.class;
        return clazz;
    }

    private Class<?>[] paramsToTypes(Map<String, Object> params) {
        if (params == null) return new Class[0];
        return params.values().stream()
                .map(obj -> obj == null ? Object.class : getPrimitiveType(obj.getClass()))
                .toArray(Class[]::new);
    }

    // ====================== 综合测试案例 ======================
    public static void main(String[] args) {
        JavaCompilerExample compiler = new JavaCompilerExample();
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
//            test2(compiler);
//            test3(compiler);
//            test4(compiler);
//            test5(compiler);
//            test6(compiler);
//            test7(compiler);
            test8(compiler);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compiler.shutdown();
        }
    }

    public static void test1(JavaCodeExecutor compiler) throws Exception {
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
     */
    public static void test2(JavaCodeExecutor compiler) throws Exception {
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
//        Integer methodResult = compiler.executeMethod(
//                "com.spring.boot.code.runner.NormalMethodTest", normalMethodCode, "calculate",
//                Integer.class,
//                new Class[]{int.class, int.class},
//                new Object[]{100, 200}
//        );

        Map<String, Object> params = new HashMap<>();
        params.put("a", 100);
        params.put("b", 200);
        params.put("methodName", "calculate");
        Object methodResult = compiler.execute(normalMethodCode, CodeType.WHOLE_CLASS, params, Integer.class);

        System.out.println("=== 普通方法执行结果 ===");
        System.out.println("返回值：" + methodResult); // 输出 300

    }

    public static void test3(JavaCodeExecutor compiler) throws Exception {
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
//        String mainLog = compiler.executeMain(
//                "MainMethodTest", mainMethodCode,
//                new String[]{"Java", "编译器API", "整合测试"}
//        );

        Map<String, Object> params = new HashMap<>();
        params.put("mainArgs", new String[]{"Java", "编译器API", "整合测试"});
        params.put("methodName", "main");
        Object mainLog = compiler.execute(mainMethodCode, CodeType.WHOLE_CLASS, params, null);

        System.out.println("\n=== Main方法执行日志 ===");
        System.out.println(mainLog);
    }

    public static void test4(JavaCodeExecutor compiler) throws Exception {
        String expressionCode = """
                10 + 20
                """;

        Map<String, Object> params = new HashMap<>();
        Object result = compiler.execute(expressionCode, CodeType.EXPRESSION, params, Integer.class);

        System.out.println("\n=== 表达式执行结果 ===");
        System.out.println(result);
    }

    public static void test5(JavaCodeExecutor compiler) throws Exception {
        String scriptCode = """
                int sum = 0;
                for (int i = 0; i < 5; i++) {
                    sum += i;
                }
                return sum;
                """;

        Map<String, Object> params = new HashMap<>();
        Object result = compiler.execute(scriptCode, CodeType.SCRIPT, params, Integer.class);

        System.out.println("\n=== 脚本执行结果 ===");
        System.out.println(result);
    }

    public static void test6(JavaCodeExecutor compiler) throws Exception {
        String scriptCode = """
                int sum = 0;
                for (int i = 0; i < 5; i++) {
                    sum += i;
                }
                return sum + a + b;
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("a", 10);
        params.put("b", 11);
        Object result = compiler.execute(scriptCode, CodeType.SCRIPT, params, Integer.class);

        System.out.println("\n=== 脚本执行结果 ===");
        System.out.println(result);
    }

    public static void test7(JavaCodeExecutor compiler) throws Exception {
        String scriptCode = """
                int sum = 0;
                for (int i = 0; i < 5; i++) {
                    sum += i;
                }
                return sum + a + b;
                """;

        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        Map<String, Object> params = new HashMap<>();
                        params.put("a", 10);
                        params.put("b", 11);
                        Object result = compiler.execute(scriptCode, CodeType.SCRIPT, params, Integer.class);

                        System.out.println("\n=== 脚本执行结果 ===");
                        System.out.println(result);
                        Assert.isTrue(Integer.parseInt(result.toString()) == 31, "执行失败！");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            executorService.shutdown();
        }
    }

    public static void test8(JavaCompilerExample compiler) throws Exception {
        String javaCode = """
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
                        System.out.println("=== Main方法执行结束 ===");
                    }
                
                    public static Integer sum1(int a, int b) {
                        logger.info("sum1执行结果：{}", a + b);
                        return a + b;
                    }
                
                    public static void sum2(int a, int b) {
                        System.out.println("sum2执行结果：" + (a + b));
                    }
                
                    public Integer sum3(int a, int b) {
                        logger.info("sum3执行结果：{}", a + b);
                        return a + b;
                    }
                
                    public void sum4(int a, int b) {
                        System.out.println("sum4执行结果：" + (a + b));
                    }
                }
                """;

        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            for (int i = 0; i < 1; i++) {
                executorService.submit(() -> {
                    try {
                        // 执行main方法
                        Map<String, Object> params1 = new HashMap<>();
                        params1.put("a", 10);
                        params1.put("b", 11);
                        params1.put("mainArgs", new String[]{"参数1", "1", "参数3"});
                        params1.put("methodName", "main");
                        Object result1 = compiler.execute(javaCode, CodeType.WHOLE_CLASS, params1, Void.class);

                        System.out.println("\n=== 执行main方法 代码执行结果 ===");
                        System.out.println(result1);

                        // 执行非main静态方法，有返回值
                        Map<String, Object> params2 = new HashMap<>();
                        params2.put("a", 10);
                        params2.put("b", 11);
                        params2.put("methodName", "sum1");
                        Object result2 = compiler.execute(javaCode, CodeType.WHOLE_CLASS, params2, Integer.class);

                        System.out.println("\n=== 执行非main静态方法，有返回值 代码执行结果 ===");
                        System.out.println(result2);

                        // 执行非main静态方法，无返回值
                        Map<String, Object> params3 = new HashMap<>();
                        params3.put("a", 10);
                        params3.put("b", 11);
                        params3.put("methodName", "sum2");
                        Object result3 = compiler.execute(javaCode, CodeType.WHOLE_CLASS, params3, Integer.class);

                        System.out.println("\n=== 执行非main静态方法，无返回值 代码执行结果 ===");
                        System.out.println(result3);

                        // 执行实例方法，有返回值
                        Map<String, Object> params4 = new HashMap<>();
                        params4.put("a", 10);
                        params4.put("b", 11);
                        params4.put("methodName", "sum3");
                        Object result4 = compiler.execute(javaCode, CodeType.WHOLE_CLASS, params4, Integer.class);

                        System.out.println("\n=== 执行实例方法，有返回值 代码执行结果 ===");
                        System.out.println(result4);

                        // 执行实例方法，无返回值
                        Map<String, Object> params5 = new HashMap<>();
                        params5.put("a", 10);
                        params5.put("b", 11);
                        params5.put("methodName", "sum4");
                        Object result5 = compiler.execute(javaCode, CodeType.WHOLE_CLASS, params5, Integer.class);

                        System.out.println("\n=== 执行实例方法，无返回值 代码执行结果 ===");
                        System.out.println(result5);
//                        Assert.isTrue(Integer.parseInt(result2.toString()) == 21, "执行失败！");
                    } catch (Exception e) {
                        log.error("执行异常：", e);
                        throw new RuntimeException(e);
                    }
                });
            }

            executorService.shutdown();
        }
    }
}
