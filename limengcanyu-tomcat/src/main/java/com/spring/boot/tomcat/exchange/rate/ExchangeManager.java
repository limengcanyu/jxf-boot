package com.spring.boot.tomcat.exchange.rate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>Description: 汇率获取静态工具类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class ExchangeManager {
    static List<RateExchanger> exchangers = new ArrayList();

    public static BigDecimal exchange(Currencies from, Currencies to) {
        ListIterator iterator = exchangers.listIterator();

        BigDecimal result;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            RateExchanger exchanger = (RateExchanger)iterator.next();
            result = exchanger.exchange(from, to);
        } while(null == result);

        return result;
    }

    static {
        exchangers.add(new BocExchanger());
        exchangers.add(new IP138Exchanger());
    }

    public static void main(String[] args) {
        BigDecimal usd2Cny = ExchangeManager.exchange(Currencies.USD, Currencies.CNY);
        BigDecimal eur2Cny = ExchangeManager.exchange(Currencies.EUR, Currencies.CNY);
        BigDecimal cny2Usd = new BigDecimal(1).divide(usd2Cny, 4, RoundingMode.HALF_UP);
        BigDecimal cny2Eur = new BigDecimal(1).divide(eur2Cny, 4, RoundingMode.HALF_UP);
        System.out.println("美元对人民币汇率：" + usd2Cny + " 人民币兑美元汇率: " + cny2Usd);
        System.out.println("欧元对人民币汇率：" + eur2Cny + " 人民币兑欧元汇率: " + cny2Eur);
    }
}
