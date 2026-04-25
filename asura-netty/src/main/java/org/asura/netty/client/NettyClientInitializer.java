package org.asura.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // String编码器、解码器
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // 若添加多个handler，只会第一个有效
        // and then business logic.
        pipeline.addLast(new NettyClientHandler());
    }
}
