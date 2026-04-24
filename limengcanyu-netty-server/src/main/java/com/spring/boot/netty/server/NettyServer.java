package com.spring.boot.netty.server;

import com.spring.boot.netty.common.constant.RedisConstant;
import com.spring.boot.netty.common.utils.InetAddressUtils;
import com.spring.boot.netty.common.utils.RedisUtilsService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 服务器启动类
 */
@Slf4j
@Component
public class NettyServer implements CommandLineRunner {

    @Resource
    private NioEventLoopGroup boosGroup;

    @Resource
    private NioEventLoopGroup workerGroup;

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${netty.port}")
    private int port;

    @Autowired
    private RedisUtilsService redisUtilsService;

    @Override
    public void run(String... args) throws Exception {
        // 绑定端口启动
        serverBootstrap.bind(port).sync();
        log.info("启动Netty服务端成功: 监听端口: {}", port);

        String hostAddress = InetAddressUtils.getLocalHost() + ":" + port;
        stringRedisTemplate.opsForSet().add(RedisConstant.NETTY_SERVER_LIST, hostAddress);
        log.info("保存Netty服务端信息到Redis中成功: {}", hostAddress);
    }

    @PreDestroy
    public void close() {
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("关闭Netty服务端成功: 监听端口: {}", port);

        String hostAddress = InetAddressUtils.getLocalHost() + ":" + port;
        stringRedisTemplate.opsForSet().remove(RedisConstant.NETTY_SERVER_LIST, hostAddress);
        log.info("从Redis中删除Netty服务端信息成功: {}", hostAddress);

        String key = RedisConstant.DEVICE_ID_LIST + ":" + hostAddress;
        redisUtilsService.deleteAllMember(key);
        log.info("从Redis中删除Netty服务端设备信息成功: {}", hostAddress);
    }

}
