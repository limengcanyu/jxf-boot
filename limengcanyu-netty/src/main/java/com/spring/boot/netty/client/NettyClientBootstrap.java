package com.spring.boot.netty.client;

import com.spring.boot.netty.properties.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 长连接方式，启动之后与服务端建立连接，不关闭
 */
@Slf4j
@Order(2) // 在Netty Server之后启动
@Component
public class NettyClientBootstrap implements CommandLineRunner {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8023;

    public static Channel channel = null;

    @Autowired
    private NettyProperties nettyProperties;

    @Autowired
    private EventLoopGroup group;

    @Autowired
    private Bootstrap bootstrap;

    @PreDestroy
    public void close() {
        group.shutdownGracefully();
        log.info("Client: 关闭客户端成功");
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the connection attempt.
        channel = bootstrap.connect(nettyProperties.getHost(), nettyProperties.getPort()).sync().channel();

        String remoteAddress = channel.remoteAddress().toString();
        log.info("Client: 连接服务端成功: {}", remoteAddress);
    }
}
