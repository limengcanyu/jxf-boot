package com.spring.boot.netty.server;

import com.spring.boot.netty.utils.JsonUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Handles a server-side channel.
 */
@Slf4j
@Sharable
public class NettyServerHandler1 extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Handler1: 客户端连接成功: {}", remoteAddress);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Handler1: 客户端断开连接: {}", remoteAddress);
    }
    
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        log.info("Handler1: 判断是否处理消息: {}", msg);
        Map<String, Object> user = JsonUtils.json2Object((String) msg, Map.class);
        return "张三".equals(user.get("name"));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Handler1: 收到客户端: {} 数据: {}", remoteAddress, request);

        // String编码器、解码器示例 发送消息给客户端
        Map<String, Object> user = JsonUtils.json2Object(request, Map.class);
        user.put("address", "上海市宝山区");
        ChannelFuture future = ctx.write(JsonUtils.object2Json(user));

        // 监听Channel是否要关闭
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Handler1: 读取客户端数据完毕: {}", remoteAddress);

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.debug("Handler1: 客户端异常: {}", remoteAddress);
        cause.printStackTrace();
        ctx.close();
    }
}
