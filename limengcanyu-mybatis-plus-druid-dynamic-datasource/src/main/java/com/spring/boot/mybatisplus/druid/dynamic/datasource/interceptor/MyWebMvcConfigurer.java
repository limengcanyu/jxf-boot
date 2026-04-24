package com.spring.boot.mybatisplus.druid.dynamic.datasource.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器类
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private DynamicDatasourceHandlerInterceptor dynamicDatasourceHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(dynamicDatasourceHandlerInterceptor);
//        registry.addInterceptor(dynamicDatasourceHandlerInterceptor).addPathPatterns("/**");
    }
}
