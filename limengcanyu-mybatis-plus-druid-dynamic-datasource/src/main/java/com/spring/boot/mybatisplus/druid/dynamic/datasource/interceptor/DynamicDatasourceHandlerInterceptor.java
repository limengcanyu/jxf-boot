package com.spring.boot.mybatisplus.druid.dynamic.datasource.interceptor;

import com.spring.boot.mybatisplus.druid.dynamic.datasource.config.DynamicDataSource;
import com.spring.boot.mybatisplus.druid.dynamic.datasource.config.DynamicDataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 动态数据源拦截器
 */
@Component
public class DynamicDatasourceHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求参数中的租户ID
        String tenantId = request.getParameter("tenantId");

        // 设置当前租户的数据源key
        DynamicDataSourceContextHolder.setDataSourceKey(tenantId);

        // 在动态数据源集合中查找当前租户的数据源，若不存在，则初始化一个新的数据源，并添加到动态数据源集合中
        if (!dynamicDataSource.contains(tenantId)) {
            // 只有url不一致，其它数据源连接信息，所有的租户都相同 或者不同也可以
            String url = "jdbc:mysql://192.168.31.128:3306/schema_name?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8";

            url = url.replace("schema_name", tenantId);

            dynamicDataSource.initializeDynamicDataSource(tenantId, url);
        }

        return true;
    }

}
