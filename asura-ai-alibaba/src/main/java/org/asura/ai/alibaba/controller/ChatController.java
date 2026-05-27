
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

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request: {}", request.getMessage());
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stream")
    public ResponseEntity<String> chatStream(@RequestParam String message) {
        log.info("Received stream chat request: {}", message);
        ChatRequest request = ChatRequest.builder().message(message).build();
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response.getContent());
    }

    @PostMapping("/memory/{conversationId}")
    public ResponseEntity<ChatResponse> chatWithMemory(
            @PathVariable String conversationId,
            @Valid @RequestBody ChatRequest request) {
        log.info("Received chat request with memory: conversationId={}, message={}", conversationId, request.getMessage());
        ChatResponse response = ((org.asura.ai.alibaba.service.impl.ChatServiceImpl) chatService)
                .chatWithMemory(request, conversationId, true);
        return ResponseEntity.ok(response);
    }

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

    @DeleteMapping("/memory/{conversationId}")
    public ResponseEntity<Void> clearMemory(@PathVariable String conversationId) {
        log.info("Clearing memory for conversationId={}", conversationId);
        memoryService.delete(conversationId);
        return ResponseEntity.ok().build();
    }
}