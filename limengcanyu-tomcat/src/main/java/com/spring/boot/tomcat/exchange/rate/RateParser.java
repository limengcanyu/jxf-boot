package com.spring.boot.tomcat.exchange.rate;

/**
 * <p>Description: 汇率解析类：根据网页内容解析出对应的汇率文本</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public interface RateParser {
    String parser(String html);

    String parser(String html, String code);
}
