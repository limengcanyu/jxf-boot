package com.spring.boot.rocketmq.transaction.example;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * <p>
 * 1、 Transactional status
 *
 * There are three states for transactional message:
 * (1) TransactionStatus.CommitTransaction: commit transaction，it means that allow consumers to consume this message.
 * (2) TransactionStatus.RollbackTransaction: rollback transaction，it means that the message will be deleted and not allowed to consume.
 * (3) TransactionStatus.Unknown: intermediate state，it means that MQ is needed to check back to determine the status.
 *
 * 2、Send transactional message
 *
 * (1) Create the transactional producer
 * Use TransactionMQProducer class to create producer client, and specify a unique producerGroup, and you can set up a custom thread pool
 * to process check requests. After executing the local transaction, you need to reply to MQ according to the execution result，
 * and the reply status is described in the above section.
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 14:12
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {

        TransactionMQProducer producer = new TransactionMQProducer("please_rename_unique_group_name");

        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.81.129:9876");

        // 定义线程池
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);

        TransactionListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionListener(transactionListener);

        producer.start();

        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};

        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.sendMessageInTransaction(msg, null);

                System.out.printf("SendResult: %s%n%n", sendResult);

//                Thread.sleep(10);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

//        for (int i = 0; i < 100000; i++) {
//            Thread.sleep(1000);
//        }

        producer.shutdown();
    }
}
