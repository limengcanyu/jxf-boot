package com.spring.boot.shardingsphere;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spring.boot.shardingsphere.mapper")
@SpringBootApplication
public class SpringBootShardingsphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShardingsphereApplication.class, args);
    }

}
