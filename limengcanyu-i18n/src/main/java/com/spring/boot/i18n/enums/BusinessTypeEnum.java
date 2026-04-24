package com.spring.boot.i18n.enums;

/**
 * 业务类型枚举
 */
public enum BusinessTypeEnum implements I18nEnum {
    //    APPROVAL("enum.type.approval"), // 枚举编码校验错误示例
    PAY("enum.type.pay"),
    REFUND("enum.type.refund"),
    PAY_WITH_PARAM("enum.type.pay.param"); // 新增：带参数的支付文案

    private final String messageCode;

    BusinessTypeEnum(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String getMessageCode() {
        return messageCode;
    }
}

