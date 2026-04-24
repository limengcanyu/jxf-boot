package com.spring.boot.tomcat.exchange.rate;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * <p>Description: 汇率工具类接口实现类：汇率网－－中国人民银行人民币汇率中间价</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class HuiLvExchanger implements RateExchanger {
    @Override
    public BigDecimal exchange(Currencies from, Currencies to) {
        String url = "https://www.huilv.cc/renminyinhanghuilv.html";

        try {
            //获取网页内容
            String data = Spider.pickData(url, Charset.forName("GB2312"));
            HuiLvRateParser huiLvRateParser = new HuiLvRateParser();

            String result = huiLvRateParser.parser(data, from.getCode());
            if (!StringUtils.isEmpty(result)) {
                return new BigDecimal(result).divide(new BigDecimal(100));
            }

            return null;
        } catch (Exception var7) {
            return null;
        }
    }

    public static void main(String[] args) {
        RateExchanger rateExchanger = new HuiLvExchanger();
        BigDecimal usd2Cny = rateExchanger.exchange(Currencies.USD, Currencies.CNY);
        BigDecimal eur2Cny = rateExchanger.exchange(Currencies.EUR, Currencies.CNY);

        System.out.println("美元兑人民币汇率: " + usd2Cny);
        System.out.println("欧元兑人民币汇率: " + eur2Cny);
    }
}
