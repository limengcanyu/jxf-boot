package org.asura.netty.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.spring.boot.netty")
public class AsuraNettyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraNettyServerApplication.class, args);
    }

}
