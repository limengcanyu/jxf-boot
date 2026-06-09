package org.asura.akka.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.asura.akka.actor.TestCounterActor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import scala.concurrent.ExecutionContextExecutor;

/**
 * 测试专用的 Akka 配置类
 * 
 * <p>使用本地模式（local provider）进行测试，避免集群网络依赖。
 * 本地模式下不使用 Cluster Sharding，直接创建单个本地 Actor。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@TestConfiguration
@Profile("test")
public class TestAkkaConfig {

    @Bean
    @Primary
    public ActorSystem actorSystem() {
        Config config = ConfigFactory.load();
        return ActorSystem.create("asura-akka-system", config);
    }

    @Bean
    @Primary
    public ExecutionContextExecutor executionContextExecutor(ActorSystem system) {
        return system.dispatcher();
    }

    /**
     * 创建本地计数器 Actor（用于测试）
     * 
     * <p>使用 TestCounterActor 替代 DistributedCounterActor，避免集群依赖。
     */
    @Bean("counterActor")
    @Primary
    public ActorRef counterActor(ActorSystem system) {
        return system.actorOf(TestCounterActor.props(), "test-counter");
    }
}