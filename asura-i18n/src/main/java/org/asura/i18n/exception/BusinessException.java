package org.asura.i18n.exception;

import org.asura.i18n.enums.CommonErrorEnum;
import org.asura.i18n.enums.I18nEnum;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final I18nEnum errorEnum;

    public BusinessException(CommonErrorEnum errorEnum) {
        super(errorEnum.getDesc());
        this.code = 500;
        this.errorEnum = errorEnum;
    }

    public BusinessException(int code, CommonErrorEnum errorEnum) {
        super(errorEnum.getDesc());
        this.code = code;
        this.errorEnum = errorEnum;
    }

    // Getter
    public int getCode() {
        return code;
    }

    public I18nEnum getErrorEnum() {
        return errorEnum;
    }
}

