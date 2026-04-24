package com.spring.boot.rocketmq.simple.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * <p>
 * 2.3 Send Messages in One-way Mode
 * One-way transmission is used for cases requiring moderate reliability, such as log collection.
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 11:09
 */
public class OnewayProducer {
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
            producer.sendOneway(msg);
        }

        //Wait for sending to complete
        Thread.sleep(5000);

        producer.shutdown();
    }
}
