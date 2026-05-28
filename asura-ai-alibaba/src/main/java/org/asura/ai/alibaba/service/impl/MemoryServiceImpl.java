
package org.asura.ai.alibaba.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.entity.ConversationMemory;
import org.asura.ai.alibaba.service.MemoryService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service("memoryService")
public class MemoryServiceImpl implements MemoryService {

    private final Map<String, List<ConversationMemory>> memoryStore = new ConcurrentHashMap<>();
    private final RedisConnectionFactory redisConnectionFactory;
    private boolean redisAvailable = false;
    private String storageType = "MEMORY";

    private static final int MAX_HISTORY_SIZE = 50;

    /**
     * 构造函数
     * 
     * @param redisConnectionFactory Redis连接工厂
     */
    public MemoryServiceImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 初始化方法，检查Redis可用性
     */
    @PostConstruct
    public void init() {
        try {
            if (redisConnectionFactory != null) {
                redisConnectionFactory.getConnection().ping();
                redisAvailable = true;
                storageType = "REDIS";
                log.info("========================================");
                log.info("   Memory Storage: REDIS (Available)");
                log.info("   Redis Host: {}", redisConnectionFactory.getClass().getSimpleName());
                log.info("========================================");
            }
        } catch (Exception e) {
            redisAvailable = false;
            storageType = "MEMORY";
            log.warn("========================================");
            log.warn("   Memory Storage: IN-MEMORY (Fallback)");
            log.warn("   Reason: Redis not available - {}", e.getMessage());
            log.warn("========================================");
        }
    }

    /**
     * 保存对话记录实现
     * 将对话记录保存到内存中，并自动修剪超出限制的历史
     * 
     * @param conversationId 会话ID
     * @param role 角色（user/assistant）
     * @param content 内容
     */
    @Override
    public void save(String conversationId, String role, String content) {
        memoryStore.computeIfAbsent(conversationId, k -> new ArrayList<>()).add(
            ConversationMemory.builder()
                .conversationId(conversationId)
                .role(role)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build()
        );
        trimHistory(conversationId);
        
        if (redisAvailable) {
            log.info("[{}] Saved memory - conversationId: {}, role: {}, contentLength: {}", 
                storageType, conversationId, role, content.length());
        } else {
            log.debug("[{}] Saved memory - conversationId: {}, role: {}, contentLength: {}", 
                storageType, conversationId, role, content.length());
        }
    }

    /**
     * 获取对话历史实现
     * 返回最近指定数量的对话记录
     * 
     * @param conversationId 会话ID
     * @param limit 返回数量限制
     * @return 对话历史列表
     */
    @Override
    public List<ConversationMemory> getHistory(String conversationId, int limit) {
        List<ConversationMemory> history = memoryStore.getOrDefault(conversationId, new ArrayList<>());
        
        if (history.isEmpty()) {
            log.debug("[{}] No history found for conversationId: {}", storageType, conversationId);
            return new ArrayList<>();
        }
        
        int startIndex = Math.max(0, history.size() - limit);
        List<ConversationMemory> result = new ArrayList<>(history.subList(startIndex, history.size()));
        
        if (redisAvailable) {
            log.info("[{}] Retrieved history - conversationId: {}, count: {}, limit: {}", 
                storageType, conversationId, result.size(), limit);
        } else {
            log.debug("[{}] Retrieved history - conversationId: {}, count: {}, limit: {}", 
                storageType, conversationId, result.size(), limit);
        }
        
        return result;
    }

    /**
     * 删除会话历史实现
     * 移除指定会话的所有历史记录
     * 
     * @param conversationId 会话ID
     */
    @Override
    public void delete(String conversationId) {
        int beforeSize = memoryStore.getOrDefault(conversationId, new ArrayList<>()).size();
        memoryStore.remove(conversationId);
        
        if (redisAvailable) {
            log.info("[{}] Deleted memory - conversationId: {}, removedCount: {}", 
                storageType, conversationId, beforeSize);
        } else {
            log.debug("[{}] Deleted memory - conversationId: {}, removedCount: {}", 
                storageType, conversationId, beforeSize);
        }
    }

    /**
     * 修剪历史记录，保持最大历史数量限制
     * 
     * @param conversationId 会话ID
     */
    private void trimHistory(String conversationId) {
        List<ConversationMemory> history = memoryStore.get(conversationId);
        if (history != null && history.size() > MAX_HISTORY_SIZE) {
            int removedCount = history.size() - MAX_HISTORY_SIZE;
            memoryStore.put(conversationId, new ArrayList<>(history.subList(history.size() - MAX_HISTORY_SIZE, history.size())));
            log.debug("[{}] Trimmed history - conversationId: {}, removedCount: {}, remaining: {}", 
                storageType, conversationId, removedCount, MAX_HISTORY_SIZE);
        }
    }

    /**
     * 获取Redis可用性状态
     * 
     * @return Redis是否可用
     */
    public boolean isRedisAvailable() {
        return redisAvailable;
    }

    /**
     * 获取当前存储类型
     * 
     * @return 存储类型（MEMORY/REDIS）
     */
    public String getStorageType() {
        return storageType;
    }
}