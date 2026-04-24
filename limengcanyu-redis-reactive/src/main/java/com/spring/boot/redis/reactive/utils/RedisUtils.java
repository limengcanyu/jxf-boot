//package com.spring.boot.redis.reactive.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * <p>Description: Redis Utils</p>
// *
// * @author rock.jiang
// * date 2019/06/20
// */
//@Component
//public class RedisUtils {
//    @Autowired
//    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
//
//    @Autowired
//    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;
//
//    // delete operation
//
//    public Mono<Long> delete(String key) {
//        return reactiveRedisTemplate.delete(key);
//    }
//
//    public Mono<Long> delete(Collection<String> keys) {
//        return reactiveRedisTemplate.delete(keys);
//    }
//
//    // expire operation
//
//    public Boolean expire(String key, long timeout, TimeUnit unit) {
//        try {
//            if (timeout > 0) {
//                reactiveRedisTemplate.expire(key, timeout, unit);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public Boolean expireAt(String key, Date date) {
//        return reactiveRedisTemplate.expireAt(key, date);
//    }
//
//    // string operation
//
//    public void setString(String key, String value) {
//        reactiveStringRedisTemplate.opsForValue().set(key, value);
//    }
//
//    public void setString(String key, String value, long timeout, TimeUnit unit) {
//        reactiveStringRedisTemplate.opsForValue().set(key, value, timeout, unit);
//    }
//
//    public String getString(String key) {
//        return reactiveStringRedisTemplate.opsForValue().get(key);
//    }
//
//    public void increment(String key, long delta) {
//        reactiveStringRedisTemplate.opsForValue().increment(key, delta);
//    }
//
//    // object operation
//
//    public void setObject(String key, Object value) {
//        reactiveRedisTemplate.opsForValue().set(key, value);
//    }
//
//    public void setObject(String key, Object value, long timeout, TimeUnit unit) {
//        reactiveRedisTemplate.opsForValue().set(key, value, timeout, unit);
//    }
//
//    public Object getObject(String key) {
//        return reactiveRedisTemplate.opsForValue().get(key);
//    }
//
//    // list operation
//
//    public <T> void setList(String key, List<T> objectList) {
//        reactiveRedisTemplate.opsForList().leftPush(key, objectList);
//    }
//
//    public <T> List<T> getList(String key) {
//        Object object = reactiveRedisTemplate.opsForList().leftPop(key, 2, TimeUnit.SECONDS);
//        if (object instanceof List){
//            return (List<T>) object;
//        }
//
//        return null;
//    }
//
//}
