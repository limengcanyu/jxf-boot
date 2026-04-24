package com.spring.boot.netty.client.utils;

import com.spring.boot.netty.common.properties.NettyProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NettyClientUtilsService {

    @Autowired
    private Bootstrap bootstrap;

    @Autowired
    private NettyProperties nettyProperties;

    // 连接服务端并获取通道
    public Channel getChannel() {

        try {
            Channel channel = bootstrap.connect("192.168.242.129", 6000).sync().channel();
            log.info("获取连接成功 服务端 {} 通道: {}", channel.remoteAddress().toString(), channel);

            // 发送连接标识，短连接
            channel.writeAndFlush("shortChannel");

            return channel;
        } catch (InterruptedException e) {
            log.error("获取连接异常！", e);
            throw new RuntimeException(e);
        }
    }

    // 关闭通道
    public void closeChannel(Channel channel) {
        log.info("开始关闭连接 服务端 {} 通道: {}", channel.remoteAddress().toString(), channel);

        try {
            channel.closeFuture().sync(); // 使用此方法会阻塞，直到客户端关闭才会关闭通道
        } catch (InterruptedException e) {
            log.info("关闭连接异常 服务端 {} 通道: {}", channel.remoteAddress().toString(), channel, e);
            throw new RuntimeException(e);
        }

        log.info("关闭连接成功 服务端 {} 通道: {}", channel.remoteAddress().toString(), channel);
    }

}
