package com.spring.boot.data.redisson.client;

import com.spring.boot.data.redisson.interfaces.HelloService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringBootDataRedissonClientApplicationTests {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
        RRemoteService remoteService = redissonClient.getRemoteService();
        HelloService helloService = remoteService.get(HelloService.class);

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "rock");
        String result = helloService.hello(map);
        System.out.println(result);
    }

}
