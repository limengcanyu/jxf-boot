package com.spring.boot.mybatisplus.druid.dynamic.datasource.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.spring.boot.mybatisplus.druid.dynamic.datasource.service.DynamicDataSourceService;
import org.springframework.stereotype.Service;

@Service
public class DynamicDataSourceServiceImpl implements DynamicDataSourceService {
    @Override
    public DruidDataSource getTenantDataSource(String tenantId) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.31.128:3306/alita?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("Gaohang321");

        return dataSource;
    }
}
