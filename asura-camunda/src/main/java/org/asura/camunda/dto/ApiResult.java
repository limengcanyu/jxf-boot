package org.asura.camunda.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一API响应结果封装类
 * 
 * @param <T> 数据类型
 * @author System
 * @version 1.0
 * @since 2024-08-24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;

    public ApiResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResult(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ApiResult(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(200, "操作成功");
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息和数据）
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message);
    }

    /**
     * 失败响应（自定义状态码）
     */
    public static <T> ApiResult<T> error(Integer code, String message) {
        return new ApiResult<>(code, message);
    }

    /**
     * 参数错误响应
     */
    public static <T> ApiResult<T> badRequest(String message) {
        return new ApiResult<>(400, message);
    }

    /**
     * 未授权响应
     */
    public static <T> ApiResult<T> unauthorized(String message) {
        return new ApiResult<>(401, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> ApiResult<T> forbidden(String message) {
        return new ApiResult<>(403, message);
    }

    /**
     * 资源未找到响应
     */
    public static <T> ApiResult<T> notFound(String message) {
        return new ApiResult<>(404, message);
    }

    // Getter and Setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}