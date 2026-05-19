package org.asura.redisson.server;

import org.asura.redisson.interfaces.HelloService;
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
