package com.spring.boot.tomcat.exchange.rate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <p>Description: 汇率解析实现类：类中国银行外汇牌价网页解析类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class BocRateParser implements RateParser {
    @Override
    public String parser(String html) {
        try {
            //解析HTML内容
            Document document = Jsoup.parse(html);
            //获取页面元素值：BOC_main class中table元素中第1行记录第7个单元格的文本值
            String result = document.getElementsByClass("BOC_main").select("table").select("tr").get(1).select("td").get(6).text();

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
