package com.spring.boot.mybatisplus.druid.multi.datasource.config;

/**
 * <p>Description: 数据源枚举类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/24 0024
 */
public enum DBTypeEnum {
    dbArtanis("dbArtanis"), dbSamuro("dbSamuro"), dbValeera("dbValeera");

    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
