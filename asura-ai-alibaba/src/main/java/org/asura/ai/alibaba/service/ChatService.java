
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.ChatRequest;
import org.asura.ai.alibaba.dto.response.ChatResponse;

import java.util.List;

/**
 * 对话服务接口
 * 提供AI对话功能，支持带历史记录的对话
 */
public interface ChatService {

    /**
     * 基础对话
     * 
     * @param request 对话请求
     * @return 对话响应
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 带历史记录的对话
     * 
     * @param request 对话请求
     * @param history 历史消息列表
     * @return 对话响应
     */
    ChatResponse chatWithHistory(ChatRequest request, List<?> history);
}