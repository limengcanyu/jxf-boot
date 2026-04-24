package com.spring.boot.rocketmq.order.example;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * Subscription message sample code
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 11:37
 */
public class OrderedConsumer {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("example_group_name");

        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.81.129:9876");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("OrderedTopicTest", "TagA || TagC || TagD");

        consumer.registerMessageListener(new MessageListenerOrderly() {

            final AtomicLong consumeTimes = new AtomicLong(0);

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("isAutoCommit: " + context.isAutoCommit());

                context.setAutoCommit(false);

//                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");

                for (MessageExt msg : msgs) {
                    System.out.printf(Thread.currentThread().getName() + " Receive New Messages: %s %n", new String(msg.getBody()));
                }

                this.consumeTimes.incrementAndGet();

                if ((this.consumeTimes.get() % 2) == 0) {
                    System.out.printf("ConsumeOrderlyStatus: %s %n", ConsumeOrderlyStatus.SUCCESS);
                    return ConsumeOrderlyStatus.SUCCESS;
                } else if ((this.consumeTimes.get() % 3) == 0) {
                    System.out.printf("ConsumeOrderlyStatus: %s %n", ConsumeOrderlyStatus.ROLLBACK);
                    return ConsumeOrderlyStatus.ROLLBACK;
                } else if ((this.consumeTimes.get() % 4) == 0) {
                    System.out.printf("ConsumeOrderlyStatus: %s %n", ConsumeOrderlyStatus.COMMIT);
                    return ConsumeOrderlyStatus.COMMIT;
                } else if ((this.consumeTimes.get() % 5) == 0) {
                    System.out.printf("ConsumeOrderlyStatus: %s %n", ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT);
                    context.setSuspendCurrentQueueTimeMillis(3000);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
