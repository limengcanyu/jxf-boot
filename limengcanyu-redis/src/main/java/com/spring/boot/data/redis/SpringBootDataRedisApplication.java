package com.spring.boot.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@SpringBootApplication
public class SpringBootDataRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataRedisApplication.class, args);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * http://localhost:8091/hello
     *
     * @return
     */
    @RequestMapping("/hello")
    public String hello() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        stringRedisTemplate.opsForValue().set("key", LocalDateTime.now().format(formatter), 3, TimeUnit.SECONDS);
        log.info(stringRedisTemplate.opsForValue().get("key"));
        return "hello";
    }

}
