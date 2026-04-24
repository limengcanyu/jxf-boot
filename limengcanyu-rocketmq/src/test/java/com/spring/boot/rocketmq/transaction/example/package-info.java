/**
 * <p>
 * Transaction examplev
 *
 * What is transactional message?
 * It can be thought of as a two-phase commit message implementation to ensure eventual consistency in distributed system.
 * Transactional message ensures that the execution of local transaction and the sending of message can be performed atomically.
 *
 * Usage Constraint
 * (1) Messages of the transactional have no schedule and batch support.
 * (2) In order to avoid a single message being checked too many times and lead to half queue message accumulation，
 *     we limited the number of checks for a single message to 15 times by default, but users can change this limit by change the “transactionCheckMax” parameter in the configuration of the broker， if one message has been checked over “transactionCheckMax” times， broker will discard this message and print an error log at the same time by default. Users can change this behavior by override the “AbstractTransactionCheckListener” class.
 * (3) A transactional message will be checked after a certain period of time that determined by parameter “transactionTimeout”
 *     in the configuration of the broker. And users also can change this limit by set user property “CHECK_IMMUNITY_TIME_IN_SECONDS”
 *     when sending transactional message, this parameter takes precedence over the “transactionMsgTimeout” parameter.
 * (4) A transactional message maybe checked or consumed more than once.
 * (5) Committed message reput to the user’s target topic may fail. Currently, it depends on the log record.
 *     High availability is ensured by the high availability mechanism of RocketMQ itself.
 *     If you want to ensure that the transactional message isn’t lost and the transaction integrity is guaranteed,
 *     it is recommended to use synchronous double write. mechanism.
 * (6) Producer IDs of transactional messages cannot be shared with producer IDs of other types of messages.
 *     Unlike other types of message, transactional messages allow backward queries. MQ Server query clients by their Producer IDs.
 *
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 14:10
 */
package com.spring.boot.rocketmq.transaction.example;