package com.spring.boot.netty.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisUtilsService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void deleteAllMember(String key) {
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        assert members != null;
        members.forEach(member -> {
            stringRedisTemplate.opsForSet().remove(key, member);
        });
    }

}
