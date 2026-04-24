package com.spring.boot.rocketmq.transaction.example;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * (2) Implement the TransactionListener interface
 * The “executeLocalTransaction” method is used to execute local transaction when send half message succeed.
 * It returns one of three transaction status mentioned in the previous section.
 *
 * The “checkLocalTransaction” method is used to check the local transaction status and respond to MQ check requests.
 * It also returns one of three transaction status mentioned in the previous section.
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 14:13
 */
public class TransactionListenerImpl implements TransactionListener {
    private final AtomicInteger transactionIndex = new AtomicInteger(0);

    private final ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        System.out.printf("执行本地事务 开始 localTrans: %s%n", localTrans);

//        int value = transactionIndex.getAndIncrement(); // 获取事务索引
//        int status = value % 3;
//        localTrans.put(msg.getTransactionId(), status); // 设置事务ID的状态

//        System.out.printf("执行本地事务 Thread: [%s] TransactionId: %s transactionIndex: %d Status: %s Message: %s%n",
//                Thread.currentThread().getId(), msg.getTransactionId(), value, status, new String(msg.getBody()));

//        System.out.printf("执行本地事务 结束 localTrans: %s%n", localTrans);

        System.out.printf("      # UNKNOW # Simulating %s related local transaction exec UNKNOWN! \n", new String(msg.getBody()));
        return LocalTransactionState.UNKNOW;

//        // Return local transaction with success(commit), in this case,
//        // this message will not be checked in checkLocalTransaction()
//        System.out.printf("    # COMMIT # Simulating msg %s related local transaction exec succeeded! ### %n", new String(msg.getBody()));
//        return LocalTransactionState.COMMIT_MESSAGE;

//        // Return local transaction with failure(rollback) , in this case,
//        // this message will not be checked in checkLocalTransaction()
//        System.out.printf("    # ROLLBACK # Simulating %s related local transaction exec failed! %n", new String(msg.getBody()));
//        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//        System.out.printf("检查本地事务 开始 localTrans: %s%n", localTrans);

        Integer status = localTrans.get(msg.getTransactionId()); // 获取事务ID的状态
//        System.out.printf("检查本地事务 Thread: [%s] TransactionId: %s Status: %s MessageExt: %s%n%n",
//                Thread.currentThread().getId(), msg.getTransactionId(), status, new String(msg.getBody()));

//        if (null != status) {
//            switch (status) {
//                case 0:
//                    System.out.printf("检查本地事务 UNKNOW Thread: [%s] TransactionId: %s Status: %s MessageExt: %s%n%n",
//                            Thread.currentThread().getId(), msg.getTransactionId(), status, new String(msg.getBody()));
//
//                    return LocalTransactionState.UNKNOW;
//                case 1:
//                    System.out.printf("检查本地事务 COMMIT_MESSAGE Thread: [%s] TransactionId: %s Status: %s MessageExt: %s%n%n",
//                            Thread.currentThread().getId(), msg.getTransactionId(), status, new String(msg.getBody()));
//
//                    return LocalTransactionState.COMMIT_MESSAGE;
//                case 2:
//                    System.out.printf("检查本地事务 ROLLBACK_MESSAGE Thread: [%s] TransactionId: %s Status: %s MessageExt: %s%n%n",
//                            Thread.currentThread().getId(), msg.getTransactionId(), status, new String(msg.getBody()));
//
//                    return LocalTransactionState.ROLLBACK_MESSAGE;
//            }
//        }

//        System.out.printf("检查本地事务 结束 localTrans: %s%n", localTrans);

        // executeLocalTransaction方法返回COMMIT_MESSAGE时，checkLocalTransaction无论返回什么都是成功发送消息
        // executeLocalTransaction方法返回UNKNOW或者ROLLBACK_MESSAGE时，checkLocalTransaction无论返回什么都是失败

//        return LocalTransactionState.COMMIT_MESSAGE;
//        return LocalTransactionState.UNKNOW;
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
