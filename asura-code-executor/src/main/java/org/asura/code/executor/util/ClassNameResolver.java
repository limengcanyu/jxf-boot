package org.asura.code.executor.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名解析器
 */
@Component
public class ClassNameResolver {

    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("(public\\s+)?class\\s+([a-zA-Z_$][a-zA-Z0-9_$]*)");

    /**
     * 从文件名解析类名
     */
    public String resolveFromFileName(String fileName) {
        if (!StringUtils.hasText(fileName) || !fileName.endsWith(".java")) {
            throw new IllegalArgumentException("无效的Java文件名：" + fileName);
        }

        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 从源码解析类名
     */
    public String resolveClassName(String javaCode) {
        if (!StringUtils.hasText(javaCode)) {
            throw new IllegalArgumentException("源码不能为空");
        }

        Matcher matcher = CLASS_NAME_PATTERN.matcher(javaCode);
        if (!matcher.find()) {
            throw new IllegalArgumentException("无法从源码中解析类名");
        }

        return matcher.group(2);
    }
}
