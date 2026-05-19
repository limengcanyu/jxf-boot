package org.asura.flaw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class AsuraFlawApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraFlawApplication.class, args);
    }

}
