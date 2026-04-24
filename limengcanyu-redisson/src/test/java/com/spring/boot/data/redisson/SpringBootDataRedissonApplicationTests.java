package com.spring.boot.data.redisson;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringBootDataRedissonApplicationTests {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
    }

    @Test
    void lock() throws InterruptedException {
        RLock lock = redissonClient.getLock("myLock");

        // wait for lock aquisition up to 100 seconds
        // and automatically unlock it after 10 seconds
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {
				TimeUnit.SECONDS.sleep(10);
            } finally {
                lock.unlock();
            }
        }
    }
}
