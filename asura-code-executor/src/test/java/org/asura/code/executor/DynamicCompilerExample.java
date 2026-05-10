package org.asura.code.executor;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JDK 21 编译 API 示例 - 修复参数类型不匹配问题
 */
public class DynamicCompilerExample {

    // 线程安全的编译结果缓存
    private static final Map<String, Class<?>> COMPILED_CLASS_CACHE = new ConcurrentHashMap<>();
    // 编译锁
    private static final Lock COMPILE_LOCK = new ReentrantLock();

    // 待编译的测试代码
    private static final String TEST_CLASS_CODE = """
            package com.example;
            
            import java.util.Arrays;
            
            public class DynamicTestClass {
                // main方法
                public static void main(String[] args) {
                    System.out.println("[main方法] 执行main方法: " + Arrays.toString(args));
                }
            
                // 静态带返回值方法（int基本类型参数）
                public static int staticMethodWithReturn(int a, int b) {
                    return a + b;
                }
            
                // 静态不带返回值方法
                public static void staticMethodWithoutReturn(String message) {
                    System.out.println("[静态无返回值方法] 接收参数: " + message);
                }
            
                // 非静态带返回值方法
                public String instanceMethodWithReturn(String prefix) {
                    String result = prefix + "-" + System.currentTimeMillis();
                    System.out.println("[非静态带返回值方法] 生成结果: " + result);
                    return result;
                }
            
                // 非静态不带返回值方法（int基本类型参数）
                public void instanceMethodWithoutReturn(int count) {
                    System.out.println("[非静态无返回值方法] 计数: " + count);
                }
            }
            """;

    public static void main(String[] args) throws Exception {
        // 1. 编译动态类
        Class<?> dynamicClass = compileJavaCode("com.example.DynamicTestClass", TEST_CLASS_CODE);

        // 2. 多线程测试各类方法调用（使用命名线程池便于调试）
        ExecutorService executor = Executors.newFixedThreadPool(5, r -> {
            Thread t = new Thread(r);
            t.setName("MethodExecutor-" + t.getId());
            return t;
        });

        try {
            // 测试main方法
            executor.submit(() -> {
                try {
                    invokeMainMethod(dynamicClass, new String[]{"参数1", "参数2"});
                } catch (Exception e) {
                    System.err.println("[main方法执行异常] " + e.getMessage());
                }
            });

            // 测试静态带返回值方法（指定int基本类型参数）
            executor.submit(() -> {
                try {
                    Object result = invokeStaticMethodWithReturn(
                            dynamicClass,
                            "staticMethodWithReturn",
                            new Class<?>[]{int.class, int.class}, // 明确指定int基本类型
                            10, 20);
                    System.out.println("[静态带返回值方法] 执行结果: " + result);
                } catch (Exception e) {
                    System.err.println("[静态带返回值方法执行异常] " + e.getMessage());
                    e.printStackTrace();
                }
            });

            // 测试静态无返回值方法
            executor.submit(() -> {
                try {
                    invokeStaticMethodWithoutReturn(
                            dynamicClass,
                            "staticMethodWithoutReturn",
                            new Class<?>[]{String.class},
                            "测试消息");
                } catch (Exception e) {
                    System.err.println("[静态无返回值方法执行异常] " + e.getMessage());
                }
            });

            // 测试非静态带返回值方法
            executor.submit(() -> {
                try {
                    System.out.println("[非静态带返回值方法] 开始执行...");
                    Object instance = dynamicClass.getDeclaredConstructor().newInstance();
                    Object result = invokeInstanceMethodWithReturn(
                            instance,
                            "instanceMethodWithReturn",
                            new Class<?>[]{String.class},
                            "测试前缀");
                    System.out.println("[非静态带返回值方法] 最终结果: " + result);
                } catch (Exception e) {
                    System.err.println("[非静态带返回值方法执行异常] " + e.getMessage());
                    e.printStackTrace();
                }
            });

            // 测试非静态无返回值方法（指定int基本类型参数）
            executor.submit(() -> {
                try {
                    System.out.println("[非静态无返回值方法] 开始执行...");
                    Object instance = dynamicClass.getDeclaredConstructor().newInstance();
                    invokeInstanceMethodWithoutReturn(
                            instance,
                            "instanceMethodWithoutReturn",
                            new Class<?>[]{int.class}, // 明确指定int基本类型
                            99);
                    System.out.println("[非静态无返回值方法] 执行完成");
                } catch (Exception e) {
                    System.err.println("[非静态无返回值方法执行异常] " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } finally {
            // 优雅关闭线程池
            executor.shutdown();
            System.out.println("等待所有任务执行完成...");
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                System.err.println("部分任务未正常执行完成");
            }
            System.out.println("所有任务执行结束");
        }
    }

    /**
     * 编译Java源代码
     */
    public static Class<?> compileJavaCode(String className, String sourceCode) throws Exception {
        if (COMPILED_CLASS_CACHE.containsKey(className)) {
            return COMPILED_CLASS_CACHE.get(className);
        }

        COMPILE_LOCK.lock();
        try {
            if (COMPILED_CLASS_CACHE.containsKey(className)) {
                return COMPILED_CLASS_CACHE.get(className);
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new IllegalStateException("未找到Java编译器，请使用JDK运行（而非JRE）");
            }

            MemoryJavaFileObject fileObject = new MemoryJavaFileObject(className, sourceCode);

            List<String> options = new ArrayList<>();
            options.add("-g");
            options.add("--release");
            options.add("21");

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8);
            MemoryFileManager memoryFileManager = new MemoryFileManager(fileManager);

            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    memoryFileManager,
                    diagnostics,
                    options,
                    null,
                    Collections.singletonList(fileObject)
            );

            boolean success = task.call();
            if (!success) {
                StringBuilder errorMsg = new StringBuilder("编译失败: ");
                for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                    errorMsg.append(String.format(
                            "行 %d: %s%n",
                            diagnostic.getLineNumber(),
                            diagnostic.getMessage(null)
                    ));
                }
                throw new Exception(errorMsg.toString());
            }

            byte[] classBytes = memoryFileManager.getClassBytes(className);
            CustomClassLoader classLoader = new CustomClassLoader(Thread.currentThread().getContextClassLoader());
            Class<?> clazz = classLoader.defineClass(className, classBytes);

            COMPILED_CLASS_CACHE.put(className, clazz);

            return clazz;
        } finally {
            COMPILE_LOCK.unlock();
        }
    }

