package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.entity.ConversationMemory;
import org.asura.ai.mapper.ConversationMemoryMapper;
import org.asura.ai.service.RAGService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * RAG（Retrieval-Augmented Generation）服务实现类
 * 基于向量检索和大语言模型实现智能问答
 */
@Service
public class RAGServiceImpl implements RAGService {

    private static final Logger logger = LoggerFactory.getLogger(RAGServiceImpl.class);

    @Resource
    private VectorStore vectorStore;

    @Resource
    private ChatClient.Builder chatClientBuilder;

    @Resource
    private ConversationMemoryMapper conversationMemoryMapper;

    /** 最大对话历史条数 */
    private static final int MAX_HISTORY_SIZE = 10;

    /** RAG提示词模板（无上下文） */
    private static final String RAG_PROMPT_TEMPLATE = """
            请根据以下提供的知识库内容来回答用户的问题。

            知识库内容:
            {document_content}

            用户问题:
            {question}

            请仅根据提供的知识库内容进行回答。如果知识库中没有相关信息，请回答"根据知识库内容，我无法回答这个问题。"
            """;

    /** RAG提示词模板（带上下文） */
    private static final String RAG_CONTEXT_PROMPT_TEMPLATE = """
            请根据以下提供的知识库内容和对话历史来回答用户的问题。

            对话历史:
            {conversation_history}

            知识库内容:
            {document_content}

            用户问题:
            {question}

            请结合对话历史理解上下文，并仅根据提供的知识库内容进行回答。
            如果知识库中没有相关信息，请回答"根据知识库内容，我无法回答这个问题。"
            """;

    /**
     * 回答用户问题（无上下文）
     * 从向量存储中检索相关文档，构建提示词并调用大语言模型
     * @param question 用户问题
     * @return 回答内容
     */
    @Override
    public String ask(String question) {
        try {
            SearchRequest searchRequest = SearchRequest.builder().query(question).topK(5).build();
            List<Document> retrievedDocs = vectorStore.similaritySearch(searchRequest);

            StringBuilder documentContent = new StringBuilder();
            for (Document doc : retrievedDocs) {
                documentContent.append(doc.getText()).append("\n\n");
            }

            PromptTemplate promptTemplate = new PromptTemplate(RAG_PROMPT_TEMPLATE);
            Map<String, Object> params = Map.of(
                    "document_content", documentContent.toString(),
                    "question", question
            );
            Prompt prompt = promptTemplate.create(params);

            String answer = chatClientBuilder.build().prompt(prompt).call().content();
            logger.info("问答完成，问题: {}, 回答长度: {}", question.length() > 30 ? question.substring(0, 30) + "..." : question, answer.length());
            return answer;
        } catch (Exception e) {
            logger.error("查询失败", e);
            return "查询失败: " + e.getMessage();
        }
    }

    /**
     * 回答用户问题（带上下文）
     * 获取对话历史，构建包含上下文的提示词，调用大语言模型并保存对话记录
     * @param question 用户问题
     * @param conversationId 对话ID
     * @return 回答内容
     */
    @Override
    @Transactional
    public String askWithContext(String question, String conversationId) {
        try {
            List<ConversationMemory> history = conversationMemoryMapper.selectByConversationId(conversationId);
            history.sort(Comparator.comparing(ConversationMemory::getCreatedAt));

            StringBuilder conversationHistory = new StringBuilder();
            int startIndex = Math.max(0, history.size() - MAX_HISTORY_SIZE);
            for (int i = startIndex; i < history.size(); i++) {
                ConversationMemory memory = history.get(i);
                conversationHistory.append(memory.getRole()).append(": ").append(memory.getContent()).append("\n");
            }

            SearchRequest searchRequest = SearchRequest.builder().query(question).topK(5).build();
            List<Document> retrievedDocs = vectorStore.similaritySearch(searchRequest);

            StringBuilder documentContent = new StringBuilder();
            for (Document doc : retrievedDocs) {
                documentContent.append(doc.getText()).append("\n\n");
            }

            PromptTemplate promptTemplate = new PromptTemplate(RAG_CONTEXT_PROMPT_TEMPLATE);
            Map<String, Object> params = Map.of(
                    "conversation_history", conversationHistory.toString(),
                    "document_content", documentContent.toString(),
                    "question", question
            );
            Prompt prompt = promptTemplate.create(params);

            String answer = chatClientBuilder.build().prompt(prompt).call().content();

            saveConversationMemory(conversationId, "user", question);
            saveConversationMemory(conversationId, "assistant", answer);

            logger.info("上下文问答完成，会话ID: {}, 问题: {}", conversationId, question.length() > 30 ? question.substring(0, 30) + "..." : question);
            return answer;
        } catch (Exception e) {
            logger.error("上下文查询失败", e);
            return "查询失败: " + e.getMessage();
        }
    }

    /**
     * 保存对话记忆
     * @param conversationId 对话ID
     * @param role 角色（user/assistant）
     * @param content 内容
     */
    private void saveConversationMemory(String conversationId, String role, String content) {
        ConversationMemory memory = new ConversationMemory();
        memory.setConversationId(conversationId);
        memory.setRole(role);
        memory.setContent(content);
        memory.setCreatedAt(LocalDateTime.now());
        conversationMemoryMapper.insert(memory);
    }

    /**
     * 清除对话历史
     * 删除指定对话ID的所有历史记录
     * @param conversationId 对话ID
     */
    @Override
    @Transactional
    public void clearConversation(String conversationId) {
        conversationMemoryMapper.deleteByConversationId(conversationId);
        logger.info("清除对话历史，会话ID: {}", conversationId);
    }
}