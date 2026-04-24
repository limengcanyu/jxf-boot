package com.spring.boot.netty.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * netty属性类
 */
@Data
@ConfigurationProperties(prefix = "netty")
@Configuration
public class NettyProperties {
    /**
     * boss线程数量 默认为cpu线程数*2
     */
    private Integer boss = 1;

    /**
     * worker线程数量 默认为cpu线程数*2
     */
    private Integer worker = 4;

    /**
     * 连接超时时间 默认为30s
     */
    private Integer timeout = 30000;

    /**
     * 服务器地址 默认为本地
     */
    private String host = "127.0.0.1";

    /**
     * 服务器主端口 默认7000
     */
    private Integer port = 7000;
}
