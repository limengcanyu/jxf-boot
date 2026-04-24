package com.spring.boot.tomcat.exchange.rate;

import java.math.BigDecimal;

/**
 * <p>Description: 汇率工具类接口</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public interface RateExchanger {
    BigDecimal exchange(Currencies from, Currencies to);
}
