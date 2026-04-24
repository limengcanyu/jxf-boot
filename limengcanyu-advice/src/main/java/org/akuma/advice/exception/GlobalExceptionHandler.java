package org.akuma.advice.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> errorHandler(Exception e) {
        System.out.println("全局异常处理：" + e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("msg", "系统异常，请稍后重试！");
        return map;
    }
}
