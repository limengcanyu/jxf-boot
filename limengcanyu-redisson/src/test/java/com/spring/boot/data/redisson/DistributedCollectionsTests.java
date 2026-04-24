package com.spring.boot.data.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class DistributedCollectionsTests {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
    }


}
