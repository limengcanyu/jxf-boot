package com.spring.boot.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class SpringBootMongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootMongoApplication.class);
    }
}
