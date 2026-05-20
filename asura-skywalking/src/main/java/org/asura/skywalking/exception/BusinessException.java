package org.asura.skywalking.exception;

import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        this(-1, message);
    }

    public static BusinessException notFound(String resourceName, Long id) {
        return new BusinessException(404, resourceName + "不存在，ID: " + id);
    }

    public static BusinessException notFound(String resourceName, String identifier) {
        return new BusinessException(404, resourceName + "不存在: " + identifier);
    }

    public static BusinessException duplicate(String fieldName, String value) {
        return new BusinessException(400, fieldName + "已存在: " + value);
    }

}