package org.asura.flowable.exception;

import lombok.Getter;

/**
 * 流程异常类
 */
@Getter
public class ProcessException extends RuntimeException {

    private final String errorCode;

    public ProcessException(String message) {
        super(message);
        this.errorCode = "PROCESS_ERROR";
    }

    public ProcessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProcessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PROCESS_ERROR";
    }

    public ProcessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}