package org.asura.caffeine.validator;

import lombok.extern.slf4j.Slf4j;
import org.asura.caffeine.config.CaffeineCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CacheConfigValidator implements CommandLineRunner {

    @Autowired
    private CaffeineCacheProperties caffeineCacheProperties;

    @Override
    public void run(String... args) throws Exception {
        // 先判空，再打印
        Map<String, CaffeineCacheProperties.CacheConfig> cacheConfigMap = caffeineCacheProperties.getConfigs();

        if (cacheConfigMap == null) {
            log.error("❌ cacheConfigMap 为 null，配置绑定失败！");
            return;
        }

        log.info("✅ cacheConfigMap 加载成功，大小：" + cacheConfigMap.size());
        cacheConfigMap.forEach((cacheName, config) -> {
            log.info("缓存：{} → 最大容量：{}，过期时间：{}", cacheName, config.getMaximumSize(), config.getExpireAfterWrite());
        });
    }

}
