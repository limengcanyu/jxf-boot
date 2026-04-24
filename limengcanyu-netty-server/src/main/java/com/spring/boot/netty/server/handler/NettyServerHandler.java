package com.spring.boot.netty.server.handler;

import com.spring.boot.netty.common.constant.RedisConstant;
import com.spring.boot.netty.common.utils.InetAddressUtils;
import com.spring.boot.netty.common.utils.JsonUtils;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * server端监听消息处理器
 * Sharable 表明可以被多个channel一起使用
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    // 客户端-设备ID映射
    public static Map<String, String> clientMap = new ConcurrentHashMap<>(16);

    // 设备ID-ChannelHandlerContext映射
    public static Map<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>(16);

    @Value("${netty.port}")
    private int port;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 客户端上线的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("Netty客户端 {} 连接成功: 通道: {}", remoteAddress, ctx.channel());
    }

    /**
     * 客户端掉线的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("Netty客户端 {} 断开成功: 通道: {}", remoteAddress, ctx.channel());

        // 从客户端-设备ID映射中删除映射信息
        String deviceId = clientMap.get(remoteAddress.substring(1));
        clientMap.remove(remoteAddress.substring(1));

        // 从设备ID-ChannelHandlerContext映射中删除映射信息
        ctxMap.remove(deviceId);

        String hostAddress = InetAddressUtils.getLocalHost() + ":" + port;
        String key = RedisConstant.DEVICE_ID_LIST + ":" + hostAddress;
        stringRedisTemplate.opsForSet().remove(key, deviceId);
        log.info("从设备列表中删除断开设备成功: {}", deviceId);

    }


    /**
     * 读取客户端信息
     *
     * @param ctx
     * @param message
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("收到Netty客户端 {} 通道: {} 消息: {}", remoteAddress, ctx.channel(), message);

        // 发送消息给客户端
        ChannelFuture future = ctx.writeAndFlush("收到客户端 " + remoteAddress + " 的消息: " + message);

        if (message.contains("shortChannel")) {
            log.debug("短连接，关闭通道");
            future.addListener(ChannelFutureListener.CLOSE);
        }

        if (message.contains("longChannel")) {
            log.debug("长连接，不关闭通道");
        }

        if (message.contains("closeLongChannel")) {
            log.debug("收到客户端请求，关闭长连接通道");
            future.addListener(ChannelFutureListener.CLOSE);
        }

        // 设置客户端的设备ID
        if (message.contains("device-id")) {
            Map o = JsonUtils.json2Object(message, Map.class);
            String deviceId = o.get("device-id").toString();

            String hostAddress = InetAddressUtils.getLocalHost() + ":" + port;

            // 向Redis中添加Server地址-设备ID映射信息
            String key = RedisConstant.DEVICE_ID_LIST + ":" + hostAddress;
            stringRedisTemplate.opsForSet().add(key, deviceId);

            // 保存设备ID-ChannelHandlerContext映射信息
            ctxMap.put(deviceId, ctx);

            // 保存客户端-设备ID映射信息
            clientMap.put(remoteAddress.substring(1), deviceId);
        }
    }

    /**
     * 异常发生时候调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.error("客户端 {} 连接异常: 通道: {}", remoteAddress, ctx.channel());
        cause.printStackTrace();
        ctx.close();
    }

}
