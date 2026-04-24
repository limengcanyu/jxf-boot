package com.spring.boot.redis.reactive.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * <p>Description: Redis Config</p>
 *
 * @author rock.jiang
 * date 2019/06/20
 */

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializer<String> stringSerializer = RedisSerializer.string();
        RedisSerializer<Object> jsonSerializer = RedisSerializer.json();

        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> redisSerializationContextBuilder = RedisSerializationContext.newSerializationContext();
        redisSerializationContextBuilder.key(stringSerializer).value(jsonSerializer);

        RedisSerializationContext<String, Object> redisSerializationContext = redisSerializationContextBuilder.build();

        return new ReactiveRedisTemplate<>(factory, redisSerializationContext);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

}
