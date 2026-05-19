package org.asura.code.executor.core;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import java.util.HashMap;
import java.util.Map;

public class JavaCodeFormatter {

    /**
     * 格式化Java代码，确保无前置空格，从行首开始
     * @param unformattedCode 未格式化的Java代码字符串
     * @return 格式化后的代码（无前置空格）
     */
    public static String formatJavaCode(String unformattedCode) {
        // 1. 自定义格式化配置（使用全版本兼容的常量）
        Map<String, String> formatOptions = new HashMap<>();

        // 核心配置：设置缩进为4个空格，基础缩进级别为0（消除前置空格）
        formatOptions.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE); // 使用空格而非Tab
        formatOptions.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4"); // 每个缩进占4个空格
        formatOptions.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "120"); // 行最大长度

        // 2. 使用自定义配置创建格式化器
        CodeFormatter formatter = ToolFactory.createCodeFormatter(formatOptions);

        // 3. 格式化代码：缩进级别设为0（关键，消除类名前的前置空格）
        TextEdit edit = formatter.format(
                CodeFormatter.K_COMPILATION_UNIT, // 格式化完整Java类
                unformattedCode,
                0, // 起始偏移量
                unformattedCode.length(), // 代码总长度
                0, // 基础缩进级别设为0（核心：无前置空格）
                System.lineSeparator() // 自适应系统换行符
        );

        if (edit == null) {
            System.err.println("代码语法错误，格式化失败");
            return unformattedCode;
        }

        // 4. 应用格式化结果
        IDocument document = new Document(unformattedCode);
        try {
            edit.apply(document);
            return document.get();
        } catch (Exception e) {
            System.err.println("格式化异常：" + e.getMessage());
            return unformattedCode;
        }
    }

}
