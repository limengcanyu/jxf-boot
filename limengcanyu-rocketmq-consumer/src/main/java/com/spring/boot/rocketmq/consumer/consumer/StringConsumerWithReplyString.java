package com.spring.boot.rocketmq.consumer.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * The consumer that replying String
 */
@Service
//@RocketMQMessageListener(topic = "${demo.rocketmq.stringRequestTopic}", consumerGroup = "${demo.rocketmq.stringRequestConsumer}", selectorExpression = "${demo.rocketmq.tag}")
@RocketMQMessageListener(topic = "${demo.rocketmq.stringRequestTopic}", consumerGroup = "string_request_consumer")
public class StringConsumerWithReplyString implements RocketMQReplyListener<String, String> {

    @Override
    public String onMessage(String message) {
        System.out.printf("------- StringConsumerWithReplyString received: %s \n", message);
        return "reply string";
    }
}
