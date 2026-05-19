package org.asura.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.cluster.sharding.ShardRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 分布式计数器 Actor
 * 
 * <p>支持集群分片的分布式计数器，每个分片管理一组计数器。
 * 使用 Cluster Sharding 实现自动分片和负载均衡。
 * 
 * <p>生产级特性：
 * <ul>
 *   <li>集群分片支持</li>
 *   <li>线程安全的计数器操作</li>
 *   <li>批量操作支持</li>
 *   <li>统计信息查询</li>
 * </ul>
 * 
 * @author Asura Team
 * @since 1.0.0
 */
public class DistributedCounterActor extends AbstractActor {

    private static final Logger log = LoggerFactory.getLogger(DistributedCounterActor.class);

    /** 分片内的计数器存储 */
    private final Map<String, Long> counters = new HashMap<>();

    /** 分片 ID */
    private final String shardId;

    /**
     * 创建 Props 配置
     * 
     * @param shardId 分片 ID
     * @return Props 配置
     */
    public static Props props(String shardId) {
        return Props.create(DistributedCounterActor.class, shardId);
    }

    /**
     * 构造函数
     * 
     * @param shardId 分片 ID
     */
    public DistributedCounterActor(String shardId) {
        this.shardId = shardId;
        log.info("DistributedCounterActor created for shard: {}", shardId);
    }

    /**
     * 增量请求消息
     */
    public static class Increment {
        public final String counterName;
        public final long delta;

        public Increment(String counterName, long delta) {
            this.counterName = counterName;
            this.delta = delta;
        }
    }

    /**
     * 获取值请求消息
     */
    public static class Get {
        public final String counterName;

        public Get(String counterName) {
            this.counterName = counterName;
        }
    }

    /**
     * 批量增量请求消息
     */
    public static class BatchIncrement {
        public final Map<String, Long> increments;

        public BatchIncrement(Map<String, Long> increments) {
            this.increments = increments;
        }
    }

    /**
     * 获取所有计数器请求
     */
    public static class GetAll {
    }

    /**
     * 重置计数器请求
     */
    public static class Reset {
        public final String counterName;

        public Reset(String counterName) {
            this.counterName = counterName;
        }
    }

    /**
     * 响应消息
     */
    public static class CounterValue {
        public final String counterName;
        public final long value;

        public CounterValue(String counterName, long value) {
            this.counterName = counterName;
            this.value = value;
        }
    }

    /**
     * 所有计数器响应消息
     */
    public static class AllCounters {
        public final String shardId;
        public final Map<String, Long> values;

        public AllCounters(String shardId, Map<String, Long> values) {
            this.shardId = shardId;
            this.values = values;
        }
    }

    /**
     * 分片消息提取器
     * 
     * <p>用于 Cluster Sharding 确定消息属于哪个实体
     */
    public static ShardRegion.MessageExtractor messageExtractor() {
        return new ShardRegion.MessageExtractor() {
            @Override
            public String entityId(Object message) {
                if (message instanceof Increment) {
                    return ((Increment) message).counterName;
                } else if (message instanceof Get) {
                    return ((Get) message).counterName;
                } else if (message instanceof Reset) {
                    return ((Reset) message).counterName;
                }
                return null;
            }

            @Override
            public String shardId(Object message) {
                String entityId = entityId(message);
                if (entityId != null) {
                    return String.valueOf(entityId.hashCode() % 100);
                }
                return null;
            }

            @Override
            public Object entityMessage(Object message) {
                return message;
            }
        };
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 处理增量请求
                .match(Increment.class, this::handleIncrement)
                // 处理获取请求
                .match(Get.class, this::handleGet)
                // 处理批量增量请求
                .match(BatchIncrement.class, this::handleBatchIncrement)
                // 处理获取所有请求
                .match(GetAll.class, this::handleGetAll)
                // 处理重置请求
                .match(Reset.class, this::handleReset)
                // 处理未知消息
                .matchAny(message -> log.warn("Received unknown message: {}", message))
                .build();
    }

    /**
     * 处理增量请求
     */
    private void handleIncrement(Increment msg) {
        synchronized (counters) {
            counters.merge(msg.counterName, msg.delta, Long::sum);
            log.debug("Incremented counter '{}' by {} in shard {}", 
                msg.counterName, msg.delta, shardId);
        }
        // 发送确认响应
        sender().tell(new CounterValue(msg.counterName, getCounter(msg.counterName)), self());
    }

    /**
     * 处理批量增量请求
     */
    private void handleBatchIncrement(BatchIncrement msg) {
        synchronized (counters) {
            msg.increments.forEach((name, delta) -> 
                counters.merge(name, delta, Long::sum));
        }
        log.debug("Batch increment {} counters in shard {}", 
            msg.increments.size(), shardId);
        sender().tell("OK", self());
    }

    /**
     * 处理获取请求
     */
    private void handleGet(Get msg) {
        long value = getCounter(msg.counterName);
        sender().tell(new CounterValue(msg.counterName, value), self());
    }

    /**
     * 处理获取所有请求
     */
    private void handleGetAll(GetAll msg) {
        Map<String, Long> snapshot;
        synchronized (counters) {
            snapshot = new HashMap<>(counters);
        }
        sender().tell(new AllCounters(shardId, snapshot), self());
    }

    /**
     * 处理重置请求
     */
    private void handleReset(Reset msg) {
        synchronized (counters) {
            counters.put(msg.counterName, 0L);
        }
        log.debug("Reset counter '{}' in shard {}", msg.counterName, shardId);
        sender().tell(new CounterValue(msg.counterName, 0), self());
    }

    /**
     * 获取计数器值（线程安全）
     */
    private long getCounter(String name) {
        synchronized (counters) {
            return counters.getOrDefault(name, 0L);
        }
    }
}