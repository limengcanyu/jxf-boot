package org.asura.rocketmq.consumer.consumer;

import org.asura.rocketmq.consumer.entity.ProductWithPayload;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

/**
 * The consumer that replying generic type
 */
@Service
//@RocketMQMessageListener(topic = "${demo.rocketmq.genericRequestTopic}", consumerGroup = "${demo.rocketmq.genericRequestConsumer}", selectorExpression = "${demo.rocketmq.tag}")
public class ConsumerWithReplyGeneric implements RocketMQReplyListener<String, ProductWithPayload<String>> {
    @Override
    public ProductWithPayload<String> onMessage(String message) {
        System.out.printf("------- ConsumerWithReplyGeneric received: %s \n", message);
        return new ProductWithPayload<String>("replyProductName", "product payload");
    }
}
