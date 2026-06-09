
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

    /**
     * 智能代理基础对话接口
     * 
     * @param request 对话请求对象，包含用户消息和会话ID
     * @return 返回AI代理生成的对话响应
     */
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

    /**
     * 带工具调用的智能代理对话接口
     * 
     * @param request 对话请求对象，包含用户消息和会话ID
     * @return 返回AI代理生成的对话响应（可能包含工具调用结果）
     */
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

    /**
     * 获取智能代理服务信息
     * 
     * @return 返回代理的名称、版本、功能描述和可用工具列表
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAgentInfo() {
        return ResponseEntity.ok(agentService.getAgentInfo());
    }

    /**
     * 智能代理对话接口（GET方式）
     * 
     * @param conversationId 会话ID
     * @param message 用户消息内容
     * @return 返回AI代理生成的对话响应
     */
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