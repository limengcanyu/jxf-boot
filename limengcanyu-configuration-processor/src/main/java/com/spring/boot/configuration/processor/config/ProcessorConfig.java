package com.spring.boot.configuration.processor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
//@EnableConfigurationProperties({ProcessorConfig.class, DefaultAsyncTaskConfig.class})
//@EnableConfigurationProperties({ProcessorConfig.class, DefaultAsyncTaskConfig.class})
@ConfigurationProperties(prefix = ProcessorConfig.PREFIX, ignoreInvalidFields = true)
public class ProcessorConfig {
    public static final String PREFIX = "processor"; // 这里对应配置文件中的processor前缀

    private String name;
}
