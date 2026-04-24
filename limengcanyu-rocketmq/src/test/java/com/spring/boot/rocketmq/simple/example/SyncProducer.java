package com.spring.boot.rocketmq.simple.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * <p>
 * 2.1 Send Messages Synchronously
 * Reliable synchronous transmission is used in extensive scenes,
 * such as important notification messages, SMS notification, SMS marketing system, etc..
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 11:04
 */
public class SyncProducer {
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");

        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.81.129:9876");

        //Launch the instance.
        producer.start();

        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest", /* Topic */
                    "TagA", /* Tag */
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );

            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
