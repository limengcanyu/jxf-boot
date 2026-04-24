package com.spring.boot.netty.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles a client-side channel.
 */
@Slf4j
@Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Client: 连接服务端成功: {}", remoteAddress);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Client: 断开服务端连接: {}", remoteAddress);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Client: 收到服务端: {} 数据: {}", remoteAddress, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Client: 读取服务端数据完毕: {}", remoteAddress);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Client: 与服务端连接异常: {}", remoteAddress);
        cause.printStackTrace();
        ctx.close();
    }
}
