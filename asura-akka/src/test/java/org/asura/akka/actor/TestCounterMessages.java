package org.asura.akka.actor;

import java.util.Map;

/**
 * 测试专用的消息类型
 * 
 * <p>完全独立于生产代码，避免集群依赖。
 * 
 * @author Asura Team
 * @since 1.0.0
 */
public class TestCounterMessages {

    public static class Increment {
        public final String counterName;
        public final long delta;

        public Increment(String counterName, long delta) {
            this.counterName = counterName;
            this.delta = delta;
        }
    }

    public static class Get {
        public final String counterName;

        public Get(String counterName) {
            this.counterName = counterName;
        }
    }

    public static class BatchIncrement {
        public final Map<String, Long> increments;

        public BatchIncrement(Map<String, Long> increments) {
            this.increments = increments;
        }
    }

    public static class GetAll {
    }

    public static class Reset {
        public final String counterName;

        public Reset(String counterName) {
            this.counterName = counterName;
        }
    }

    public static class CounterValue {
        public final String counterName;
        public final long value;

        public CounterValue(String counterName, long value) {
            this.counterName = counterName;
            this.value = value;
        }
    }

    public static class AllCounters {
        public final Map<String, Long> values;

        public AllCounters(Map<String, Long> values) {
            this.values = values;
        }
    }
}