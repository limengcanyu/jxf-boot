package org.asura.disruptor;

public interface DisruptorMqService {

    /**
     * 消息
     * @param message
     */
    void publishMessage(String message);

}
