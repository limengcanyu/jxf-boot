package com.spring.boot.rocketmq.schedule.example;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * <p>
 * 2. Send scheduled messages
 *
 * 3. Verification
 * You should see messages are consumed about 10 seconds later than their storing time.
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 13:17
 */
public class ScheduledMessageProducer {

    public static void main(String[] args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");

        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.81.129:9876");

        // Launch producer
        producer.start();

        int totalMessagesToSend = 10;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes());

            // This message will be delivered to consumer 10 seconds later.
            message.setDelayTimeLevel(3);

            // Send the message
            producer.send(message);
        }

        // Shutdown producer after use.
        producer.shutdown();

        System.out.println("producerGroup: " + producer.getProducerGroup() + " ScheduledMessageProducer started");
    }

}
