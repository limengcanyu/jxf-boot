package com.spring.boot.tomcat.exchange.rate;

/**
 * <p>Description: 汇率币种枚举类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public enum Currencies {
    CNY("人民币", "CNY", "0"),
    USD("美元", "USD", "1316"),
    HKD("港币", "HKD", "1315"),
    TWD("台币", "TWD", "2895"),
    EUR("欧元", "EUR", "1326"),
    GBP("英镑", "GBP", "1314");

    private String code;
    private String bocCode;
    private String name;

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getBocCode() {
        return this.bocCode;
    }

    private Currencies(String name, String code, String bocCode) {
        this.name = name;
        this.code = code;
        this.bocCode = bocCode;
    }
}
