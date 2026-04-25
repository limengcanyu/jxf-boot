package org.asura.i18n.enums;

/**
 * 业务状态枚举
 */
public enum BusinessStatusEnum implements I18nEnum {
    SUCCESS("enum.status.success"),
    FAILED("enum.status.failed"),
    SUCCESS_WITH_PARAM("enum.status.success.param"); // 新增：带参数的成功文案

    private final String messageCode;

    BusinessStatusEnum(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String getMessageCode() {
        return messageCode;
    }
}

