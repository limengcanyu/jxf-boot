package com.spring.boot.mybatisplus.druid.dynamic.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源类
 * 继承 AbstractRoutingDataSource 则数据源切换交由Spring动态处理， determineCurrentLookupKey() 方法决定当前租户的数据源key
 */
@Primary
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    /**
     * 默认构造器初始化默认数据源，由容器调用此构造方法初始化
     */
    public DynamicDataSource() {
        initializeDefaultDataSource();
    }

    @Override
    protected String determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    /**
     * 初始化默认数据源
     */
    private void initializeDefaultDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.31.128:3306/artanis?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("Gaohang321");

        targetDataSources.put("artanis", dataSource);

        //目标数据源
        setTargetDataSources(targetDataSources);
        //默认数据源
        setDefaultTargetDataSource(dataSource);
    }

    /**
     * 初始化动态数据源
     *
     * @param tenantId
     * @param url
     */
    public void initializeDynamicDataSource(String tenantId, String url) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername("root");
        dataSource.setPassword("Gaohang321");

        // 将新建数据源添加到数据源集合中
        targetDataSources.put(tenantId, dataSource);

        // 重要，调用此方法使新增数据源有效
        afterPropertiesSet();
    }

    /**
     * 数据源集合中是否具有指定租户的数据源
     *
     * @param tenantId
     * @return
     */
    public boolean contains(String tenantId) {

        DruidDataSource tenantDataSource = (DruidDataSource) targetDataSources.get(tenantId);

        return !ObjectUtils.isEmpty(tenantDataSource);
    }

}
