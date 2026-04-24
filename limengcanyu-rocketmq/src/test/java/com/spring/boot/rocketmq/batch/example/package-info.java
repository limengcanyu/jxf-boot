/**
 * <p>
 * Batch Example
 *
 * Why batch?
 * Sending messages in batch improves performance of delivering small messages.
 *
 * Usage constraints
 * Messages of the same batch should have: same topic, same waitStoreMsgOK and no schedule support.
 *
 * Besides, the total size of the messages in one batch should be no more than 1MiB.
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 13:29
 */
package com.spring.boot.rocketmq.batch.example;