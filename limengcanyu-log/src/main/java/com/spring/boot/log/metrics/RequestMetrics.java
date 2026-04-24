package com.spring.boot.log.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求指标收集器，提供标准化监控数据
 */
@Component
public class RequestMetrics {
    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeRequests = new AtomicInteger(0);
    private final Map<String, Counter> exceptionCounters = new ConcurrentHashMap<>();

    public RequestMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // 注册活跃请求数指标
        Gauge.builder("http.request.active", activeRequests, AtomicInteger::get)
                .description("当前活跃请求数量")
                .register(meterRegistry);
    }

    /**
     * 记录请求指标
     */
    public void recordRequest(String requestKey, double costMs, boolean hasException) {
        // 记录耗时指标（自动计算平均值、分位数）
        Timer.builder("http.request.duration")
                .tag("request", requestKey)
                .description("请求处理耗时(ms)")
                .register(meterRegistry)
                .record(Duration.ofSeconds((long) costMs));

        // 记录异常指标
        if (hasException) {
            exceptionCounters.computeIfAbsent(requestKey, k ->
                    Counter.builder("http.request.exception")
                            .tag("request", k)
                            .description("请求异常次数")
                            .register(meterRegistry)
            ).increment();
        }
    }

    public void incrementActiveRequests() {
        activeRequests.incrementAndGet();
    }

    public void decrementActiveRequests() {
        activeRequests.decrementAndGet();
    }
}

