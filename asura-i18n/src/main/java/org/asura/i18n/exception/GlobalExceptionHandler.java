package org.asura.i18n.exception;

import org.asura.i18n.enums.CommonErrorEnum;
import org.asura.i18n.utils.I18nEnumUtils;
import org.asura.i18n.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器（支持国际化）
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        String errorMsg = e.getErrorEnum().getDesc();
        log.error("业务异常：{}，Locale：{}", errorMsg, LocaleContextHolder.getLocale(), e);
        return Result.error(e.getCode(), errorMsg);
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        String errorMsg = I18nEnumUtils.getDesc(CommonErrorEnum.SYSTEM_ERROR);
        log.error("系统异常：{}，Locale：{}", errorMsg, LocaleContextHolder.getLocale(), e);
        return Result.error(500, errorMsg);
    }

}

