package org.asura.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.asura.akka.actor.GreetingActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Akka 服务封装类
 * 
 * <p>该类作为 Spring 服务，封装了 Akka Actor 的创建、消息发送和生命周期管理。
 * 提供了与 Akka Actor 交互的高层接口，使得业务代码可以方便地使用 Actor 模型。
 * 
 * <p>核心功能：
 * <ul>
 *   <li>创建和管理 GreetingActor 实例</li>
 *   <li>封装 Actor 消息发送逻辑</li>
 *   <li>处理异步消息的超时和异常</li>
 *   <li>管理 ActorSystem 的生命周期</li>
 * </ul>
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@Service
public class AkkaService {

    /** 日志记录器 */
    private static final Logger log = LoggerFactory.getLogger(AkkaService.class);

    /** 
     * Akka Actor 系统引用
     * 通过构造函数注入，由 AkkaConfig 配置类创建
     */
    private final ActorSystem actorSystem;

    /** 
     * GreetingActor 的引用
     * 在 @PostConstruct 阶段创建
     */
    private ActorRef greetingActor;

    /**
     * 构造函数，注入 ActorSystem
     * 
     * @param actorSystem Akka Actor 系统实例
     */
    public AkkaService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    /**
     * 初始化方法，在 Spring 容器启动后执行
     * 
     * <p>创建 GreetingActor 实例，并记录其路径信息。
     * Actor 通过 actorOf 方法创建，传入 Props 和 Actor 名称。
     */
    @PostConstruct
    public void init() {
        // 使用 ActorSystem 创建 Actor，指定 Actor 名称为 "greeting-actor"
        greetingActor = actorSystem.actorOf(GreetingActor.props(), "greeting-actor");
        log.info("GreetingActor created with path: {}", greetingActor.path());
    }

    /**
     * 销毁方法，在 Spring 容器关闭前执行
     * 
     * <p>优雅地终止 ActorSystem，确保所有 Actor 都能正确关闭。
     */
    @PreDestroy
    public void destroy() {
        log.info("Shutting down Akka system");
        actorSystem.terminate();
    }

    /**
     * 发送问候消息到 GreetingActor
     * 
     * <p>使用 Akka 的 Patterns.ask 模式发送异步消息，并等待响应。
     * 该方法会阻塞等待 Actor 的响应，直到超时或收到回复。
     * 
     * @param name 问候对象的名称
     * @return 问候语字符串，格式为 "Hello, {name}!"
     */
    public String greet(String name) {
        // 设置超时时间为 5 秒
        FiniteDuration timeoutDuration = Duration.create(5, TimeUnit.SECONDS);
        Timeout timeout = new Timeout(timeoutDuration);
        
        // 使用 Patterns.ask 发送消息，返回 Future 对象
        // ask 模式用于需要等待响应的场景
        Future<Object> future = Patterns.ask(greetingActor, new GreetingActor.GreetMessage(name), timeout);
        
        try {
            // 阻塞等待 Future 完成，获取响应结果
            return (String) Await.result(future, timeoutDuration);
        } catch (Exception e) {
            // 处理超时或其他异常
            log.error("Error sending message to GreetingActor", e);
            return "Error: " + e.getMessage();
        }
    }
}