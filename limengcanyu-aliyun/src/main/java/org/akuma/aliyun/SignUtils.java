package org.akuma.aliyun;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class SignUtils {

    // 预定义编码方法
    private static final String ENCODING = "UTF-8";

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }

    // 预定义编码时间格式Timestamp。参数Timestamp必须符合ISO8601规范，并需要使用UTC时间，时区为+0。
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    private static final String HTTP_METHOD = "GET";
    private static final String SEPARATOR = "&";
    private static final String ALGORITHM = "HmacSHA1";

    public static String sign(Map<String, String> parameters) {
        String signature = null;

        try {
            // 排序请求参数
            String[] sortedKeys = parameters.keySet().toArray(new String[]{});
            Arrays.sort(sortedKeys);

            // 构造 stringToSign 字符串
            StringBuilder stringToSign = new StringBuilder();
            stringToSign.append(HTTP_METHOD).append(SEPARATOR);
            stringToSign.append(percentEncode("/")).append(SEPARATOR);
            StringBuilder canonicalizedQueryString = new StringBuilder();

            for (String key : sortedKeys) {
                // 这里注意编码 key 和 value
                canonicalizedQueryString.append("&")
                        .append(percentEncode(key)).append("=")
                        .append(percentEncode(parameters.get(key)));
            }

            // 这里注意编码 canonicalizedQueryString
            stringToSign.append(percentEncode(canonicalizedQueryString.substring(1)));
            log.debug("编码字符串: {}", stringToSign);

            // 以下是一段计算签名的示例代码
            String key = AliyunConst.accessSecret + "&";
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM));
            byte[] signData = mac.doFinal(stringToSign.toString().getBytes(ENCODING));

            signature = new String(Base64.getEncoder().encodeToString(signData));
            log.debug("签名字符串: {}", signature);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            log.debug("签名参数出现异常！");
        }

        return signature;
    }

    public static String generateUrl(String baseUrl, Map<String, String> parameters) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        parameters.forEach((s, s2) -> urlBuilder.append(s).append("=").append(s2).append("&"));
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        log.debug("请求URL: {}", urlBuilder.toString());
        return urlBuilder.toString();
    }
}
