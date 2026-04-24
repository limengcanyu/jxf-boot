package com.spring.boot.tomcat.interceptors;

import com.spring.boot.tomcat.annotations.RolePermission;
import com.spring.boot.tomcat.exceptions.PermissionException;
import com.spring.boot.tomcat.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // http header
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            System.out.println("header name: " + name + " value: " + request.getHeader(name));
        }

        String token = request.getHeader("token");
        if (ObjectUtils.isEmpty(token)) {
            throw new PermissionException("用户未登录！");
        }

        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            System.out.println("attribute name: " + name + " value: " + request.getAttribute(name));
        }

        // http get
        request.getParameterMap().forEach((key, value) -> System.out.println("parameter key: " + key + " value: " + Arrays.toString(value)));

        if (handler instanceof HandlerMethod handlerMethod) {
            RolePermission rolePermission = handlerMethod.getMethodAnnotation(RolePermission.class);
            if (!Objects.isNull(rolePermission)) {
                boolean required = rolePermission.required();

                if (required) {
                    // 从redis中获取用户角色
                    List<String> rolesOfRedis = redisService.getUserRoles("001");

                    String[] roles = rolePermission.roles();
                    for (String role : roles) {
                        if (!rolesOfRedis.contains(role)) {
                            throw new PermissionException("用户权限不足！");
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
