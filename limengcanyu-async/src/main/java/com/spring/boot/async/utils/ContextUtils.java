package com.spring.boot.async.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * description:
 *
 * @author rock
 * time 2020/7/3 0003 10:41
 */
public class ContextUtils {
    // 可以在线程之间传递变量
    private static final ThreadLocal<String> context = new TransmittableThreadLocal<>();

    public static void setTenantId(String tenantId) {
        context.remove();
        context.set(tenantId);
    }

    public static String getTenantId() {
        return context.get();
    }
}
