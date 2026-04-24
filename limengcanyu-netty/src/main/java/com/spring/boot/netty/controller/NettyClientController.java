package com.spring.boot.netty.controller;

import com.spring.boot.netty.client.NettyClientBootstrap;
import com.spring.boot.netty.client.NettyClientUtilsService;
import com.spring.boot.netty.utils.JsonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class NettyClientController {
    @Autowired
    private NettyClientUtilsService nettyClientUtilsService;

    /**
     * http://localhost:8080/sendMessage1
     *
     * @return
     */
    @RequestMapping("/sendMessage1")
    public String sendMessage1() {
        // 使用临时连接发送消息
        Channel channel = nettyClientUtilsService.getChannel();
        channel.writeAndFlush(JsonUtils.object2Json(getUser()));
        nettyClientUtilsService.closeChannel(channel);
        return "发送消息成功";
    }

    /**
     * http://localhost:8080/sendMessage2
     *
     * @return
     */
    @RequestMapping("/sendMessage2")
    public String sendMessage2() {
        // 使用长连接发送消息
        NettyClientBootstrap.channel.writeAndFlush(JsonUtils.object2Json(getUser()));
        return "发送消息成功";
    }

    Map<String, Object> getUser() {
        List<String> nameList = new ArrayList<>();
        nameList.add("张三");
        nameList.add("赵四");
        nameList.add("王五");

        Map<String, Object> user = new HashMap<>();
        user.put("name", nameList.get(new Random().nextInt(3)));
        user.put("age", 21);

        return user;
    }

}
