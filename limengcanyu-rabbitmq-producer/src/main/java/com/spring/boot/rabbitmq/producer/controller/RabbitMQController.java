package com.spring.boot.rabbitmq.producer.controller;

import com.spring.boot.rabbitmq.producer.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 *
 * @author rock
 * time 2020/7/14 0014 8:47
 */
@RestController
public class RabbitMQController {
    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * localhost:8080//directExchange/directExchangeRoutingKey1Message
     *
     * @param message
     * @return
     */
    @RequestMapping("/directExchange/{message}")
    public String directExchange(@PathVariable String message) {
        return rabbitMQService.putRequest2Queue(message);
    }
}
