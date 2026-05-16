package org.asura.ddd.structure.infrastructure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.asura.ddd.structure.infrastructure.context.TenantContextHolder;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // 从请求头获取租户ID（示例：X-Tenant-Id）
        String tenantIdStr = request.getHeader("X-Tenant-Id");

        if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
            Long tenantId = Long.parseLong(tenantIdStr);
            TenantContextHolder.setCurrentTenantId(tenantId);
        } else {
            // 设置默认租户ID（如公共租户）
            TenantContextHolder.setCurrentTenantId(0L);
        }

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        // 请求结束后清除上下文，防止内存泄漏
        TenantContextHolder.clear();
    }
}
