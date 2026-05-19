package org.asura.akka.service;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.asura.akka.actor.TestCounterMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 测试专用的计数器服务
 * 
 * <p>完全独立于生产代码，使用测试专用的消息类型和本地 Actor。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
@Service("counterService")
@Profile("test")
public class TestCounterService {

    private static final Logger log = LoggerFactory.getLogger(TestCounterService.class);
    private static final FiniteDuration TIMEOUT = Duration.create(10, TimeUnit.SECONDS);

    private final ActorRef counterActor;
    private final ExecutionContextExecutor executionContext;

    public TestCounterService(
            @Qualifier("counterActor") ActorRef counterActor,
            ExecutionContextExecutor executionContext) {
        this.counterActor = counterActor;
        this.executionContext = executionContext;
        log.info("TestCounterService initialized");
    }

    public long increment(String counterName, long delta) {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new TestCounterMessages.Increment(counterName, delta),
                new Timeout(TIMEOUT)
            );
            TestCounterMessages.CounterValue result =
                (TestCounterMessages.CounterValue) Await.result(future, TIMEOUT);
            return result.value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to increment counter", e);
        }
    }

    public CompletableFuture<Long> incrementAsync(String counterName, long delta) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        Patterns.ask(
            counterActor,
            new TestCounterMessages.Increment(counterName, delta),
            new Timeout(TIMEOUT)
        ).onComplete(result -> {
            if (result.isSuccess()) {
                TestCounterMessages.CounterValue value =
                    (TestCounterMessages.CounterValue) result.get();
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
                new TestCounterMessages.Get(counterName),
                new Timeout(TIMEOUT)
            );
            TestCounterMessages.CounterValue result =
                (TestCounterMessages.CounterValue) Await.result(future, TIMEOUT);
            return result.value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get counter", e);
        }
    }

    public CompletableFuture<Long> getAsync(String counterName) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        Patterns.ask(
            counterActor,
            new TestCounterMessages.Get(counterName),
            new Timeout(TIMEOUT)
        ).onComplete(result -> {
            if (result.isSuccess()) {
                TestCounterMessages.CounterValue value =
                    (TestCounterMessages.CounterValue) result.get();
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
                new TestCounterMessages.BatchIncrement(increments),
                new Timeout(TIMEOUT)
            );
            Await.result(future, TIMEOUT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to batch increment", e);
        }
    }

    public long reset(String counterName) {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new TestCounterMessages.Reset(counterName),
                new Timeout(TIMEOUT)
            );
            TestCounterMessages.CounterValue result =
                (TestCounterMessages.CounterValue) Await.result(future, TIMEOUT);
            return result.value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset counter", e);
        }
    }

    public Map<String, Long> getAllCounters() {
        try {
            Future<Object> future = Patterns.ask(
                counterActor,
                new TestCounterMessages.GetAll(),
                new Timeout(TIMEOUT)
            );
            TestCounterMessages.AllCounters result =
                (TestCounterMessages.AllCounters) Await.result(future, TIMEOUT);
            return result.values;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all counters", e);
        }
    }
}