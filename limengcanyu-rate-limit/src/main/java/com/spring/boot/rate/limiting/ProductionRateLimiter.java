package com.spring.boot.rate.limiting;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * 生产级Guava限流组件（基于令牌桶算法）
 * 特性：预热限流、超时降级、监控报警、动态调整QPS
 */
@Slf4j
@Component
public class ProductionRateLimiter {

    // 限流实例（volatile保证多线程可见性）
    private volatile RateLimiter rateLimiter;

    // 基础配置（可改为配置中心动态注入，如Nacos/Apollo）
    private static final double BASE_QPS = 10.0; // 基础QPS
    private static final int WARMUP_SECONDS = 3; // 预热时间（应对流量突增）
    private static final long ACQUIRE_TIMEOUT_MS = 50; // 获取令牌超时时间

    /**
     * 初始化：预热式限流（突发流量时平滑提升至目标QPS）
     */
    @PostConstruct
    public void init() {
        // SmoothWarmingUp：预热令牌桶，适合秒杀、峰值流量场景
        this.rateLimiter = RateLimiter.create(BASE_QPS, WARMUP_SECONDS, TimeUnit.SECONDS);
        log.info("限流组件初始化完成，基础QPS：{}，预热时间：{}s", BASE_QPS, WARMUP_SECONDS);
    }

    /**
     * 尝试获取限流许可（生产核心方法）
     * @return true：允许访问；false：限流拦截
     */
    public boolean tryAcquire() {
        try {
            // 带超时获取令牌，避免线程阻塞
            return rateLimiter.tryAcquire(ACQUIRE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // 异常降级：限流组件故障时放行（避免雪崩）
            log.error("限流组件异常，降级放行", e);
            return true;
        }
    }

    /**
     * 动态调整QPS（支持配置中心推送更新）
     * @param newQps 新QPS值（需大于0）
     */
    public void updateQps(double newQps) {
        if (newQps <= 0) {
            log.error("无效QPS配置：{}，忽略更新", newQps);
            return;
        }
        rateLimiter.setRate(newQps);
        log.info("限流QPS动态更新：{} -> {}", rateLimiter.getRate(), newQps);
    }
}
