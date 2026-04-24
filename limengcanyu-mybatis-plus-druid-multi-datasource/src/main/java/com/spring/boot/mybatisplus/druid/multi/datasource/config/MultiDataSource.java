package com.spring.boot.mybatisplus.druid.multi.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <p>Description: 动态数据源类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/24 0024
 */
public class MultiDataSource extends AbstractRoutingDataSource {
    /**
     * 决定当前使用哪个数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.getDbType();
    }
}
