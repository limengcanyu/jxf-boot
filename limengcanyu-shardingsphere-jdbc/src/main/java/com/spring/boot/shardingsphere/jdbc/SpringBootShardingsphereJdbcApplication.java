package com.spring.boot.shardingsphere.jdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spring.boot.shardingsphere.jdbc.dao.mapper")
@SpringBootApplication(scanBasePackages = "com.spring.boot")
public class SpringBootShardingsphereJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShardingsphereJdbcApplication.class, args);
    }

}
