package com.spring.boot.netty.client;

import com.spring.boot.netty.common.bean.Message;
import com.spring.boot.netty.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class SpringBootNettyClientApplicationTestsBootstrap {

    @Test
    void contextLoads() {
        Map<String, String> map = new HashMap<>();
        map.put("device-id", "1");
        Message message = new Message(map);
        String json = new String(message.getContent(), StandardCharsets.UTF_8);
        System.out.println(json);

        Map o = JsonUtils.json2Object(json, Map.class);
        System.out.println(o);
        System.out.println(o.get("device-id"));
    }

}
