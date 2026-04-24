package com.spring.boot.i18n.enums;

/**
 * 通用错误枚举
 */
public enum CommonErrorEnum implements I18nEnum {
    SYSTEM_ERROR("enum.error.system"),
    PARAM_ERROR("enum.error.param");

    private final String messageCode;

    CommonErrorEnum(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String getMessageCode() {
        return messageCode;
    }
}

