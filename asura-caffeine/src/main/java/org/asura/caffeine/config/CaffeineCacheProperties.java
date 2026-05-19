package org.asura.caffeine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

/**
 * 读取yml中caffeine配置的属性类
 * 配置前缀：caffeine.cache
 */
@Component
@ConfigurationProperties(prefix = "caffeine.cache")
@Data
public class CaffeineCacheProperties {
    private Map<String, CaffeineCacheProperties.CacheConfig> configs;
    private Map<String, CacheRule> caches;

    @Data
    public static class CacheConfig {
        private Long maximumSize;        // 最大容量
        private Integer expireAfterWrite;// 写入后过期(分钟)
        private Integer expireAfterAccess;// 访问后过期(分钟)
        private Boolean recordStats = false;// 是否开启统计
    }

    @Data
    public static class CacheRule {
        private Integer maximumSize;
        private String expireAfterWrite;
        private String expireAfterAccess;
        private String refreshAfterWrite;
        private Boolean recordStats = false;

        // 解析时间字符串为Duration（支持s/m/h/d）
        public Duration getExpireAfterWriteDuration() {
            return parseDuration(expireAfterWrite);
        }

        public Duration getExpireAfterAccessDuration() {
            return parseDuration(expireAfterAccess);
        }

        public Duration getRefreshAfterWriteDuration() {
            return parseDuration(refreshAfterWrite);
        }

        private Duration parseDuration(String durationStr) {
            if (durationStr == null || durationStr.isBlank()) {
                return null;
            }
            char unit = durationStr.charAt(durationStr.length() - 1);
            long value = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));
            return switch (unit) {
                case 's' -> Duration.ofSeconds(value);
                case 'm' -> Duration.ofMinutes(value);
                case 'h' -> Duration.ofHours(value);
                case 'd' -> Duration.ofDays(value);
                default -> throw new IllegalArgumentException("不支持的时间单位：" + unit);
            };
        }
    }
}
