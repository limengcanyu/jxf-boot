package org.asura.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class AsuraMongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsuraMongoApplication.class);
    }
}
