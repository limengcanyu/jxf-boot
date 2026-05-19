package org.asura.akka.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Akka 配置类
 * 
 * <p>该类负责配置和创建 Akka ActorSystem，将其集成到 Spring 容器中。
 * ActorSystem 是 Akka 应用的核心组件，负责管理所有 Actor 的生命周期。
 * 
 * <p>配置来源：
 * <ul>
 *   <li>application.yml 中的 akka 配置段</li>
 *   <li>默认的 reference.conf（Akka 内置配置）</li>
 * </ul>
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@Configuration
public class AkkaConfig {

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
     * <p>配置加载顺序：
     * 1. 首先加载 application.yml 中的配置
     * 2. 然后加载 Akka 默认的 reference.conf
     * 3. 用户配置会覆盖默认配置
     * 
     * @return ActorSystem 实例，命名为 "asura-akka-system"
     */
    @Bean
    public ActorSystem actorSystem() {
        // 加载配置文件（从 application.yml 和 reference.conf）
        Config config = ConfigFactory.load();
        
        // 创建 ActorSystem，指定系统名称和配置
        // 系统名称用于日志和监控识别
        return ActorSystem.create("asura-akka-system", config);
    }
}