package org.akuma.advice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan("org.akuma.advice.filter")
@SpringBootApplication
public class AkumaAdviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkumaAdviceApplication.class, args);
    }

}
