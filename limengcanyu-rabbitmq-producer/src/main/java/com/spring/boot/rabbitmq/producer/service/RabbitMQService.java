package com.spring.boot.rabbitmq.producer.service;

/**
 * description:
 *
 * @author rock
 * time 2020/7/14 0014 8:48
 */
public interface RabbitMQService {
    String putRequest2Queue(String message);
}
