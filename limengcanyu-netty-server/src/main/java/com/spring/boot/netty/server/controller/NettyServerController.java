package com.spring.boot.netty.server.controller;

import com.spring.boot.netty.common.constant.RedisConstant;
import com.spring.boot.netty.common.utils.InetAddressUtils;
import com.spring.boot.netty.common.utils.JsonUtils;
import com.spring.boot.netty.server.handler.NettyServerHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class NettyServerController {

    @Value("${netty.port}")
    private int nettyPort;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * http://localhost:8081/sendMessage2Client
     *
     * @return
     */
    @RequestMapping("/sendMessage2Client")
    public String sendMessage2Client() {
        Collection<ChannelHandlerContext> ctxCollection = NettyServerHandler.ctxMap.values();

        ctxCollection.forEach(ctx -> ctx.writeAndFlush("这是服务端推送的消息"));

        return "ok";
    }

    /**
     * http://localhost:8081/sendMessage2Topic1
     *
     * @return
     */
    @RequestMapping("/sendMessage2Topic1")
    public String sendMessage2Topic1() {
        stringRedisTemplate.convertAndSend(RedisConstant.TOPIC1, "test message");
        return "ok";
    }

    /**
     * http://localhost:8081/sentHttp?deviceId=1
     *
     * @return
     */
    @RequestMapping("/sentHttp")
    public String sentHttp(String deviceId) {
        // 本Server绑定该设备，本Server处理，直接发送指令到Client
        String hostAddress = InetAddressUtils.getLocalHost() + ":" + nettyPort;
        Set<String> deviceIdSet = stringRedisTemplate.opsForSet().members(com.spring.boot.netty.common.constant.RedisConstant.DEVICE_ID_LIST + ":" + hostAddress);
        assert deviceIdSet != null;
        if (deviceIdSet.contains(deviceId)) {
            log.info("本Server绑定了该设备，本Server处理本次请求");
            ChannelHandlerContext ctx = NettyServerHandler.ctxMap.get(deviceId);
            ctx.writeAndFlush("我收到了HTTP请求，你来处理下我发送的指令吧");
        } else {
            // 本Server未绑定该设备，发送消息到topic，通知其它Server处理
            log.info("本Server未绑定该设备，由其它Server处理本次请求");
            Map<String, String> map = new HashMap<>();
            map.put("device-id", deviceId);
            stringRedisTemplate.convertAndSend(RedisConstant.TOPIC1, JsonUtils.object2Json(map));
        }

        return "ok";
    }
}
