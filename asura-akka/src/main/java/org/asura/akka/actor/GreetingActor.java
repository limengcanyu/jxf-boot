package org.asura.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 问候消息处理 Actor
 * 
 * <p>该 Actor 负责处理问候请求消息，接收一个名称参数并返回问候语。
 * 展示了 Akka Actor 的基本结构和消息处理模式。
 * 
 * <p>Actor 模型核心特点：
 * <ul>
 *   <li>状态隔离：每个 Actor 有独立的状态和消息队列</li>
 *   <li>消息驱动：通过消息传递进行通信</li>
 *   <li>异步处理：消息按顺序处理，但发送者不会阻塞</li>
 * </ul>
 * 
 * @author Asura Team
 * @since 1.0.0
 */
public class GreetingActor extends AbstractActor {

    /** 日志记录器 */
    private static final Logger log = LoggerFactory.getLogger(GreetingActor.class);

    /**
     * 创建 Actor 的 Props 配置
     * 
     * <p>Props 是 Akka 中用于配置 Actor 创建的不可变配置对象，
     * 包含了创建 Actor 所需的所有信息（如构造函数参数、部署配置等）。
     * 
     * @return Props 配置对象
     */
    public static Props props() {
        return Props.create(GreetingActor.class);
    }

    /**
     * 问候消息记录类
     * 
     * <p>使用 Java Record 定义不可变的消息对象，
     * 包含需要问候的对象名称。
     * 
     * @param name 问候对象的名称
     */
    public record GreetMessage(String name) {
    }

    /**
     * 创建消息接收器
     * 
     * <p>该方法在 Actor 创建时调用，定义了 Actor 如何处理接收到的消息。
     * 使用 receiveBuilder() 构建消息处理逻辑。
     * 
     * <p>消息匹配规则：
     * <ul>
     *   <li>GreetMessage 类型：处理问候请求</li>
     *   <li>其他类型：记录警告日志</li>
     * </ul>
     * 
     * @return Receive 对象，定义消息处理逻辑
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 处理 GreetMessage 类型的消息
                .match(GreetMessage.class, message -> {
                    // 记录接收到的问候请求
                    log.info("Received greeting request for: {}", message.name());
                    
                    // 构建问候语
                    String greeting = "Hello, " + message.name() + "!";
                    
                    // 向消息发送者回复消息
                    // sender() 获取发送者的 ActorRef
                    // tell() 发送异步消息，self() 表示发送者（即当前 Actor）
                    sender().tell(greeting, self());
                })
                // 处理未知类型的消息
                .matchAny(message -> {
                    log.warn("Received unknown message: {}", message);
                })
                .build();
    }
}