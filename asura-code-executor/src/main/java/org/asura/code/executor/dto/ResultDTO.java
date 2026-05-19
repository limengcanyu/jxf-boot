package org.asura.code.executor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 响应码
    private int code;
    // 响应消息
    private String msg;
    // 响应数据
    private T data;
    // 请求ID
    private String requestId;
    // 时间戳
    private long timestamp;

    // 成功响应
    public static <T> ResultDTO<T> success(T data) {
        return ResultDTO.<T>builder()
                .code(200)
                .msg("操作成功")
                .data(data)
                .requestId(java.util.UUID.randomUUID().toString().replace("-", ""))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    // 失败响应
    public static <T> ResultDTO<T> fail(String msg) {
        return ResultDTO.<T>builder()
                .code(500)
                .msg(msg)
                .data(null)
                .requestId(java.util.UUID.randomUUID().toString().replace("-", ""))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ResultDTO<T> fail(int code, String msg) {
        return ResultDTO.<T>builder()
                .code(code)
                .msg(msg)
                .data(null)
                .requestId(java.util.UUID.randomUUID().toString().replace("-", ""))
                .timestamp(System.currentTimeMillis())
                .build();
    }

    // 自定义响应
    public static <T> ResultDTO<T> of(int code, String msg, T data) {
        return ResultDTO.<T>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .requestId(java.util.UUID.randomUUID().toString().replace("-", ""))
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
