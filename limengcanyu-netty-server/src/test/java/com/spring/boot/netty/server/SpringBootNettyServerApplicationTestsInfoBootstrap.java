package com.spring.boot.netty.server;

import com.spring.boot.netty.common.constant.RedisConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
class SpringBootNettyServerApplicationTestsInfoBootstrap {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
        String key = RedisConstant.DEVICE_ID_LIST + ":" + "127.0.0.1:7001";
        stringRedisTemplate.opsForSet().remove(key, "1");
    }

    @Test
    void test1() {
        stringRedisTemplate.opsForSet().add("netty-server-list", "localhost:7001");
        stringRedisTemplate.opsForSet().add("netty-server-list", "localhost:7002");
        stringRedisTemplate.opsForSet().add("netty-server-list", "localhost:7003");

        stringRedisTemplate.opsForSet().add("netty-server-list:localhost:7001", "localhost:8001");
        stringRedisTemplate.opsForSet().add("netty-server-list:localhost:7001", "localhost:8002");

        stringRedisTemplate.opsForSet().add("netty-server-list:localhost:7002", "localhost:9001");
        stringRedisTemplate.opsForSet().add("netty-server-list:localhost:7002", "localhost:9002");
    }

    @Test
    void test2() {
        Set<String> members = stringRedisTemplate.opsForSet().members("netty-server-list");
        assert members != null;
        members.forEach(System.out::println);

        System.out.println("========================================");

        Set<String> members1 = stringRedisTemplate.opsForSet().members("netty-server-list:localhost:7001");
        assert members1 != null;
        members1.forEach(System.out::println);

    }

    @Test
    void redis() {
        stringRedisTemplate.convertAndSend(RedisConstant.TOPIC1, "test message");
    }

}
