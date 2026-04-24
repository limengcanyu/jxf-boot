package com.spring.boot.mybatisplus.druid.dynamic.datasource.service;

import com.alibaba.druid.pool.DruidDataSource;

public interface DynamicDataSourceService {
    DruidDataSource getTenantDataSource(String tenantId);
}
