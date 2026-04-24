package com.spring.boot.disruptor;

public interface DisruptorMqService {

    /**
     * 消息
     * @param message
     */
    void publishMessage(String message);

}
