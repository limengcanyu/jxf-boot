package org.asura.akka.config;

import akka.actor.ActorSystem;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion;
import org.asura.akka.actor.DistributedCounterActor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Akka Cluster Sharding 配置类
 * 
 * <p>配置集群分片，实现分布式数据管理和负载均衡。
 * Cluster Sharding 自动将 Actor 分布到集群中的各个节点。
 * 
 * <p>生产级特性：
 * <ul>
 *   <li>自动分片和负载均衡</li>
 *   <li>故障自动转移</li>
 *   <li>分布式状态管理</li>
 * </ul>
 * 
 * <p>生产环境使用（非 test profile）。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@Configuration
@Profile("!test")
public class ClusterShardingConfig {

    /**
     * 创建分布式计数器的 ShardRegion
     * 
     * <p>ShardRegion 是集群分片的入口点，负责路由消息到正确的分片。
     * 
     * @param system ActorSystem 实例
     * @return ShardRegion 引用
     */
    @Bean
    public akka.actor.ActorRef counterShardRegion(ActorSystem system) {
        // 创建分片设置
        ClusterShardingSettings settings = ClusterShardingSettings.create(system);

        // 定义实体提供者
        ClusterSharding clusterSharding = ClusterSharding.get(system);

        // 使用 start 方法创建分片区域
        return clusterSharding.start(
            "distributed-counter",                              // 实体类型名称
            DistributedCounterActor.props("default"),            // Props
            settings,                                           // 分片设置
            DistributedCounterActor.messageExtractor()          // 消息提取器
        );
    }
}