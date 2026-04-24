package com.spring.boot.data.redis;

import com.spring.boot.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringBootDataRedisApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testString () {
        stringRedisTemplate.opsForValue().set("key", "value", 300, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("key"));

        redisTemplate.opsForValue().set("key1", "value1", 300, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("key1"));

        redisTemplate.opsForValue().set("name", "xiaobai");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    @Test
    public void testSaveUser() {
        redisTemplate.opsForValue().set("user", new User("小白", 23));
        User user = (User) redisTemplate.opsForValue().get("user");
        System.out.println(user);
    }

    @Test
    public void testHash () {
        redisTemplate.opsForHash().put("user1", "name", "clarence");
        redisTemplate.opsForHash().put("user1", "age", "25");
        Map map = redisTemplate.opsForHash().entries("user1");
        System.out.println(map);
    }

    @Test
    public void testList () {
        redisTemplate.opsForList().leftPushAll("names", "xiaobai", "xiaohei", "xiaolan");
        List<Object> names = redisTemplate.opsForList().range("names", 0, 3);
        System.out.println(names);
    }

    @Test
    public void testSet () {
        redisTemplate.opsForSet().add("set", "a", "b", "c", "c");
        Set<Object> set = redisTemplate.opsForSet().members("set");
        System.out.println(set);
    }

    @Test
    public void testSortedSet () {
        redisTemplate.opsForZSet().add("class", "xiaobai", 90);

        Set aClass = redisTemplate.opsForZSet().rangeByScore("class", 90, 100);
        System.out.println("分数在90-100之间的元素：" + aClass);

        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("xiaohei", 88.0));
        set.add(new DefaultTypedTuple<>("xiaohui", 94.0));
        set.add(new DefaultTypedTuple<>("xiaolan", 84.0));
        set.add(new DefaultTypedTuple<>("xiaolv", 82.0));
        set.add(new DefaultTypedTuple<>("xiaohong", 99.0));
        redisTemplate.opsForZSet().add("class", set);

        Set aClass1 = redisTemplate.opsForZSet().range("class", 0, 6);
        System.out.println("分数由低到高的6个元素：" + aClass1);
    }

}
