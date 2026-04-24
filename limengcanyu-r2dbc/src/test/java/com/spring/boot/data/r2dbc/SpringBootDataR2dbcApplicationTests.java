package com.spring.boot.data.r2dbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.test.StepVerifier;

@Slf4j
@SpringBootTest
class SpringBootDataR2dbcApplicationTests {

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    void contextLoads() {
//        r2dbcEntityTemplate.getDatabaseClient().sql("CREATE TABLE person" +
//                        "(id VARCHAR(255) PRIMARY KEY," +
//                        "name VARCHAR(255)," +
//                        "age INT)")
//                .fetch()
//                .rowsUpdated()
//                .as(StepVerifier::create)
//                .expectNextCount(1)
//                .verifyComplete();

        r2dbcEntityTemplate.insert(SysUser.class)
                .using(new SysUser(6, "rock", 34, "rock@baomidou.com"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        r2dbcEntityTemplate.select(SysUser.class).first()
                .doOnNext(it -> log.info(it.toString()))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

}
