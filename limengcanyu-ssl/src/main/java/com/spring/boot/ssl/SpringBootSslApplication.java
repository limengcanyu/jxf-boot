package com.spring.boot.ssl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootSslApplication {

    /**
     * https://localhost:8443/echo
     *
     * @return
     */
    @RequestMapping("/echo")
    public String echo() {
        return "hello ssl";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSslApplication.class, args);
    }

}
