
package org.asura.ai.alibaba.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.ChatRequest;
import org.asura.ai.alibaba.dto.response.ChatResponse;
import org.asura.ai.alibaba.service.ChatService;
import org.asura.ai.alibaba.service.MemoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MemoryService memoryService;

    /**
     * 基础对话接口
     * 
     * @param request 对话请求对象，包含用户消息和模型参数
     * @return 返回AI生成的对话响应
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 流式对话接口（GET方式）
     * 
     * @param message 用户消息内容
     * @return 返回AI生成的文本响应内容
     */
    @GetMapping("/stream")
    public ResponseEntity<String> chatStream(@RequestParam String message) {
        log.info("Received stream chat request: {}", message);
        ChatRequest request = ChatRequest.builder().message(message).build();
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response.getContent());
    }

    /**
     * 带对话记忆的对话接口（POST方式）
     * 
     * @param conversationId 会话ID，用于关联对话历史
     * @param request 对话请求对象，包含用户消息
     * @return 返回带有上下文理解的AI响应
     */
    @PostMapping("/memory/{conversationId}")
    public ResponseEntity<ChatResponse> chatWithMemory(
            @PathVariable String conversationId,
            @Valid @RequestBody ChatRequest request) {
        log.info("Received chat request with memory: conversationId={}, message={}", conversationId, request.getMessage());
        ChatResponse response = ((org.asura.ai.alibaba.service.impl.ChatServiceImpl) chatService)
                .chatWithMemory(request, conversationId, true);
        return ResponseEntity.ok(response);
    }

    /**
     * 带对话记忆的对话接口（GET方式）
     * 
     * @param conversationId 会话ID，用于关联对话历史
     * @param message 用户消息内容
     * @return 返回带有上下文理解的AI响应
     */
    @GetMapping("/memory/{conversationId}")
    public ResponseEntity<ChatResponse> chatWithMemoryGet(
            @PathVariable String conversationId,
            @RequestParam String message) {
        log.info("Received chat request with memory (GET): conversationId={}, message={}", conversationId, message);
        ChatRequest request = ChatRequest.builder().message(message).build();
        ChatResponse response = ((org.asura.ai.alibaba.service.impl.ChatServiceImpl) chatService)
                .chatWithMemory(request, conversationId, true);
        return ResponseEntity.ok(response);
    }

    /**
     * 清除指定会话的对话记忆
     * 
     * @param conversationId 会话ID
     * @return 返回空响应，成功状态码
     */
    @DeleteMapping("/memory/{conversationId}")
    public ResponseEntity<Void> clearMemory(@PathVariable String conversationId) {
        log.info("Clearing memory for conversationId={}", conversationId);
        memoryService.delete(conversationId);
        return ResponseEntity.ok().build();
    }
}