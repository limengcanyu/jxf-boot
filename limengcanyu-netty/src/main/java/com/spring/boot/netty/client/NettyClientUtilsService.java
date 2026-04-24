package com.spring.boot.netty.client;

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

    // 每次连接服务端获取一个通道
    public Channel getChannel() {
        try {
            Channel ch = bootstrap.connect(NettyClientBootstrap.HOST, NettyClientBootstrap.PORT).sync().channel();
            log.info("Client: 连接服务端成功 通道: {}", ch);
            return ch;
        } catch (InterruptedException e) {
            log.error("Client: 连接服务端异常");
            throw new RuntimeException(e);
        }
    }

    public void closeChannel(Channel ch) {
        try {
            // 等待Channel关闭
            ch.closeFuture().sync();
            log.info("Client: 断开服务端连接 通道: {}", ch);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
