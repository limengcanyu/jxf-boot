package com.spring.boot.log.config;

import com.spring.boot.log.interceptor.RequestMonitorInterceptor;
import com.spring.boot.log.metrics.RequestMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${request.monitor.slow-threshold-ms:500}") // 可配置的慢请求阈值
    private long slowThresholdMs;

    private final RequestMetrics requestMetrics;
    private final ApplicationContext applicationContext;

    public WebConfig(RequestMetrics requestMetrics, ApplicationContext applicationContext) {
        this.requestMetrics = requestMetrics;
        this.applicationContext = applicationContext;
    }

    @Bean
    public RequestMonitorInterceptor requestMonitorInterceptor() {
        return new RequestMonitorInterceptor(
                slowThresholdMs,
                requestMetrics,
                applicationContext
        );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestMonitorInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/static/**", "/error", // 排除静态资源和错误页
                        "/actuator/**" // 排除监控端点自身
                );
    }
}

