package com.spring.boot.tomcat.exchange.rate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>Description: 汇率工具类接口实现类：中国银行外汇牌价汇率查询类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class BocExchanger implements RateExchanger {
    @Override
    public BigDecimal exchange(Currencies from, Currencies to) {
        if (Currencies.CNY != to) {
            throw new IllegalArgumentException("BOC只支持其它币种兑人民币的汇率");
        }

        try {
            //获取中国银行外汇牌价网页内容
            String html = Spider.getFromBoc(from.getBocCode());
            BocRateParser bocRateParser = new BocRateParser();
            //解析中国银行外汇牌价网页内容，获取100人民币的汇率值
            String result = bocRateParser.parser(html);
            //100人民币的汇率值除以100，得出1人民币的汇率值
            BigDecimal rate = new BigDecimal(result).divide(new BigDecimal(100), 4, RoundingMode.HALF_DOWN);

            return rate;
        } catch (Exception var6) {
            return null;
        }
    }

    public static void main(String[] args) {
        RateExchanger rateExchanger = new BocExchanger();
        BigDecimal usd2Cny = rateExchanger.exchange(Currencies.USD, Currencies.CNY);
        System.out.println("美元兑人民币汇率：" + usd2Cny);
    }
}
