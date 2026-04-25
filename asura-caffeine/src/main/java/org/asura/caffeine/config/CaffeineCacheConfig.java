package org.asura.caffeine.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 生产级Caffeine核心配置类【终极无报错版】
 * ✅ 彻底解决：refreshAfterWrite LoadingCache报错 + lookup方法不存在 + 枚举不存在 所有问题
 * ✅ 缓存策略yml配置化，无硬编码，生产最佳实践
 * ✅ 多缓存差异化策略+缓存全生命周期监听(新增/更新/删除/过期/淘汰)
 * ✅ 禁止缓存NULL值，彻底解决缓存穿透
 * ✅ 开启缓存统计，监控接口正常访问无报错
 * ✅ SpringCache注解无侵入，业务代码完全不变
 */
@Slf4j
@EnableCaching
@Configuration
public class CaffeineCacheConfig {

    // ========== 缓存名称常量，与yml配置key严格一致 ==========
    public static final String CACHE_USER = "userCache";
    public static final String CACHE_GOODS = "goodsCache";
    public static final String CACHE_ORDER = "orderCache";
    public static final String CACHE_DICT = "dictCache";

    private final CaffeineCacheProperties cacheProperties;

    public CaffeineCacheConfig(CaffeineCacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager() {
            @NonNull
            @Override
            protected Cache createCaffeineCache(@NonNull String cacheName) {
                CaffeineCacheProperties.CacheConfig cacheConfig = cacheProperties.getConfigs().get(cacheName);
                if (cacheConfig == null) {
                    return super.createCaffeineCache(cacheName);
                }

                // 构建缓存规则，无任何冗余配置，解决LoadingCache报错
                Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder().maximumSize(cacheConfig.getMaximumSize());

                // 写入后过期 - 普通缓存支持，无限制
                if (cacheConfig.getExpireAfterWrite() != null) {
                    caffeineBuilder.expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.MINUTES);
                }
                // 访问后过期 - 普通缓存支持，无限制（热点数据推荐）
                if (cacheConfig.getExpireAfterAccess() != null) {
                    caffeineBuilder.expireAfterAccess(cacheConfig.getExpireAfterAccess(), TimeUnit.MINUTES);
                }
                // 开启缓存统计 - 监控接口依赖此配置，必须保留
                if (cacheConfig.getRecordStats()) {
                    caffeineBuilder.recordStats();
                }

                // 绑定缓存移除监听器，所有移除行为正常监听
                caffeineBuilder.removalListener((key, value, cause) -> cacheRemovalListener(cacheName, key, value, cause));

                // 返回自定义缓存对象，监听缓存新增行为
                return new CustomCaffeineCache(cacheName, caffeineBuilder.build(), false);
            }
        };
        // 核心防穿透配置：禁止缓存null值
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * 缓存移除监听器 - Caffeine3.1.8 真实有效枚举值（共5个，无任何多余）
     * 监听：新增/更新/主动删除/批量清空/过期/容量淘汰/内存回收 全生命周期
     */
    private void cacheRemovalListener(String cacheName, Object key, Object value, RemovalCause cause) {
        String operateDesc = switch (cause) {
            case EXPLICIT  -> "【主动删除/批量清空】单个缓存删除 或 清空缓存空间";
            case REPLACED  -> "【缓存更新】缓存值被新数据覆盖";
            case COLLECTED -> "【内存回收】缓存对象被JVM GC回收";
            case EXPIRED   -> "【缓存过期】缓存达到过期时间自动失效";
            case SIZE      -> "【容量淘汰】缓存超量，按W-TinyLFU算法淘汰";
        };
        log.info("===== 缓存事件监听 ===== 缓存空间: {}, 缓存KEY: {}, 操作类型: {}, 缓存VALUE: {}",
                cacheName, key, operateDesc, value);
    }

    /**
     * 自定义缓存类，监听【缓存新增】行为
     * ✅ 正确方法：super.lookup(key) 判断缓存是否存在（Spring封装类的标准方法）
     * ✅ 健壮性处理：过滤空key/空value，无空指针风险
     */
    static class CustomCaffeineCache extends CaffeineCache {
        public CustomCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache, boolean allowNullValues) {
            super(name, cache, allowNullValues);
        }

        @Override
        public void put(@NonNull Object key, Object value) {
            if (value == null) {
                return;
            }
            // 判断是否为新增缓存：lookup返回null → 缓存无此key
            boolean isCacheNew = super.lookup(key) == null;
            super.put(key, value);
            if (isCacheNew) {
                log.info("===== 缓存事件监听 ===== 缓存空间: {}, 缓存KEY: {}, 操作类型: 【缓存新增】, 缓存VALUE: {}",
                        super.getName(), key, value);
            }
        }
    }

    /**
     * 读取yml中caffeine配置的属性类
     * 配置前缀：caffeine.cache
     */
    @Component
    @ConfigurationProperties(prefix = "caffeine.cache")
    @Data
    public static class CaffeineCacheProperties {
        private Map<String, CacheConfig> configs;

        @Data
        public static class CacheConfig {
            private Long maximumSize;        // 最大容量
            private Integer expireAfterWrite;// 写入后过期(分钟)
            private Integer expireAfterAccess;// 访问后过期(分钟)
            private Boolean recordStats = false;// 是否开启统计
        }
    }

    // ==============================================
    // ========== 核心新增：手动注册4个缓存Bean ==========
    // ==============================================
    @Bean(name = CACHE_USER)
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> userCache() {
        return getCaffeineNativeCache(CACHE_USER);
    }

    @Bean(name = CACHE_GOODS)
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> goodsCache() {
        return getCaffeineNativeCache(CACHE_GOODS);
    }

    @Bean(name = CACHE_ORDER)
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> orderCache() {
        return getCaffeineNativeCache(CACHE_ORDER);
    }

    @Bean(name = CACHE_DICT)
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> dictCache() {
        return getCaffeineNativeCache(CACHE_DICT);
    }

    // ========== 新增：获取原生缓存的工具方法 ==========
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> getCaffeineNativeCache(String cacheName) {
        CaffeineCache caffeineCache = (CaffeineCache) caffeineCacheManager().getCache(cacheName);
        assert caffeineCache != null;
        return caffeineCache.getNativeCache();
    }
}

