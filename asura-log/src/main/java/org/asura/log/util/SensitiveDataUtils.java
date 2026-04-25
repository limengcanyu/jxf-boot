package org.asura.log.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;

/**
 * 敏感数据处理工具类
 */
public class SensitiveDataUtils {
    // 敏感字段正则（匹配键值对中的敏感key）
    private static final Pattern SENSITIVE_FIELD_PATTERN = Pattern.compile(
            "(password|pwd|secret|token|mobile|phone|idcard|cardNo|bankCard)[:=][\"']?.*?[\"']?");

    // 手机号脱敏（保留前3后4）
    private static final Pattern MOBILE_PATTERN = Pattern.compile("(1[3-9]\\d{9})");

    // 身份证脱敏（保留前6后4）
    private static final Pattern IDCARD_PATTERN = Pattern.compile("(\\d{6})\\d{8}(\\d{4})");

    /**
     * 对字符串中的敏感信息进行脱敏
     */
    public static String desensitize(String content) {
        if (StringUtils.isBlank(content)) return "";

        // 1. 脱敏敏感字段值
        String result = SENSITIVE_FIELD_PATTERN.matcher(content)
                .replaceAll(match -> match.group(1) + "=***");

        // 2. 脱敏手机号
        result = MOBILE_PATTERN.matcher(result)
                .replaceAll(match -> match.group().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

        // 3. 脱敏身份证号
        result = IDCARD_PATTERN.matcher(result)
                .replaceAll("$1********$2");

        // 限制长度，避免日志过大
        return StringUtils.abbreviate(result, 2000);
    }

    /**
     * 获取客户端真实IP（处理代理场景）
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isInvalidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private static boolean isInvalidIp(String ip) {
        return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip);
    }
}

