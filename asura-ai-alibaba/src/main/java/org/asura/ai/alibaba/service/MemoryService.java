
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.entity.ConversationMemory;

import java.util.List;

public interface MemoryService {
    void save(String conversationId, String role, String content);
    List<ConversationMemory> getHistory(String conversationId, int limit);
    void delete(String conversationId);
}