package com.spring.boot.netty.server.message;

import com.spring.boot.netty.common.constant.RedisConstant;
import com.spring.boot.netty.common.utils.InetAddressUtils;
import com.spring.boot.netty.common.utils.JsonUtils;
import com.spring.boot.netty.server.handler.NettyServerHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;

@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Value("${netty.port}")
    private int port;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody());
        System.out.println("received topic1 message: " + json);

        Map map = JsonUtils.json2Object(json, Map.class);
        String deviceId = (String) map.get("device-id");

        String hostAddress = InetAddressUtils.getLocalHost() + ":" + port;
        Set<String> deviceIdSet = stringRedisTemplate.opsForSet().members(RedisConstant.DEVICE_ID_LIST + ":" + hostAddress);
        assert deviceIdSet != null;
        if (deviceIdSet.contains(deviceId)) {
            log.info("本Server绑定了该设备，本Server处理本次请求");

            ChannelHandlerContext ctx = NettyServerHandler.ctxMap.get(deviceId);
            ctx.writeAndFlush("我收到了HTTP请求，你来处理下我发送的指令吧");
        }
    }

}
