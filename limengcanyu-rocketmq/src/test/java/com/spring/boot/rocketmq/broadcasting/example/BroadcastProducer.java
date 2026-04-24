package com.spring.boot.rocketmq.broadcasting.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * <p>
 * Producer example
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 12:17
 */
public class BroadcastProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");

        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.81.129:9876");

        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TopicTest", "TagA", "OrderID188",
                    ("Hello world" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            SendResult sendResult = producer.send(msg);

            System.out.printf("%s%n", sendResult);
        }

        producer.shutdown();
    }
}
