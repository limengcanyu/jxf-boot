package com.spring.boot.tomcat.exchange.rate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <p>Description: 汇率解析实现类：获取汇率网汇率</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class HuiLvRateParser implements RateParser {
    @Override
    public String parser(String html) {
        return null;
    }

    @Override
    public String parser(String html, String code) {
        String result = null;
        try {
            Document document = Jsoup.parse(html);
            //获取网页中data-table class的table中行元素
            Elements trElements = document.getElementsByClass("data-table").select("tr");

            if (null != trElements && !trElements.isEmpty()) {
                //循环处理每一行
                for (Element nextTrElements : trElements) {
                    //获取当前行下的所有单元格元素
                    Elements tdElements = nextTrElements.select("td");
                    if (null != tdElements && !tdElements.isEmpty()) {
                        //获取第1个单元格元素
                        Element firstTdElement = tdElements.first();
                        System.out.println("当前行第1个单元格文本: " + firstTdElement.text());

                        //获取最后1个单元格元素
//                        Element lastTdElement = tdElements.last();
//                        System.out.println("当前行最后1个单元格文本: " + lastTdElement.text());

                        if (firstTdElement.hasText() && firstTdElement.text().startsWith(code)) {
                            //获取当前行倒数第2个元素，即汇率值
                            Element rateElement = tdElements.get(tdElements.size() - 2);
                            if (rateElement.hasText()) {
                                result = rateElement.text();
                            }
                        }
                    }

                }
            }

            System.out.println("兑人民币汇率: " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询汇率出现异常！");
            return null;
        }
    }
}
