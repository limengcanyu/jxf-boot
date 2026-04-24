package com.spring.boot.tomcat.service;

import com.spring.boot.tomcat.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    public void valueSet() {
//        redisTemplate.opsForValue().set("key", "value", 3, TimeUnit.SECONDS);
//    }
//
//    public void valueGet() {
//        redisTemplate.opsForValue().get("key");
//    }

    public User getUser(String userId) {
        return User.builder().userId(userId).username("rock").age(21).build();
    }

    public List<String> getUserRoles(String userId) {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE1");
        roles.add("ROLE2");
        roles.add("ROLE3");
        return roles;
    }
}
