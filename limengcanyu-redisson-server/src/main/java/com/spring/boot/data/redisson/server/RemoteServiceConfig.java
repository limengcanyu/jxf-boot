package com.spring.boot.data.redisson.server;

import com.spring.boot.data.redisson.interfaces.HelloService;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RRemoteService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteServiceConfig {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private HelloService helloService;

    @PostConstruct
    public void init () {
        RRemoteService remoteService = redissonClient.getRemoteService();
        remoteService.register(HelloService.class, helloService);
    }
}
