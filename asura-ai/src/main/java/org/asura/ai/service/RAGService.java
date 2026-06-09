package org.asura.ai.service;

/**
 * RAG（Retrieval-Augmented Generation）服务接口
 * 提供基于知识库的智能问答功能
 */
public interface RAGService {

    /**
     * 回答用户问题（无上下文）
     * @param question 用户问题
     * @return 回答内容
     */
    String ask(String question);

    /**
     * 回答用户问题（带上下文）
     * @param question 用户问题
     * @param conversationId 对话ID
     * @return 回答内容
     */
    String askWithContext(String question, String conversationId);

    /**
     * 清除对话历史
     * @param conversationId 对话ID
     */
    void clearConversation(String conversationId);
}