package com.spring.boot.netty.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.spring.boot.netty")
public class SpringBootNettyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNettyClientApplication.class, args);
    }

}
