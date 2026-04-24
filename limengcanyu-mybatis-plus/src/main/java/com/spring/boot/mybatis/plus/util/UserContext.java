package com.spring.boot.mybatis.plus.util;

import java.util.Map;

/**
 * 用户上下文对象
 */
public class UserContext {
    private static final ThreadLocal<Map<String, Object>> currentContext = new ThreadLocal<>();

    public static void set(Map<String, Object> user) {
        currentContext.set(user);
    }

    public static Map<String, Object> get(){
        return currentContext.get();
    }

    public static void remove() {
        currentContext.remove();
    }
}
