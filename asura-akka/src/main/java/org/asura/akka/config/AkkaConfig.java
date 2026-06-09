package org.asura.akka.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import scala.concurrent.ExecutionContextExecutor;

/**
 * Akka 配置类
 * 
 * <p>该类负责配置和创建 Akka ActorSystem，将其集成到 Spring 容器中。
 * ActorSystem 是 Akka 应用的核心组件，负责管理所有 Actor 的生命周期。
 * 
 * <p>配置来源：
 * <ul>
 *   <li>application.conf - Akka 专用配置文件</li>
 *   <li>默认的 reference.conf（Akka 内置配置）</li>
 * </ul>
 * 
 * <p>生产环境使用（非 test profile）。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@Configuration
@Profile("!test")
public class AkkaConfig {

    private static final Logger log = LoggerFactory.getLogger(AkkaConfig.class);

    /**
     * 创建 Akka ActorSystem Bean
     * 
     * <p>ActorSystem 是 Akka 的核心组件，相当于整个 Akka 应用的"操作系统"。
     * 它负责：
     * <ul>
     *   <li>管理 Actor 的创建和销毁</li>
     *   <li>提供消息调度和分发</li>
     *   <li>处理 Actor 之间的通信</li>
     *   <li>管理线程池和资源</li>
     * </ul>
     * 
     * @return ActorSystem 实例，命名为 "asura-akka-system"
     */
    @Bean
    public ActorSystem actorSystem() {
        // 加载 Akka 配置文件（application.conf）
        Config config = ConfigFactory.load();
        
        // 获取 actor provider 配置
        String provider = config.getString("akka.actor.provider");
        
        // 记录配置信息
        if ("cluster".equals(provider)) {
            log.info("Akka ActorSystem starting in CLUSTER mode");
        } else if ("local".equals(provider)) {
            log.info("Akka ActorSystem starting in LOCAL mode");
        } else {
            log.warn("Akka ActorSystem starting with unknown provider: {}", provider);
        }
        
        // 创建 ActorSystem
        return ActorSystem.create("asura-akka-system", config);
    }

    /**
     * 创建 Scala ExecutionContextExecutor Bean
     * 
     * <p>ExecutionContextExecutor 用于执行异步操作，是 Scala 并发编程的核心组件。
     * Akka 使用它来处理 Future 和异步消息处理。
     * 
     * @param system ActorSystem 实例
     * @return ExecutionContextExecutor 实例
     */
    @Bean
    public ExecutionContextExecutor executionContextExecutor(ActorSystem system) {
        return system.dispatcher();
    }
}