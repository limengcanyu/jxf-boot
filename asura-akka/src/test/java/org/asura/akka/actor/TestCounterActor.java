package org.asura.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试专用的本地计数器 Actor
 * 
 * <p>简化版计数器，不依赖集群分片，用于测试环境。
 * 使用测试专用的消息类型，避免依赖生产代码中的集群相关类。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
public class TestCounterActor extends AbstractActor {

    private static final Logger log = LoggerFactory.getLogger(TestCounterActor.class);

    private final Map<String, Long> counters = new HashMap<>();

    public static Props props() {
        return Props.create(TestCounterActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TestCounterMessages.Increment.class, this::handleIncrement)
                .match(TestCounterMessages.Get.class, this::handleGet)
                .match(TestCounterMessages.BatchIncrement.class, this::handleBatchIncrement)
                .match(TestCounterMessages.GetAll.class, this::handleGetAll)
                .match(TestCounterMessages.Reset.class, this::handleReset)
                .matchAny(message -> log.warn("Received unknown message: {}", message))
                .build();
    }

    private void handleIncrement(TestCounterMessages.Increment msg) {
        synchronized (counters) {
            counters.merge(msg.counterName, msg.delta, Long::sum);
        }
        sender().tell(new TestCounterMessages.CounterValue(msg.counterName, getCounter(msg.counterName)), self());
    }

    private void handleBatchIncrement(TestCounterMessages.BatchIncrement msg) {
        synchronized (counters) {
            msg.increments.forEach((name, delta) -> 
                counters.merge(name, delta, Long::sum));
        }
        sender().tell("OK", self());
    }

    private void handleGet(TestCounterMessages.Get msg) {
        long value = getCounter(msg.counterName);
        sender().tell(new TestCounterMessages.CounterValue(msg.counterName, value), self());
    }

    private void handleGetAll(TestCounterMessages.GetAll msg) {
        Map<String, Long> snapshot;
        synchronized (counters) {
            snapshot = new HashMap<>(counters);
        }
        sender().tell(new TestCounterMessages.AllCounters(snapshot), self());
    }

    private void handleReset(TestCounterMessages.Reset msg) {
        synchronized (counters) {
            counters.put(msg.counterName, 0L);
        }
        sender().tell(new TestCounterMessages.CounterValue(msg.counterName, 0), self());
    }

    private long getCounter(String name) {
        synchronized (counters) {
            return counters.getOrDefault(name, 0L);
        }
    }
}