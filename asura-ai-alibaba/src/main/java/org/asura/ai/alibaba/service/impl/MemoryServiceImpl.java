
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

    public MemoryServiceImpl(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

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

    private void trimHistory(String conversationId) {
        List<ConversationMemory> history = memoryStore.get(conversationId);
        if (history != null && history.size() > MAX_HISTORY_SIZE) {
            int removedCount = history.size() - MAX_HISTORY_SIZE;
            memoryStore.put(conversationId, new ArrayList<>(history.subList(history.size() - MAX_HISTORY_SIZE, history.size())));
            log.debug("[{}] Trimmed history - conversationId: {}, removedCount: {}, remaining: {}", 
                storageType, conversationId, removedCount, MAX_HISTORY_SIZE);
        }
    }

    public boolean isRedisAvailable() {
        return redisAvailable;
    }

    public String getStorageType() {
        return storageType;
    }
}