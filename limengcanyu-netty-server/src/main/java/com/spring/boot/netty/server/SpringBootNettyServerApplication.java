package com.spring.boot.netty.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.spring.boot.netty")
public class SpringBootNettyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNettyServerApplication.class, args);
    }

}
