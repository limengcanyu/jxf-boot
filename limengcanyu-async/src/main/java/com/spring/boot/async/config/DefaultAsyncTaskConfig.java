package com.spring.boot.async.config;

import com.spring.boot.async.utils.VisibleThreadPoolTaskExecutor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Data
@EnableAsync(proxyTargetClass = true)
@ConfigurationProperties(prefix="async.task")
@Configuration
public class DefaultAsyncTaskConfig {

    @Value("${corePoolSize: 10}")
    private int corePoolSize;

    @Value("${maxPoolSize: 200}")
    private int maxPoolSize;

    @Value("${queueCapacity: 10}")
    private int queueCapacity;

    @Value("${threadNamePrefix: artanis}")
    private String threadNamePrefix;

    // 自定义异步执行线程池
    @Bean(name = "asyncTaskExecutor")
    public TaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
