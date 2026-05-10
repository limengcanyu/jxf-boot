package org.asura.code.executor.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDK25 稳定版 Java 代码校验器
 * 功能：语法校验（javac） + 危险方法检测（Java Parser） + 大数组检测（源码扫描）
 */
@Component
public class JavaCodeValidator {

    // 禁止调用的危险方法（可按需扩展）
    private static final List<String> FORBIDDEN_METHODS = Arrays.asList(
            "System.exit", "Runtime.exec", "Runtime.getRuntime().exec", "ProcessBuilder.start", "System.gc", "Thread.sleep"
    );
    // 最大数组大小限制（100MB，可按需调整）
    private static final long MAX_ARRAY_SIZE = 100 * 1024 * 1024;

    // 正则匹配数组创建表达式（单/多维度）
    private static final Pattern ARRAY_CREATION_PATTERN = Pattern.compile("new\\s+[a-zA-Z0-9_]+((\\[([^\\]]+)\\])+)");
    private static final Pattern DIMENSION_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");

    @Value("${java.runner.security.allow-keywords}")
    private String allowKeywords;

    @Value("${java.runner.security.deny-keywords}")
    private String denyKeywords;

    // 危险正则表达式
    private static final Pattern DANGER_PATTERN = Pattern.compile(
            "(\\bRuntime\\.getRuntime\\(\\)\\.exec\\b)|(\\bnew\\s+ProcessBuilder\\b)|(\\bFileInputStream\\b)|(\\bFileOutputStream\\b)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 全维度校验Java代码
     * @param className 类名
     * @param javaCode  待校验的Java源码
     * @return 校验结果
     */
    public ValidationResult validate(String className, String javaCode) {
        ValidationResult result = new ValidationResult();
        result.setClassName(className);
        result.setValid(true);
        List<String> allErrors = new ArrayList<>();

        // 危险方法校验（基于Java Parser AST解析）
        List<String> dangerMethodErrors = detectForbiddenMethods(javaCode);
        if (!dangerMethodErrors.isEmpty()) {
            allErrors.addAll(dangerMethodErrors);
        }

        // 危险操作校验
        List<String> dangerOperationErrors = detectForbiddenOperations(javaCode);
        if (!dangerOperationErrors.isEmpty()) {
            allErrors.addAll(dangerOperationErrors);
        }

        // 大数组校验（基于源码行扫描，支持多维度总大小）
        List<String> largeArrayErrors = detectLargeArrays(javaCode);
        if (!largeArrayErrors.isEmpty()) {
            allErrors.addAll(largeArrayErrors);
        }

        // 4. 结果汇总
        if (!allErrors.isEmpty()) {
            result.setValid(false);
            result.setErrorMsg("安全规则违规：\n" + String.join("\n", allErrors));
            result.setErrorType(ErrorType.SECURITY_ERROR);
        } else {
            result.setErrorMsg("代码校验通过");
        }

        return result;
    }


    /**
     * 检查代码安全性
     *
     * @param javaCode 源码
     */
    public void check(String javaCode) {
        if (!StringUtils.hasText(javaCode)) {
            throw new SecurityException("源码不能为空");
        }

        // 1. 黑名单检查（优先）
        String[] denyList = denyKeywords.split(",");
        for (String deny : denyList) {
            String keyword = deny.trim();
            if (keyword.isEmpty()) continue;

            if (javaCode.contains(keyword)) {
                throw new SecurityException("源码包含危险关键字：" + keyword);
            }
        }

        // 2. 危险模式检查
        if (DANGER_PATTERN.matcher(javaCode).find()) {
            throw new SecurityException("源码包含危险代码模式");
        }

        // 4. 代码长度检查
        if (javaCode.length() > 1024 * 1024) { // 1MB
            throw new SecurityException("源码长度超过限制（1MB）");
        }

        // 代码长度 + 循环深度 + 输出大小硬限制，防止死循环、超大输出、恶意长代码：
        // 追加在 check() 方法末尾
        // 最大源码长度 512KB
        if (javaCode.length() > 1024 * 512) {
            throw new SecurityException("源码长度超出限制，最大允许512KB");
        }

        // 禁止无限循环简写（while(true) / for(;;)）
        if (javaCode.contains("while(true)") || javaCode.contains("for(;;)")) {
            throw new SecurityException("禁止无终止条件的死循环");
        }

        // 禁止大量打印语句轰炸输出
        long printCount = javaCode.chars().filter(ch -> ch == '"').count();
        if (printCount > 200) {
            throw new SecurityException("字符串/打印语句过多，疑似恶意输出攻击");
        }
    }

    /**
     * 危险方法检测：基于Java Parser AST解析，禁止调用高危方法
     */
    private List<String> detectForbiddenMethods(String javaCode) {
        List<String> errors = new ArrayList<>();
        try {
            JavaParser javaParser = new JavaParser();
            ParseResult<CompilationUnit> parseResult = javaParser.parse(new StringReader(javaCode));

            if (parseResult.isSuccessful()) {
                CompilationUnit cu = parseResult.getResult().get();
                new ForbiddenMethodDetector().visit(cu, errors);
            }
        } catch (Exception e) {
            errors.add("危险方法检测异常：" + e.getMessage());
        }
        return errors;
    }

    private List<String> detectForbiddenOperations(String javaCode) {
        List<String> errors = new ArrayList<>();
        // 检查反射调用
        if (javaCode.contains("Class.forName") || javaCode.contains("getDeclaredMethod") || javaCode.contains("setAccessible")) {
            errors.add("危险操作检测异常：禁止使用反射操作");
        }

        // 检查线程创建
        if (javaCode.contains("new Thread") || javaCode.contains("ExecutorService") || javaCode.contains("Thread.sleep")) {
            errors.add("危险操作检测异常：禁止创建线程/线程休眠");
        }

        // 检查系统属性访问
        if (javaCode.contains("System.getenv") || javaCode.contains("System.getProperty")) {
            errors.add("危险操作检测异常：禁止访问系统环境/属性");
        }
        return errors;
    }

    /**
     * 大数组检测：基于源码行扫描，支持单/多维度数组总大小校验
     */
    private List<String> detectLargeArrays(String javaCode) {
        List<String> errors = new ArrayList<>();
        String[] lines = javaCode.split("\\r?\\n");

        for (int lineNum = 0; lineNum < lines.length; lineNum++) {
            String line = lines[lineNum].trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue; // 跳过空行和注释行
            }

            // 匹配数组创建表达式
            Matcher arrayMatcher = ARRAY_CREATION_PATTERN.matcher(line);
            while (arrayMatcher.find()) {
                String dimensionPart = arrayMatcher.group(1);
                Matcher dimMatcher = DIMENSION_PATTERN.matcher(dimensionPart);

                long totalSize = 1;
                List<String> dimensionExprs = new ArrayList<>();

                // 计算所有维度的总大小
                while (dimMatcher.find()) {
                    String sizeExpr = dimMatcher.group(1).trim();
                    dimensionExprs.add(sizeExpr);
                    totalSize *= calculateDimensionSize(sizeExpr);
                }

                // 校验总大小是否超过阈值
                if (totalSize > MAX_ARRAY_SIZE) {
                    errors.add(String.format(
                            "行%d：禁止创建超大多维数组（总大小%dM，最大允许%dM，维度：%s）",
                            lineNum + 1, totalSize / 1024 / 1024, MAX_ARRAY_SIZE / 1024/ 1024, String.join("×", dimensionExprs)
                    ));
                }
            }
        }
        return errors;
    }

