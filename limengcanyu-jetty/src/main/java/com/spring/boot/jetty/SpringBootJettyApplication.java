package com.spring.boot.jetty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class SpringBootJettyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootJettyApplication.class);
    }

    /**
     * localhost:8072/echo
     *
     * @return
     */
    @RequestMapping("/echo")
    public String echo() {
        log.debug("this is jetty app echo");
        return "this is jetty app";
    }
}
