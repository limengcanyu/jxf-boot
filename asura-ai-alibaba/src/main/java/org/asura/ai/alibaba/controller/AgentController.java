
package org.asura.ai.alibaba.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.ChatRequest;
import org.asura.ai.alibaba.dto.response.ChatResponse;
import org.asura.ai.alibaba.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/ai/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String conversationId = request.getConversationId();
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }
        
        log.info("[AGENT API] Chat request: conversationId={}, message={}", conversationId, request.getMessage());
        
        String response = agentService.chat(request.getMessage(), conversationId);
        
        return ResponseEntity.ok(ChatResponse.builder()
                .content(response)
                .model(request.getModel())
                .conversationId(conversationId)
                .build());
    }

    @PostMapping("/chat/tools")
    public ResponseEntity<ChatResponse> chatWithTools(@RequestBody ChatRequest request) {
        String conversationId = request.getConversationId();
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }
        
        log.info("[AGENT API] Tool-enabled chat request: conversationId={}, message={}", conversationId, request.getMessage());
        
        String response = agentService.chatWithTools(request.getMessage(), conversationId);
        
        return ResponseEntity.ok(ChatResponse.builder()
                .content(response)
                .model(request.getModel())
                .conversationId(conversationId)
                .build());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAgentInfo() {
        return ResponseEntity.ok(agentService.getAgentInfo());
    }

    @GetMapping("/chat/{conversationId}")
    public ResponseEntity<ChatResponse> chatGet(
            @PathVariable String conversationId,
            @RequestParam String message) {
        log.info("[AGENT API] GET chat: conversationId={}, message={}", conversationId, message);
        
        String response = agentService.chat(message, conversationId);
        
        return ResponseEntity.ok(ChatResponse.builder()
                .content(response)
                .conversationId(conversationId)
                .build());
    }
}