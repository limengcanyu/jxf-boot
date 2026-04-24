package com.spring.boot.netty.client;

import com.spring.boot.netty.client.NettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyClientConfig {

    @Bean
    EventLoopGroup group() {
        return new NioEventLoopGroup();
    }

    @Bean
    Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group())
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
        return bootstrap;
    }
}
