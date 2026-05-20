package org.asura.skywalking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 应用配置类
 */
@Slf4j
@Configuration
@EnableAsync
public class AppConfig {

    /**
     * 异步任务执行器
     */
    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                5,
                60L,
                java.util.concurrent.TimeUnit.SECONDS,
                new java.util.concurrent.LinkedBlockingQueue<>(100),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("async-task-" + thread.getId());
                    thread.setDaemon(true);
                    return thread;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        log.info("Async executor initialized with corePoolSize={}, maxPoolSize={}",
                executor.getCorePoolSize(), executor.getMaximumPoolSize());
        return executor;
    }

}