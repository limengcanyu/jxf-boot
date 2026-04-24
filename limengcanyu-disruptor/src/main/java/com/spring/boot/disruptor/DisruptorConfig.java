package com.spring.boot.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;

@Slf4j
@Configuration
public class DisruptorConfig {

    @Bean
    public RingBuffer<MessageEvent> messageModelRingBuffer() {
        // 生产者线程工厂
        ThreadFactory threadFactory = r -> new Thread(r, "disruptorThread");

        // 事件工厂
        MessageEventFactory factory = new MessageEventFactory();

        // ringBuffer大小，必须为2的N次方（能将求模运算转为位运算提高效率），否则将影响效率
        int bufferSize = 1024;

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 创建disruptor，采用单生产者模式
        Disruptor<MessageEvent> disruptor = new Disruptor<>(
                factory,
                bufferSize,
                threadFactory,
                ProducerType.SINGLE,
                strategy
        );

        // 设置事件业务处理器---消费者
        disruptor.handleEventsWith(new MessageEventHandler());

        // 启动disruptor线程
        disruptor.start();

        // 获取ringBuffer环，用于接取生产者生产的事件
        RingBuffer<MessageEvent> ringBuffer = disruptor.getRingBuffer();

        log.info("Disruptor start successfully!");
        return ringBuffer;
    }

}
