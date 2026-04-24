package com.spring.boot.rabbitmq.producer.service.impl;

import com.spring.boot.rabbitmq.producer.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description:
 *
 * @author rock
 * time 2020/7/14 0014 8:48
 */
@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public String putRequest2Queue(String message) {
        rabbitTemplate.setExchange("directExchange");
        rabbitTemplate.convertAndSend("directExchangeRoutingKey1", "directExchangeRoutingKey1Message");
        return "success";
    }
}
