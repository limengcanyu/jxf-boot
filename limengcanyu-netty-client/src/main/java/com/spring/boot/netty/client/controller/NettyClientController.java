package com.spring.boot.netty.client.controller;

import com.spring.boot.netty.client.NettyClient;
import com.spring.boot.netty.client.utils.NettyClientUtilsService;
import com.spring.boot.netty.common.utils.JsonUtils;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NettyClientController {

    @Autowired
    private NettyClientUtilsService nettyClientUtilsService;

    /**
     * http://localhost:8091/sendMessageUsingLongChannel?name=张三&age=20
     *
     * @param student
     * @return
     */
    @GetMapping("/sendMessageUsingLongChannel")
    public String sendMessageUsingLongChannel(Student student) {
        NettyClient.channel.writeAndFlush(JsonUtils.object2Json(student));
        return "发送消息到Netty服务端成功！";
    }

    /**
     * http://localhost:8091/closeChannel
     *
     * @return
     */
    @GetMapping("/closeChannel")
    public String closeChannel() {
        NettyClient.channel.writeAndFlush("closeLongChannel");
        nettyClientUtilsService.closeChannel(NettyClient.channel);
        return "关闭连接成功";
    }

    /**
     * http://localhost:8091/sendMessageUsingShortChannel?name=张三&age=20
     *
     * @param student
     * @return
     */
    @GetMapping("/sendMessageUsingShortChannel")
    public String sendMessageUsingShortChannel(Student student) {
        Channel channel = nettyClientUtilsService.getChannel();
        channel.writeAndFlush(JsonUtils.object2Json(student));
        nettyClientUtilsService.closeChannel(channel);
        return "发送消息到Netty服务端成功！";
    }

}
