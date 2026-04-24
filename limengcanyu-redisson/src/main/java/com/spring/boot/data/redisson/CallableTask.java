package com.spring.boot.data.redisson;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Long> {

    @RInject
    private RedissonClient redissonClient;

    @RInject
    private String taskId;

    @Override
    public Long call() throws Exception {
        RMap<String, Integer> map = redissonClient.getMap("myMap");
        Long result = 0L;
        for (Integer value : map.values()) {
            result += value;
        }
        return result;
    }

}
