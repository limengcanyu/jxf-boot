package com.spring.boot.tomcat.exchange.rate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <p>Description: 汇率解析实现类：获取IP138网汇率</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class IP138RateParser implements RateParser {
    @Override
    public String parser(String html) {
        try {
            Document document = Jsoup.parse(html);
            String result = document.getElementsByClass("rate").select("td").get(4).text();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String parser(String html, String code) {
        return null;
    }
}
