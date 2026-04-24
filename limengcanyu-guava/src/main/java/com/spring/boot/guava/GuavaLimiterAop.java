package com.spring.boot.guava;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class GuavaLimiterAop {
    /**
     * 不同的接口，不同的流量控制
     * map的key为 Limiter.key
     */
    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Around("@annotation(com.spring.boot.guava.GuavaLimiter)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        GuavaLimiter guavaLimiter = method.getAnnotation(GuavaLimiter.class);
        if (guavaLimiter != null) {
            String key = guavaLimiter.key();
            RateLimiter rateLimiter = null;

            if (!limitMap.containsKey(key)) {
                rateLimiter = RateLimiter.create(guavaLimiter.permitsPerSecond());
                limitMap.put(key, rateLimiter);
                log.info("新建了令牌桶={}，容量={}", key, guavaLimiter.permitsPerSecond());
            }

            rateLimiter = limitMap.get(key);

            boolean acquire = rateLimiter.tryAcquire(guavaLimiter.timeout(), guavaLimiter.timeunit());
            if (!acquire) {
                log.error("令牌桶={}，获取令牌失败", key);
                this.responseFail(guavaLimiter.msg());
                return null;
            }
        }

        return joinPoint.proceed();
    }

    /**
     * 直接向前端抛出异常
     *
     * @param msg 提示信息
     */
    private void responseFail(String msg) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        assert response != null;
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(500);
        response.getWriter().write(msg);
    }
}
