
package org.asura.ai.alibaba.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.ChatRequest;
import org.asura.ai.alibaba.dto.response.ChatResponse;
import org.asura.ai.alibaba.entity.ConversationMemory;
import org.asura.ai.alibaba.service.ChatService;
import org.asura.ai.alibaba.service.MemoryService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final MemoryService memoryService;

    private static final int DEFAULT_HISTORY_LIMIT = 10;

    /**
     * 基础对话实现
     * 创建新会话并调用带记忆的对话方法
     * 
     * @param request 对话请求
     * @return 对话响应
     */
    @Override
    public ChatResponse chat(ChatRequest request) {
        String conversationId = UUID.randomUUID().toString();
        return chatWithMemory(request, conversationId, true);
    }

    /**
     * 带历史记录的对话实现
     * 将传入的历史记录保存到记忆服务后调用带记忆的对话方法
     * 
     * @param request 对话请求
     * @param history 历史消息列表
     * @return 对话响应
     */
    @Override
    public ChatResponse chatWithHistory(ChatRequest request, java.util.List<?> history) {
        String conversationId = UUID.randomUUID().toString();
        history.forEach(h -> memoryService.save(conversationId, "user", h.toString()));
        return chatWithMemory(request, conversationId, true);
    }

    /**
     * 带对话记忆的核心对话方法
     * 从记忆服务获取历史记录，构建带上下文的prompt，调用AI模型生成响应
     * 
     * @param request 对话请求
     * @param conversationId 会话ID
     * @param saveMemory 是否保存本次对话到记忆
     * @return 对话响应
     */
    public ChatResponse chatWithMemory(ChatRequest request, String conversationId, boolean saveMemory) {
        log.info("Processing chat request with memory: conversationId={}, message={}", conversationId, request.getMessage());

        List<ConversationMemory> history = memoryService.getHistory(conversationId, DEFAULT_HISTORY_LIMIT);

        StringBuilder historyBuilder = new StringBuilder();
        for (ConversationMemory memory : history) {
            historyBuilder.append(memory.getRole()).append(": ").append(memory.getContent()).append("\n");
        }

        String template = """
                你是一个乐于助人的AI助手。
                请根据以下对话历史理解上下文并回答问题。

                对话历史:
                {history}

                用户问题:
                {question}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "history", historyBuilder.toString(),
                "question", request.getMessage()
        );
        Prompt prompt = promptTemplate.create(params);

        String content = chatClientBuilder.build().prompt(prompt).call().content();

        if (saveMemory) {
            memoryService.save(conversationId, "user", request.getMessage());
            memoryService.save(conversationId, "assistant", content);
        }

        return ChatResponse.builder()
                .content(content)
                .model(request.getModel())
                .build();
    }
}