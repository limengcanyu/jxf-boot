package com.spring.boot.tomcat.exchange.rate;

import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * <p>Description: 汇率工具类接口实现类：IP138汇率查询类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class IP138Exchanger implements RateExchanger {
    @Override
    public BigDecimal exchange(Currencies from, Currencies to) {
        String urlPattern = "http://qq.ip138.com/hl.asp?from=%s&to=%s&q=1";
        String url = String.format(urlPattern, from, to);

        try {
            String data = Spider.pickData(url, Charset.forName("GB2312"));
            String result = (new IP138RateParser()).parser(data);
            return BigDecimal.valueOf(Double.valueOf(result));
        } catch (Exception e) {
            return null;
        }
    }
}
