package com.spring.boot.netty.server.config;

import com.spring.boot.netty.common.properties.NettyProperties;
import com.spring.boot.netty.server.handler.NettyServerInitializer;
import com.spring.boot.netty.server.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * netty配置
 */
@Configuration
@EnableConfigurationProperties
public class NettyServerConfig {
    @Autowired
    NettyProperties nettyProperties;

    /**
     * boss 线程池
     * 负责客户端连接
     *
     * @return
     */
    @Bean
    public NioEventLoopGroup boosGroup() {
        return new NioEventLoopGroup(nettyProperties.getBoss());
    }

    /**
     * worker线程池
     * 负责业务处理
     *
     * @return
     */
    @Bean
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(nettyProperties.getWorker());
    }

    @Bean
    public NettyServerInitializer nettyServerInitializer() {
        return new NettyServerInitializer();
    }

    @Bean
    public NettyServerHandler serverListenerHandler() {
        return new NettyServerHandler();
    }

    /**
     * 服务器启动器
     *
     * @return
     */
    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup(), workerGroup()) // 指定使用的线程组
                .channel(NioServerSocketChannel.class) // 指定使用的通道
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyProperties.getTimeout()) // 指定连接超时时间
                .childHandler(nettyServerInitializer()); // 指定worker处理器
        return serverBootstrap;
    }

}
