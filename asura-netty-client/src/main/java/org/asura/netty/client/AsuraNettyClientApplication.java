package org.asura.netty.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.asura.netty.client")
public class AsuraNettyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraNettyClientApplication.class, args);
    }

}
