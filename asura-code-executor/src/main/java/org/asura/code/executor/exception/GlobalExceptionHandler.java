package org.asura.code.executor.exception;

import org.asura.code.executor.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public ResultDTO<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return ResultDTO.of(e.getCode(), e.getMessage(), null);
    }

    /**
     * 安全异常处理
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultDTO<Void> handleSecurityException(SecurityException e) {
        log.warn("安全异常：{}", e.getMessage());
        return ResultDTO.of(403, e.getMessage(), null);
    }

    /**
     * 文件大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResultDTO<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("文件大小超限：{}", e.getMessage());
        return ResultDTO.fail("文件大小超过限制（1MB）");
    }

    /**
     * 参数异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultDTO<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常：{}", e.getMessage());
        return ResultDTO.fail(e.getMessage());
    }

    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDTO<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ResultDTO.fail("系统内部错误：" + e.getMessage());
    }
}
