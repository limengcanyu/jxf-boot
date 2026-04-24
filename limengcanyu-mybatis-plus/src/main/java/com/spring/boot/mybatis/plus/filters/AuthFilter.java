package com.spring.boot.mybatis.plus.filters;

import com.spring.boot.mybatis.plus.util.UserContext;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("AuthFilter doFilter =============================");

        // 模拟从token中获取用户信息
        Map<String, Object> user = new HashMap<>();
        user.put("userId", "U001");
        user.put("username", "南风不竞");

        // 将用户信息放入用户上下文中
        UserContext.set(user);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
