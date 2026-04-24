package com.spring.boot.data.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.spring.boot.data.redisson.DistributedExecutorServiceConfig.executorOptions;

@Slf4j
@SpringBootTest
public class DistributedExecutorServiceTests {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void contextLoads() throws ExecutionException, InterruptedException {
        RExecutorService executorService = redissonClient.getExecutorService("myExecutor", executorOptions());

        executorService.submit(new RunnableTask(123));

        Future<Long> future = executorService.submit(new CallableTask());
        Long result = future.get();
        log.debug("result: {}", result);


    }

}
