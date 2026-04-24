package com.spring.boot.mybatisplus.sharding.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/25 0025
 */
@SpringBootTest
public class DataSourceTest {
    @Resource
    private DataSource dataSource;

    @Test
    public void test() {
        System.out.println("dataSource：" + dataSource);
    }
}
