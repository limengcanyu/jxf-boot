package com.spring.boot.data.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;

@Slf4j
public class RunnableTask implements Runnable {

    @RInject
    private RedissonClient redissonClient;

    @RInject
    private String taskId;

    private long param;

    public RunnableTask() {
    }

    public RunnableTask(long param) {
        this.param = param;
    }

    @Override
    public void run() {
        RAtomicLong atomic = redissonClient.getAtomicLong("myAtomic");
        long l = atomic.addAndGet(param);
        log.debug("l: {}", l);
    }

}
