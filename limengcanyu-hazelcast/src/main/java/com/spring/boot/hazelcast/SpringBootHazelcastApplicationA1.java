package com.spring.boot.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class SpringBootHazelcastApplicationA1 {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "a1");

        SpringApplication.run(SpringBootHazelcastApplicationA1.class, args);
    }
}
