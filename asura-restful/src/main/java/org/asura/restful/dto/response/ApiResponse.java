package org.asura.restful.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API响应")
public class ApiResponse<T> {

    @Schema(description = "响应码", example = "200")
    private Integer code;

    @Schema(description = "响应消息", example = "success")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "时间戳")
    private Long timestamp = System.currentTimeMillis();

    public ApiResponse() {}

    public ApiResponse(Integer code, String message, T data, Long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp != null ? timestamp : System.currentTimeMillis();
    }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private Integer code;
        private String message;
        private T data;
        private Long timestamp = System.currentTimeMillis();

        public ApiResponseBuilder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(code, message, data, timestamp);
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "created", data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(204, "no content", null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(500, message, null, System.currentTimeMillis());
    }

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
}