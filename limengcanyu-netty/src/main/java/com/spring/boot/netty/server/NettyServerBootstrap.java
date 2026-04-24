package com.spring.boot.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Order(1)
@Component
public class NettyServerBootstrap implements CommandLineRunner {

    static final int PORT = 8023;

    public static Channel channel = null;

    @Autowired
    private EventLoopGroup bossGroup;

    @Autowired
    private EventLoopGroup workerGroup;

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Override
    public void run(String... args) throws Exception {
        channel = serverBootstrap.bind(PORT).sync().channel();

        String localAddress = channel.localAddress().toString();
        log.info("Server: 服务端启动成功: {}", localAddress);
    }

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("Server: 服务端关闭成功");
    }

}
