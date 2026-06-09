package org.asura.akka.service;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.asura.akka.actor.DistributedCounterActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 分布式计数器服务
 * 
 * <p>提供高并发的分布式计数器操作，基于 Akka Cluster Sharding 实现。
 * 支持异步和同步两种调用方式。
 * 
 * <p>生产级特性：
 * <ul>
 *   <li>高并发支持（每秒数万次操作）</li>
 *   <li>分布式部署</li>
 *   <li>异步非阻塞 API</li>
 *   <li>批量操作优化</li>
 *   <li>支持本地模式（测试环境）和集群模式（生产环境）</li>
 * </ul>
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@Service("counterService")
@Profile("!test")
public class DistributedCounterService {

    private static final Logger log = LoggerFactory.getLogger(DistributedCounterService.class);

    private static final FiniteDuration TIMEOUT = Duration.create(10, TimeUnit.SECONDS);

    private final ActorRef counterActor;
    private final ExecutionContextExecutor executionContext;

    @Autowired(required = false)
    public DistributedCounterService(
            @Qualifier("counterShardRegion") ActorRef counterShardRegion,
            ExecutionContextExecutor executionContext) {
        this.counterActor = counterShardRegion;
        this.executionContext = executionContext;
        log.info("DistributedCounterService initialized in CLUSTER mode");
    }

    public long increment(String counterName, long delta) {
        try {
            Future<Object> future = Patterns.ask(
                counterActor, 
                new DistributedCounterActor.Increment(counterName, delta), 
                new Timeout(TIMEOUT)
            );
            DistributedCounterActor.CounterValue result = 
                (DistributedCounterActor.CounterValue) Await.result(future, TIMEOUT);
            return result.value;
        } catch (Exception e) {
            log.error("Failed to increment counter: {}", counterName, e);
            throw new RuntimeException("Failed to increment counter", e);
        }
    }

    public CompletableFuture<Long> incrementAsync(String counterName, long delta) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        
        Patterns.ask(
            counterActor,
            new DistributedCounterActor.Increment(counterName, delta),
            new Timeout(TIMEOUT)
        ).onComplete(result -> {
            if (result.isSuccess()) {
                DistributedCounterActor.CounterValue value = 
                    (DistributedCounterActor.CounterValue) result.get();
                future.complete(value.value);
            } else {
                future.completeExceptionally(result.failed().get());
            }
            return BoxedUnit.UNIT;
        }, executionContext);
        
        return future;
    }

    public long get(String counterName) {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new DistributedCounterActor.Get(counterName),
                new Timeout(TIMEOUT)
            );
            DistributedCounterActor.CounterValue result = 
                (DistributedCounterActor.CounterValue) Await.result(future, TIMEOUT);
            return result.value;
        } catch (Exception e) {
            log.error("Failed to get counter: {}", counterName, e);
            throw new RuntimeException("Failed to get counter", e);
        }
    }

    public CompletableFuture<Long> getAsync(String counterName) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        
        Patterns.ask(
            counterActor,
            new DistributedCounterActor.Get(counterName),
            new Timeout(TIMEOUT)
        ).onComplete(result -> {
            if (result.isSuccess()) {
                DistributedCounterActor.CounterValue value = 
                    (DistributedCounterActor.CounterValue) result.get();
                future.complete(value.value);
            } else {
                future.completeExceptionally(result.failed().get());
            }
            return BoxedUnit.UNIT;
        }, executionContext);
        
        return future;
    }

    public void batchIncrement(Map<String, Long> increments) {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new DistributedCounterActor.BatchIncrement(increments),
                new Timeout(TIMEOUT)
            );
            Await.result(future, TIMEOUT);
        } catch (Exception e) {
            log.error("Failed to batch increment counters", e);
            throw new RuntimeException("Failed to batch increment", e);
        }
    }

    public long reset(String counterName) {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new DistributedCounterActor.Reset(counterName),
                new Timeout(TIMEOUT)
            );
            DistributedCounterActor.CounterValue result = 
                (DistributedCounterActor.CounterValue) Await.result(future, TIMEOUT);
            return result.value;
        } catch (Exception e) {
            log.error("Failed to reset counter: {}", counterName, e);
            throw new RuntimeException("Failed to reset counter", e);
        }
    }

    public Map<String, Long> getAllCounters() {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new DistributedCounterActor.GetAll(),
                new Timeout(TIMEOUT)
            );
            DistributedCounterActor.AllCounters result = 
                (DistributedCounterActor.AllCounters) Await.result(future, TIMEOUT);
            return result.values;
        } catch (Exception e) {
            log.error("Failed to get all counters", e);
            throw new RuntimeException("Failed to get all counters", e);
        }
    }
}