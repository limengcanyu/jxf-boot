
package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.entity.ConversationMemory;
import org.asura.ai.mapper.ConversationMemoryMapper;
import org.asura.ai.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 会话管理服务实现类
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationServiceImpl.class);

    @Resource
    private ConversationMemoryMapper conversationMemoryMapper;

    @Override
    public String createConversation() {
        String conversationId = UUID.randomUUID().toString();
        logger.info("创建新会话: {}", conversationId);
        return conversationId;
    }

    @Override
    public List<String> listConversations() {
        List<ConversationMemory> allMemories = conversationMemoryMapper.selectList(null);
        Set<String> conversationIds = new LinkedHashSet<>();
        for (ConversationMemory memory : allMemories) {
            conversationIds.add(memory.getConversationId());
        }
        logger.info("查询到 {} 个会话", conversationIds.size());
        return new ArrayList<>(conversationIds);
    }

    @Override
    public List<ConversationMemory> getConversationHistory(String conversationId) {
        List<ConversationMemory> history = conversationMemoryMapper.selectByConversationId(conversationId);
        history.sort(Comparator.comparing(ConversationMemory::getCreatedAt));
        logger.info("查询会话 {} 的历史记录，共 {} 条", conversationId, history.size());
        return history;
    }

    @Override
    public Map<String, Object> getConversationSummary(String conversationId) {
        List<ConversationMemory> history = getConversationHistory(conversationId);
        Map<String, Object> summary = new HashMap<>();
        
        if (history.isEmpty()) {
            summary.put("conversationId", conversationId);
            summary.put("messageCount", 0);
            summary.put("firstMessage", null);
            summary.put("lastMessage", null);
            summary.put("firstMessageTime", null);
            summary.put("lastMessageTime", null);
        } else {
            ConversationMemory first = history.get(0);
            ConversationMemory last = history.get(history.size() - 1);
            
            summary.put("conversationId", conversationId);
            summary.put("messageCount", history.size());
            summary.put("firstMessage", first.getContent());
            summary.put("lastMessage", last.getContent());
            summary.put("firstMessageTime", first.getCreatedAt());
            summary.put("lastMessageTime", last.getCreatedAt());
        }
        
        return summary;
    }

    @Override
    @Transactional
    public void deleteConversation(String conversationId) {
        conversationMemoryMapper.deleteByConversationId(conversationId);
        logger.info("删除会话: {}", conversationId);
    }

    @Override
    @Transactional
    public void deleteAllConversations() {
        conversationMemoryMapper.delete(null);
        logger.info("删除所有会话");
    }
}