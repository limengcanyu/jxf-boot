
package org.asura.ai.service;

import org.asura.ai.entity.ConversationMemory;

import java.util.List;
import java.util.Map;

/**
 * 会话管理服务接口
 * 提供会话的创建、查询、删除等功能
 */
public interface ConversationService {

    /**
     * 创建新会话
     * @return 新会话ID
     */
    String createConversation();

    /**
     * 查询所有会话列表
     * @return 会话ID列表
     */
    List<String> listConversations();

    /**
     * 查询指定会话的历史记录
     * @param conversationId 会话ID
     * @return 对话历史列表
     */
    List<ConversationMemory> getConversationHistory(String conversationId);

    /**
     * 查询指定会话的摘要信息
     * @param conversationId 会话ID
     * @return 会话摘要（包含第一条和最后一条消息）
     */
    Map<String, Object> getConversationSummary(String conversationId);

    /**
     * 删除指定会话
     * @param conversationId 会话ID
     */
    void deleteConversation(String conversationId);

    /**
     * 删除所有会话
     */
    void deleteAllConversations();
}