package org.asura.shardingsphere;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.spring.boot.shardingsphere.mapper")
@SpringBootApplication
public class AsuraShardingSphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraShardingSphereApplication.class, args);
    }

}
