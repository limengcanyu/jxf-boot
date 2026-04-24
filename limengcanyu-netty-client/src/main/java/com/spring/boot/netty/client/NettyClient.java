package com.spring.boot.netty.client;

import com.spring.boot.netty.common.properties.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class NettyClient implements CommandLineRunner {

    public static Channel channel = null;

    @Autowired
    NettyProperties nettyProperties;

    @Autowired
    private NioEventLoopGroup group;

    @Autowired
    private Bootstrap bootstrap;

    // 启动之后获取长连接通道
    @Override
    public void run(String... args) throws Exception {
        try {
            // 连接到Nginx
            channel = bootstrap.connect("192.168.242.129", 6000).sync().channel();

            channel.writeAndFlush("向Netty服务端发送长连接标识: longChannel");
        } catch (InterruptedException e) {
            log.error("连接到Netty服务端异常！", e);
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void close() {
        group.shutdownGracefully();
        log.info("关闭Netty客户端成功！");
    }

}
