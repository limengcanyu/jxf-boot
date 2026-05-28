
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.entity.ConversationMemory;

import java.util.List;

/**
 * 对话记忆服务接口
 * 提供会话历史的存储、查询和删除功能
 */
public interface MemoryService {
    
    /**
     * 保存对话记录
     * 
     * @param conversationId 会话ID
     * @param role 角色（user/assistant）
     * @param content 内容
     */
    void save(String conversationId, String role, String content);
    
    /**
     * 获取对话历史
     * 
     * @param conversationId 会话ID
     * @param limit 返回数量限制
     * @return 对话历史列表
     */
    List<ConversationMemory> getHistory(String conversationId, int limit);
    
    /**
     * 删除会话历史
     * 
     * @param conversationId 会话ID
     */
    void delete(String conversationId);
}