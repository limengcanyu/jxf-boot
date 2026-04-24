package com.spring.boot.disruptor;

import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@Service
public class DisruptorMqServiceImpl implements DisruptorMqService {

    @Autowired
    private RingBuffer<MessageEvent> ringBuffer;

    @Override
    public void publishMessage(String message) {
        log.info("record the message: {}",message);

        // 获取下一个可用位置的下标
        long sequence = ringBuffer.next();

        try {
            // 返回可用位置的元素
            MessageEvent event = ringBuffer.get(sequence);
            // 设置该位置元素的值
            event.setMessage(message);

            log.info("往消息队列中添加消息：{}", event);
        } catch (Exception e) {
            log.error("failed to add event to messageModelRingBuffer for : e = {},{}",e,e.getMessage());
        } finally {
            // 发布Event，激活观察者去消费，将sequence传递给消费者
            // 注意最后的publish方法必须放在finally中以确保必须得到调用；如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            ringBuffer.publish(sequence);
        }
    }

}
