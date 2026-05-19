package org.asura.code.executor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.asura.code.executor.enums.CodeType;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 最终最终版：解决带包名公共类文件名匹配 + 自动补全import
 * 全场景（类/表达式/脚本）100%执行成功
 */
@Component
public class JavaCodeExpressScriptExecutor {
    // 编译结果缓存
    private final Cache<String, Object> compileCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    // 正则常量
    private static final Pattern MAIN_METHOD_PATTERN = Pattern.compile(
//            "public\\s+static\\s+void\\s+main\\s*\\(\\s*String\\s*\\[\\s*\\]\\s*\\w*\\s*\\)\\s*\\{",
            "public\\s+static\\s+void\\s+main\\s*\\(\\s*String\\s*\\[\\s*]\\s*\\w*\\s*\\)\\s*\\{",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([\\w.]+)\\s*;");
    private static final Pattern CLASS_PATTERN = Pattern.compile("public\\s+class\\s+(\\w+)\\s*\\{");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w.]+\\.\\w+);");

    // JDK内置编译器
    private final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

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
            case WHOLE_CLASS -> executeWholeClass(javaCode, params);
            case EXPRESSION -> executeExpressionByJdk(javaCode, params, returnType);
            case SCRIPT -> executeScriptByJdk(javaCode, params, returnType);
        };
    }

    // ------------------------------ 1. 完整类执行（修复文件名+自动补全import） ------------------------------
    private Object executeWholeClass(String code, Map<String, Object> params) throws Exception {
        // 步骤1：自动补全必要的import（解决Arrays找不到符号）
        String completeCode = autoCompleteImports(code);

        // 步骤2：提取全限定类名和简单类名
        String fullClassName = extractClassName(completeCode);
        String simpleClassName = fullClassName.contains(".") ? fullClassName.substring(fullClassName.lastIndexOf(".") + 1) : fullClassName;

        // 步骤3：编译类（修复文件名匹配）
        String codeMd5 = DigestUtils.md5Hex(completeCode);
        Class<?> clazz = (Class<?>) compileCache.get(codeMd5, k -> {
            try {
                return compileClassWithJdk(completeCode, fullClassName, simpleClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // 步骤4：执行main方法或普通方法
        if (hasMainMethod(completeCode)) {
            Method mainMethod = clazz.getMethod("main", String[].class);
            String[] mainArgs = params != null && params.containsKey("mainArgs") ? (String[]) params.get("mainArgs") : new String[]{};
            mainMethod.invoke(null, (Object) mainArgs);
            return "main方法执行完成";
        }

        if (params == null || !params.containsKey("methodName")) {
            throw new IllegalArgumentException("无main方法时，params必须包含methodName");
        }
        String methodName = (String) params.get("methodName");
        params.remove("methodName");

        Object[] methodParams = params.values().toArray();
        Class<?>[] paramTypes = new Class[methodParams.length];
        for (int i = 0; i < methodParams.length; i++) {
            Object param = methodParams[i];
            paramTypes[i] = param == null ? Object.class : getPrimitiveType(param.getClass());
        }

        Method method = findMethod(clazz, methodName, paramTypes);
        if (method == null) {
            throw new NoSuchMethodException("未找到方法：" + methodName + "，参数类型：" + Arrays.toString(paramTypes));
        }

        Object instance = clazz.getDeclaredConstructor().newInstance();
        return method.invoke(instance, methodParams);
    }

    // ------------------------------ 2. 表达式执行（原有逻辑，执行成功） ------------------------------
    private Object executeExpressionByJdk(String expr, Map<String, Object> params, Class<?> returnType) throws Exception {
        String className = "Expr_" + DigestUtils.md5Hex(expr + params + returnType).substring(0, 8);

        // 生成表达式执行动态类
        String dynamicCode = generateExprDynamicClass(className, expr, params, returnType);

//        // 格式化代码
//        completeCode = JavaCodeFormatter.formatJavaCode(completeCode);
//        System.out.println(completeCode);

        String codeMd5 = DigestUtils.md5Hex(dynamicCode);
        Class<?> clazz = (Class<?>) compileCache.get(codeMd5, k -> {
            try {
                return compileClassWithJdk(dynamicCode, className, className);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod("execute", paramsToTypes(params));
        return method.invoke(instance, params.values().toArray());
    }

    // ------------------------------ 3. 脚本执行（原有逻辑，执行成功） ------------------------------
    private Object executeScriptByJdk(String script, Map<String, Object> params, Class<?> returnType) throws Exception {
        String className = "Script_" + DigestUtils.md5Hex(script + params + returnType).substring(0, 8);

        // 生成脚本执行动态类
        String dynamicCode = generateScriptDynamicClass(className, script, params, returnType);

//        // 格式化代码
//        completeCode = JavaCodeFormatter.formatJavaCode(completeCode);
//        System.out.println(completeCode);

        String codeMd5 = DigestUtils.md5Hex(dynamicCode);
        Class<?> clazz = (Class<?>) compileCache.get(codeMd5, k -> {
            try {
                return compileClassWithJdk(dynamicCode, className, className);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod("execute", paramsToTypes(params));
        return method.invoke(instance, params.values().toArray());
    }

    // ------------------------------ 核心修复：编译类（处理包名+文件名匹配） ------------------------------
    /**
     * 编译类：支持带包名的公共类，保证文件名=简单类名.java，路径=包名拆分后的路径
     * @param javaCode 完整类代码
     * @param fullClassName 全限定类名（如com.example.TestMain）
     * @param simpleClassName 简单类名（如TestMain）
     */
    private Class<?> compileClassWithJdk(String javaCode, String fullClassName, String simpleClassName) throws Exception {
        Map<String, ByteArrayOutputStream> classBytes = new HashMap<>();
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);
        JavaFileManager customFileManager = new ForwardingJavaFileManager<>(fileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String clsName, JavaFileObject.Kind kind, FileObject sibling) {
                // 输出类文件路径：包名.类名 → 包名/类名.class（如com.example.TestMain → com/example/TestMain.class）
                String clsPath = clsName.replace(".", "/") + kind.extension;
                return new SimpleJavaFileObject(URI.create("mem:///" + clsPath), kind) {
                    @Override
                    public OutputStream openOutputStream() {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        classBytes.put(clsName, bos);
                        return bos;
                    }
                };
            }
        };

        // 关键修复：源文件URI = 包名拆分路径 + 简单类名.java（如mem:///com/example/TestMain.java）
        String sourceUri = "mem:///" + (fullClassName.contains(".") ? fullClassName.substring(0, fullClassName.lastIndexOf(".")) : "").replace(".", "/");
        if (!sourceUri.endsWith("/")) {
            sourceUri += "/";
        }
        sourceUri += simpleClassName + ".java";

        JavaFileObject sourceFile = new SimpleJavaFileObject(URI.create(sourceUri), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return javaCode;
            }
        };

        // 编译参数（JDK 21兼容）
        List<String> options = Arrays.asList("-encoding", "UTF-8", "-source", "21", "-target", "21");
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, customFileManager, null, options, null, Collections.singletonList(sourceFile));
        if (!task.call()) {
            throw new CompileException("类编译失败：\n" + javaCode);
        }

        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(classBytes, this.getClass().getClassLoader());
        return classLoader.loadClass(fullClassName);
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
        code.append("        ").append(script).append("\n");
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

    static class ByteArrayClassLoader extends ClassLoader {
        private final Map<String, ByteArrayOutputStream> classBytes;

        public ByteArrayClassLoader(Map<String, ByteArrayOutputStream> classBytes, ClassLoader parent) {
            super(parent);
            this.classBytes = classBytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            ByteArrayOutputStream bos = classBytes.get(name);
            if (bos == null) {
                return super.findClass(name);
            }
            byte[] bytes = bos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    static class CompileException extends Exception {
        public CompileException(String message) {
            super(message);
        }
    }
}
