package com.spring.boot.netty.client.handler;

import com.spring.boot.netty.common.utils.JsonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    @Value("${device.id}")
    private String deviceId;

    /**
     * 服务端上线的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("连接Netty服务端 {} 成功: 通道: {}", remoteAddress, ctx.channel());

        // 向服务端发送设备ID标识
        Map<String, String> map = new HashMap<>();
        map.put("device-id", deviceId);
        ctx.writeAndFlush(JsonUtils.object2Json(map));
        log.info("向Netty服务端发送消息成功: {}", JsonUtils.object2Json(map));
    }

    /**
     * 服务端掉线的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("断开Netty服务端 {} 成功: 通道: {}", remoteAddress, ctx.channel());
    }


    /**
     * 读取服务端消息
     *
     * @param ctx
     * @param message
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("收到Netty服务端 {} 通道: {} 消息: {} ", remoteAddress, ctx.channel(), message);
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
        log.error("连接Netty服务端 {} 异常: 通道: {}", remoteAddress, ctx.channel());
        cause.printStackTrace();
        ctx.close();
    }

}