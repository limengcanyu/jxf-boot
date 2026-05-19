package org.asura.redisson.client;

import org.asura.redisson.interfaces.HelloService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AsuraRedissonClientApplicationTests {

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
