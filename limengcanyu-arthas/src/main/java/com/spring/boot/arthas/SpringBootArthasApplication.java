package com.spring.boot.arthas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class SpringBootArthasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootArthasApplication.class, args);
    }

    /**
     * http://localhost:8091/hello
     *
     * @return
     */
    @RequestMapping("/hello")
    public String hello() {
        log.info("call hello");
        return "hello";
    }

}
