package com.spring.boot.configuration.processor.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data // 必须加setter，getter方法
@Configuration
@ConfigurationProperties(prefix="async.task")
public class DefaultAsyncTaskConfig {

    @Value("${corePoolSize: 10}")
    private int corePoolSize;

    @Value("${maxPoolSize: 200}")
    private int maxPoolSize;

    @Value("${queueCapacity: 10}")
    private int queueCapacity;

    @Value("${threadNamePrefix: artanis}")
    private String threadNamePrefix;

}
