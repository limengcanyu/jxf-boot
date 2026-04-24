package com.spring.boot.netty.server.config;

import com.spring.boot.netty.common.constant.RedisConstant;
import com.spring.boot.netty.server.message.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    ChannelTopic topic1() {
        return new ChannelTopic(RedisConstant.TOPIC1);
    }

    @Bean
    RedisMessageSubscriber redisMessageSubscriber() {
        return new RedisMessageSubscriber();
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisMessageSubscriber());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListener(), topic1());
        return container;
    }

}
