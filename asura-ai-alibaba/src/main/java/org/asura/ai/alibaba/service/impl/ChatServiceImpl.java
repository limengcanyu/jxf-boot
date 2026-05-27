
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

    @Override
    public ChatResponse chat(ChatRequest request) {
        String conversationId = UUID.randomUUID().toString();
        return chatWithMemory(request, conversationId, true);
    }

    @Override
    public ChatResponse chatWithHistory(ChatRequest request, java.util.List<?> history) {
        String conversationId = UUID.randomUUID().toString();
        history.forEach(h -> memoryService.save(conversationId, "user", h.toString()));
        return chatWithMemory(request, conversationId, true);
    }

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