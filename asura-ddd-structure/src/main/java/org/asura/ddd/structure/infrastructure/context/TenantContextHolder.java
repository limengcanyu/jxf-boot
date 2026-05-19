package org.asura.ddd.structure.infrastructure.context;

public class TenantContextHolder {

    private static final ThreadLocal<Long> tenantIdThreadLocal = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void setCurrentTenantId(Long tenantId) {
        tenantIdThreadLocal.set(tenantId);
    }

    public static Long getCurrentTenantId() {
        return tenantIdThreadLocal.get();
    }

    public static void clear() {
        tenantIdThreadLocal.remove();
    }
}