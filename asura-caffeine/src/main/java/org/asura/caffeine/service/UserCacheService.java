package org.asura.caffeine.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.asura.caffeine.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 基础用法（原生 API）
 * 不依赖 Spring Cache，直接使用 Caffeine 原生 API：
 */
@Component
public class UserCacheService {

    private Cache<Long, UserDTO> userCache;

    @PostConstruct
    public void initCache() {
        // 初始化缓存
        userCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 获取缓存（无则加载）
     */
    public UserDTO getUser(Long userId) {
        // get(key, loader)：缓存不存在时执行 loader 加载数据并放入缓存
        return userCache.get(userId, this::loadUserFromDB);
    }

    /**
     * 手动更新缓存
     */
    public void updateUser(Long userId, UserDTO user) {
        userCache.put(userId, user);
    }

    /**
     * 手动移除缓存
     */
    public void removeUser(Long userId) {
        userCache.invalidate(userId);
    }

    /**
     * 模拟从数据库加载数据
     */
    private UserDTO loadUserFromDB(Long userId) {
        System.out.println("从数据库加载用户：" + userId);
        // 实际场景替换为 DB 查询
        UserDTO user = new UserDTO();
        user.setId(userId);
        user.setUserName("用户" + userId);
        return user;
    }

}
