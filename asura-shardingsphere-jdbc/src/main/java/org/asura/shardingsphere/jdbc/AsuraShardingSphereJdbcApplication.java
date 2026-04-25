package org.asura.shardingsphere.jdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spring.boot.shardingsphere.jdbc.dao.mapper")
@SpringBootApplication(scanBasePackages = "com.spring.boot")
public class AsuraShardingSphereJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraShardingSphereJdbcApplication.class, args);
    }

}
