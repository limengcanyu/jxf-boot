package com.spring.boot.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequestMapping("/example")
@RestController
public class ExampleController {

    /**
     * http://localhost:8091/example/hello
     *
     * @return
     */
    @GetMapping("/hello")
    public String hello() {
        log.info("call hello......");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
