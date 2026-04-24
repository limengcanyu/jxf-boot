package com.spring.boot.data.redisson.server;

import com.spring.boot.data.redisson.interfaces.HelloService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(Map<String, Object> map) {
        System.out.println(map);
        return "hello";
    }

}
