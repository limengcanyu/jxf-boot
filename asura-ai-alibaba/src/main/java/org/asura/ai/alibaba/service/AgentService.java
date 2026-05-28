
package org.asura.ai.alibaba.service;

import java.util.Map;

/**
 * 智能代理服务接口
 * 提供基于AI的智能代理功能，支持工具调用
 */
public interface AgentService {
    
    /**
     * 基础对话
     * 
     * @param message 用户消息
     * @param conversationId 会话ID
     * @return AI响应内容
     */
    String chat(String message, String conversationId);
    
    /**
     * 带工具调用的对话
     * 
     * @param message 用户消息
     * @param conversationId 会话ID
     * @return AI响应内容（可能包含工具调用结果）
     */
    String chatWithTools(String message, String conversationId);
    
    /**
     * 获取代理服务信息
     * 
     * @return 代理信息，包含名称、版本、功能描述等
     */
    Map<String, Object> getAgentInfo();
}