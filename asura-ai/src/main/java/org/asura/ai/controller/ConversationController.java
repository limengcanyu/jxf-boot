
package org.asura.ai.controller;

import jakarta.annotation.Resource;
import org.asura.ai.entity.ConversationMemory;
import org.asura.ai.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会话管理控制器
 * 提供会话的创建、查询、删除等REST API接口
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Resource
    private ConversationService conversationService;

    /**
     * 创建新会话
     * 
     * POST http://localhost:8080/api/conversations
     * 
     * @return 包含新会话ID的响应
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createConversation() {
        String conversationId = conversationService.createConversation();
        return ResponseEntity.ok(Map.of("conversationId", conversationId));
    }

    /**
     * 查询所有会话列表
     * 
     * GET http://localhost:8080/api/conversations
     * 
     * @return 会话ID列表
     */
    @GetMapping
    public ResponseEntity<List<String>> listConversations() {
        List<String> conversations = conversationService.listConversations();
        return ResponseEntity.ok(conversations);
    }

    /**
     * 查询指定会话的历史记录
     * 
     * GET http://localhost:8080/api/conversations/{conversationId}/history
     * 
     * @param conversationId 会话ID
     * @return 对话历史列表
     */
    @GetMapping("/{conversationId}/history")
    public ResponseEntity<List<ConversationMemory>> getConversationHistory(@PathVariable String conversationId) {
        List<ConversationMemory> history = conversationService.getConversationHistory(conversationId);
        return ResponseEntity.ok(history);
    }

    /**
     * 查询指定会话的摘要信息
     * 
     * GET http://localhost:8080/api/conversations/{conversationId}/summary
     * 
     * @param conversationId 会话ID
     * @return 会话摘要信息
     */
    @GetMapping("/{conversationId}/summary")
    public ResponseEntity<Map<String, Object>> getConversationSummary(@PathVariable String conversationId) {
        Map<String, Object> summary = conversationService.getConversationSummary(conversationId);
        return ResponseEntity.ok(summary);
    }

    /**
     * 删除指定会话
     * 
     * DELETE http://localhost:8080/api/conversations/{conversationId}
     * 
     * @param conversationId 会话ID
     * @return 204 No Content
     */
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String conversationId) {
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除所有会话
     * 
     * DELETE http://localhost:8080/api/conversations
     * 
     * @return 204 No Content
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllConversations() {
        conversationService.deleteAllConversations();
        return ResponseEntity.noContent().build();
    }
}