    /**
     * 调用main方法
     */
    public static void invokeMainMethod(Class<?> clazz, String[] args) throws Exception {
        try {
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (InvocationTargetException e) {
            throw new Exception("main方法执行异常", e.getTargetException());
        }
    }

    /**
     * 调用静态带返回值方法（支持指定参数类型）
     */
    public static Object invokeStaticMethodWithReturn(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        try {
            Method method = clazz.getMethod(methodName, paramTypes);
            return method.invoke(null, args);
        } catch (InvocationTargetException e) {
            throw new Exception(methodName + " 执行异常: " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new Exception(clazz.getName() + "." + methodName + Arrays.toString(paramTypes), e);
        }
    }

    /**
     * 调用静态不带返回值方法（支持指定参数类型）
     */
    public static void invokeStaticMethodWithoutReturn(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        try {
            Method method = clazz.getMethod(methodName, paramTypes);
            method.invoke(null, args);
        } catch (InvocationTargetException e) {
            throw new Exception(methodName + " 执行异常: " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new Exception(clazz.getName() + "." + methodName + Arrays.toString(paramTypes), e);
        }
    }

    /**
     * 调用非静态带返回值方法（支持指定参数类型）
     */
    public static Object invokeInstanceMethodWithReturn(Object instance, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        try {
            Method method = instance.getClass().getMethod(methodName, paramTypes);
            return method.invoke(instance, args);
        } catch (InvocationTargetException e) {
            throw new Exception(methodName + " 执行异常: " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new Exception(instance.getClass().getName() + "." + methodName + Arrays.toString(paramTypes), e);
        }
    }

    /**
     * 调用非静态不带返回值方法（支持指定参数类型）
     */
    public static void invokeInstanceMethodWithoutReturn(Object instance, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        try {
            Method method = instance.getClass().getMethod(methodName, paramTypes);
            method.invoke(instance, args);
        } catch (InvocationTargetException e) {
            throw new Exception(methodName + " 执行异常: " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new Exception(instance.getClass().getName() + "." + methodName + Arrays.toString(paramTypes), e);
        }
    }

    /**
     * 内存中的Java文件对象
     */
    static class MemoryJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        public MemoryJavaFileObject(String className, String code) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    /**
     * 内存文件管理器
     */
    static class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        private final Map<String, MemoryOutputJavaFileObject> classFiles = new HashMap<>();

        public MemoryFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                MemoryOutputJavaFileObject fileObject = new MemoryOutputJavaFileObject(className);
                classFiles.put(className, fileObject);
                return fileObject;
            }
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }

        public byte[] getClassBytes(String className) {
            MemoryOutputJavaFileObject fileObject = classFiles.get(className);
            if (fileObject == null) {
                throw new IllegalArgumentException("类 " + className + " 未找到");
            }
            return fileObject.getBytes();
        }
    }

    /**
     * 内存输出Java文件对象
     */
    static class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        public MemoryOutputJavaFileObject(String className) {
            super(URI.create("memory:///" + className.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        public byte[] getBytes() {
            return outputStream.toByteArray();
        }
    }

    /**
     * 自定义类加载器
     */
    static class CustomClassLoader extends ClassLoader {
        public CustomClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> defineClass(String name, byte[] bytes) {
            return super.defineClass(name, bytes, 0, bytes.length);
        }
    }
}
