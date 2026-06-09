package org.asura.flowable.exception;

import org.asura.flowable.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProcessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleProcessException(ProcessException e) {
        LOGGER.warn("Process exception: code={}, message={}", e.getErrorCode(), e.getMessage());
        return ApiResponse.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleTaskNotFoundException(TaskNotFoundException e) {
        LOGGER.warn("Task not found: {}", e.getMessage());
        return ApiResponse.error("TASK_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(ProcessInstanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleProcessInstanceNotFoundException(ProcessInstanceNotFoundException e) {
        LOGGER.warn("Process instance not found: {}", e.getMessage());
        return ApiResponse.error("PROCESS_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(ProcessDefinitionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleProcessDefinitionNotFoundException(ProcessDefinitionNotFoundException e) {
        LOGGER.warn("Process definition not found: {}", e.getMessage());
        return ApiResponse.error("DEFINITION_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        LOGGER.warn("Validation exception: {}", errors);
        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setCode("VALIDATION_ERROR");
        response.setMessage("参数校验失败");
        response.setData(errors);
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.warn("Illegal argument: {}", e.getMessage());
        return ApiResponse.error("INVALID_ARGUMENT", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleAllExceptions(Exception e) {
        LOGGER.error("Unexpected exception: {}", e.getMessage(), e);
        return ApiResponse.error("INTERNAL_ERROR", "系统内部错误，请稍后重试");
    }
}