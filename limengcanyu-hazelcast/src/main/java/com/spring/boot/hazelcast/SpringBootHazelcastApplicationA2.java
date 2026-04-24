package com.spring.boot.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootHazelcastApplicationA2 {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "a2");

        SpringApplication.run(SpringBootHazelcastApplicationA2.class, args);
    }

}