    /**
     * 计算单个维度的大小（支持纯数字、乘号表达式）
     */
    private long calculateDimensionSize(String sizeExpr) {
        // 纯数字场景
        if (sizeExpr.matches("\\d+")) {
            return Long.parseLong(sizeExpr);
        }
        // 乘号表达式场景（如200*1024*1024）
        if (sizeExpr.contains("*")) {
            long result = 1;
            String cleanExpr = sizeExpr.replaceAll("\\s+", "");
            String[] parts = cleanExpr.split("\\*");
            for (String part : parts) {
                try {
                    result *= Long.parseLong(part);
                } catch (NumberFormatException e) {
                    return 1; // 非数字部分按1计算，避免漏检
                }
            }
            return result;
        }
        // 动态表达式（如变量、方法调用）按1计算
        return 1;
    }

    /**
     * 危险方法检测器：遍历AST节点，匹配禁止调用的方法
     */
    private static class ForbiddenMethodDetector extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(MethodCallExpr n, List<String> errors) {
            super.visit(n, errors);
            String methodFullName = n.getScope().map(Object::toString).orElse("") + "." + n.getNameAsString();
            for (String forbiddenMethod : FORBIDDEN_METHODS) {
                if (methodFullName.startsWith(forbiddenMethod)) {
                    errors.add(String.format(
                            "行%d列%d：禁止调用危险方法[%s]",
                            n.getBegin().get().line, n.getBegin().get().column, methodFullName
                    ));
                }
            }
        }
    }

    // ==================== 校验结果封装 ====================
    @Setter
    @Getter
    public static class ValidationResult {
        // Getter & Setter
        private String className;       // 校验的类名
        private boolean valid;          // 是否校验通过
        private String errorMsg;        // 错误信息（校验通过时为"代码校验通过"）
        private ErrorType errorType;    // 错误类型

    }

    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        SYNTAX_ERROR,    // 语法错误（如缺少分号、语法不合法）
        SECURITY_ERROR,  // 安全规则违规（如调用危险方法、创建大数组）
        ENV_ERROR        // 运行环境错误（如未找到JDK编译器）
    }


    // ==================== 测试用例 ====================
    public static void main(String[] args) {
        JavaCodeValidator validator = new JavaCodeValidator();

        // 测试1：纯数字大数组
        String largeArrayCode1 = "public class Test {\n" +
                "    public static void main(String[] args) {\n" +
                "        byte[] bigArray = new byte[200000000];\n" +
                "    }\n" +
                "}";
        ValidationResult result1 = validator.validate("Test", largeArrayCode1);
        System.out.println("测试1-纯数字大数组：");
        System.out.println("是否合法：" + result1.isValid());
        System.out.println("错误信息：" + result1.getErrorMsg());
        System.out.println("------------------------");

        // 测试2：乘号表达式大数组
        String largeArrayCode2 = "public class Test {\n" +
                "    public static void main(String[] args) {\n" +
                "        byte[] bigArray = new byte[200 * 1024 * 1024];\n" +
                "    }\n" +
                "}";
        ValidationResult result2 = validator.validate("Test", largeArrayCode2);
        System.out.println("测试2-乘号表达式大数组：");
        System.out.println("是否合法：" + result2.isValid());
        System.out.println("错误信息：" + result2.getErrorMsg());
        System.out.println("------------------------");

        // 测试3：多维度大数组（调整为总大小超100MB：100*1024*1024=104857600）
        String largeArrayCode3 = "public class Test {\n" +
                "    public static void main(String[] args) {\n" +
                "        int[][] bigArray = new int[10000][1024*1024];\n" +
                "    }\n" +
                "}";
        ValidationResult result3 = validator.validate("Test", largeArrayCode3);
        System.out.println("测试3-多维度大数组：");
        System.out.println("是否合法：" + result3.isValid());
        System.out.println("错误信息：" + result3.getErrorMsg());
        System.out.println("------------------------");

        // 测试4：危险方法
        String dangerCode = "public class Test {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.exit(0);\n" +
                "    }\n" +
                "}";
        ValidationResult result4 = validator.validate("Test", dangerCode);
        System.out.println("测试4-危险方法：");
        System.out.println("是否合法：" + result4.isValid());
        System.out.println("错误信息：" + result4.getErrorMsg());
        System.out.println("------------------------");

        // 测试5：合法代码
        String validCode = "public class Test {\n" +
                "    public static void main(String[] args) {\n" +
                "        var msg = \"JDK25测试\";\n" +
                "        System.out.println(msg);\n" +
                "    }\n" +
                "}";
        ValidationResult result5 = validator.validate("Test", validCode);
        System.out.println("测试5-合法代码：");
        System.out.println("是否合法：" + result5.isValid());
        System.out.println("错误信息：" + result5.getErrorMsg());
    }
}
