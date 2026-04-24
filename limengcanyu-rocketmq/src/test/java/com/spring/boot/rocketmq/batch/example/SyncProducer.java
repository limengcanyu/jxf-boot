package com.spring.boot.rocketmq.batch.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

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

//        // 发送不超过1M的批量消息
//        String topic = "BatchTest";
//        List<Message> messages = new ArrayList<>();
//        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
//        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
//        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));
//
//        try {
//            producer.send(messages);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //handle the error
//        }

        // 发送超过1M的批量消息
        String topic = "BatchTest";
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            messages.add(new Message(topic, "TagA", "OrderID00" + i, ("Hello world " + i).getBytes()));
        }

        //then you could split the large list into small ones:
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()) {
            try {
                List<Message>  listItem = splitter.next();
                producer.send(listItem);
            } catch (Exception e) {
                e.printStackTrace();
                //handle the error
            }
        }

        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
