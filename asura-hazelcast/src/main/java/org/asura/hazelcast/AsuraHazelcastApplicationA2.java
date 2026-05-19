package org.asura.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsuraHazelcastApplicationA2 {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "a2");

        SpringApplication.run(AsuraHazelcastApplicationA2.class, args);
    }

}
