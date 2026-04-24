package com.spring.boot.netty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("netty")
@Configuration
public class NettyProperties {
    private String host;
    private int port;
}
