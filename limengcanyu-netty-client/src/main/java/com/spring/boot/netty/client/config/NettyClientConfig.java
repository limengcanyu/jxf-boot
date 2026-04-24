package com.spring.boot.netty.client.config;

import com.spring.boot.netty.client.handler.NettyClientHandler;
import com.spring.boot.netty.client.handler.NettyClientInitializer;
import com.spring.boot.netty.common.properties.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * netty配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
public class NettyClientConfig {
    @Autowired
    NettyProperties nettyProperties;

    @Bean
    public NioEventLoopGroup group() {
        return new NioEventLoopGroup();
    }

    @Bean
    public NettyClientInitializer nettyClientInitializer() {
        return new NettyClientInitializer();
    }

    @Bean
    public NettyClientHandler nettyClientHandler() {
        return new NettyClientHandler();
    }

    /**
     * 客户端启动器
     *
     * @return
     */
    @Bean
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group()) // 指定线程组
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class) // 指定通道
                .handler(nettyClientInitializer()); // 指定处理器
        return bootstrap;
    }

}
