package org.asura.common.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    /**
     * 编码 URL 参数
     *
     * @param value 参数
     * @return 编码后的参数
     */
    public static String encodeUtf8(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
