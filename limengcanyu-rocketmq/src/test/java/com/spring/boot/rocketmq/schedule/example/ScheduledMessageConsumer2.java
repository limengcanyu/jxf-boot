package com.spring.boot.rocketmq.schedule.example;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * <p>
 * 1. Start consumer to wait for incoming subscribed messages
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 13:17
 */
public class ScheduledMessageConsumer2 {

    public static void main(String[] args) throws Exception {
        // Instantiate message consumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ExampleConsumer");

        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.81.129:9876");

        // Subscribe topics
        consumer.subscribe("TestTopic", "*");

        // Register message listener
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
                for (MessageExt message : messages) {
                    // Print approximate delay time period
                    System.out.println("Receive message[msgId=" + message.getMsgId() + "] "
                            + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // Launch consumer
        consumer.start();

        System.out.println("consumerGroup: " + consumer.getConsumerGroup() + " ScheduledMessageConsumer2 started");
    }
}
