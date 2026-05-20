package org.asura.skywalking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    /**
     * 响应码
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
     * 成功响应
     */
    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .code(0)
                .message("success")
                .data(data)
                .build();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> CommonResponse<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     */
    public static <T> CommonResponse<T> error(Integer code, String message) {
        return CommonResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * 失败响应（默认错误码）
     */
    public static <T> CommonResponse<T> error(String message) {
        return error(-1, message);
    }

}