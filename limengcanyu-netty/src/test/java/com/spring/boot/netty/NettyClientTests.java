package com.spring.boot.netty;

import com.spring.boot.netty.client.NettyClientInitializer;
import com.spring.boot.netty.utils.JsonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class NettyClientTests {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer());

            Channel channel = bootstrap.connect("192.168.242.129", 6000).sync().channel();

            Map<String, Object> user = new HashMap<>();
            user.put("name", "张三");
            user.put("age", 21);

            channel.writeAndFlush(JsonUtils.object2Json(user));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventExecutors.shutdownGracefully();
            log.info("关闭客户端成功");
        }
    }
}
