package org.asura.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RExecutorService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.asura.redisson.DistributedExecutorServiceConfig.executorOptions;

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
