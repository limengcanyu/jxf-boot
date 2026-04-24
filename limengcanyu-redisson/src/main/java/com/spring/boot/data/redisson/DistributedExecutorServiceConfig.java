package com.spring.boot.data.redisson;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.ExecutorOptions;
import org.redisson.api.RExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.redisson.api.executor.TaskFailureListener;
import org.redisson.api.executor.TaskFinishedListener;
import org.redisson.api.executor.TaskStartedListener;
import org.redisson.api.executor.TaskSuccessListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class DistributedExecutorServiceConfig {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        RExecutorService executor = redissonClient.getExecutorService("myExecutor");
        executor.registerWorkers(workerOptions());
    }

    WorkerOptions workerOptions() {
        WorkerOptions workerOptions = WorkerOptions.defaults()

                // Defines workers amount used to execute tasks.
                // Default is 1.
                .workers(2)

                // Defines Spring BeanFactory instance to execute tasks
                // with Spring's '@Autowired', '@Value' or JSR-330's '@Inject' annotation.
                .beanFactory(beanFactory)

                // Defines custom ExecutorService to execute tasks
                // Config.executor is used by default.
                .executorService(Executors.newFixedThreadPool(3))

                // Defines task timeout since task execution start moment
                .taskTimeout(60, TimeUnit.SECONDS)

                // add listener which is invoked on task successed event
                .addListener(new TaskSuccessListener() {
                    @Override
                    public void onSucceeded(String taskId, Object result) {
                        log.info("taskId: {} succeeded result: {}", taskId, result);
                    }
                })

                // add listener which is invoked on task failed event
                .addListener((TaskFailureListener) (taskId, exception) -> log.info("taskId: {} failed", taskId, exception))

                // add listener which is invoked on task started event
                .addListener((TaskStartedListener) taskId -> log.info("taskId: {} started", taskId))

                // add listener which is invoked on task finished event
                .addListener((TaskFinishedListener) taskId -> log.info("taskId: {} finished", taskId));

        return workerOptions;
    }

    public static ExecutorOptions executorOptions() {
        ExecutorOptions executorOptions = ExecutorOptions.defaults();

        // Defines task retry interval at the end of which task is executed again.
        // ExecutorService worker re-schedule task execution retry every 5 seconds.
        //
        // Set 0 to disable.
        //
        // Default is 5 minutes
        executorOptions.taskRetryInterval(10, TimeUnit.MINUTES);
        return executorOptions;
    }

}
