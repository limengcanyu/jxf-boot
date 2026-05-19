package org.asura.caffeine.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.asura.caffeine.config.CaffeineCacheConfig;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产级缓存监控接口
 * 核心指标：命中率(核心，要求≥90%)、总请求数、淘汰数、加载耗时、命中数/未命中数
 * 可接入Prometheus/Grafana，也可用于生产告警
 */
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class CacheMonitorController {

    private final CacheManager cacheManager;

    @GetMapping("/cache/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> statsMap = new HashMap<>(8);
        // 获取用户缓存统计
        statsMap.putAll(getSingleCacheStats(CaffeineCacheConfig.CACHE_USER));
        // 获取商品缓存统计
        statsMap.putAll(getSingleCacheStats(CaffeineCacheConfig.CACHE_GOODS));
        // 获取订单缓存统计
        statsMap.putAll(getSingleCacheStats(CaffeineCacheConfig.CACHE_ORDER));
        return ResponseEntity.ok(statsMap);
    }

    /**
     * 获取单个缓存空间的核心统计指标
     */
    private Map<String, Object> getSingleCacheStats(String cacheName) {
        Map<String, Object> singleStats = new HashMap<>(6);
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (caffeineCache == null) {
            return singleStats;
        }
        CacheStats stats = caffeineCache.getNativeCache().stats();
        singleStats.put(cacheName + "-命中率(核心)", String.format("%.2f%%", stats.hitRate() * 100));
        singleStats.put(cacheName + "-总请求数", stats.requestCount());
        singleStats.put(cacheName + "-命中数", stats.hitCount());
        singleStats.put(cacheName + "-未命中数", stats.missCount());
        singleStats.put(cacheName + "-缓存淘汰数", stats.evictionCount());
        singleStats.put(cacheName + "-平均加载耗时(ms)", stats.averageLoadPenalty() / 1000000);
        return singleStats;
    }

}
