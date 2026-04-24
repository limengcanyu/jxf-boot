package com.spring.boot.rocketmq.transaction.example;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 3. Consume Messages
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 11:10
 */
public class Consumer2 {

    public static void main(String[] args) throws InterruptedException, MQClientException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");

        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.81.129:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe("TopicTest", "*");

        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);

                if (!CollectionUtils.isEmpty(msgs)) {
                    for (MessageExt msg : msgs) {
                        System.out.printf("Receive New Messages: %s %n", new String(msg.getBody()));
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("consumerGroup: %s Consumer Started.%n", consumer.getConsumerGroup());
    }
}
