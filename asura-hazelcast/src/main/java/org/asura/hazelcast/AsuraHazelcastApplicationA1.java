package org.asura.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsuraHazelcastApplicationA1 {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "a1");

        SpringApplication.run(AsuraHazelcastApplicationA1.class, args);
    }
}
