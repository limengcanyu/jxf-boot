package org.asura.ai.controller;

import jakarta.annotation.Resource;
import org.asura.ai.service.RAGService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * RAG（Retrieval-Augmented Generation）问答控制器
 * 提供基于知识库的智能问答功能
 */
@RestController
@RequestMapping("/api/rag")
public class RAGController {

    @Resource
    private RAGService ragService;

    /**
     * 提问接口
     * 根据知识库内容回答用户问题，支持上下文对话
     *
     * POST <a href="http://localhost:8080/api/rag/ask">...</a>
     * 请求体: {"question": "问题内容", "conversationId": "对话ID(可选)"}
     *
     * @param request 请求体，包含question和可选的conversationId
     * @return 包含answer和可选conversationId的响应
     */
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String conversationId = request.get("conversationId");
        String answer;
        if (conversationId != null && !conversationId.isEmpty()) {
            answer = ragService.askWithContext(question, conversationId);
        } else {
            answer = ragService.ask(question);
        }
        if (conversationId != null && !conversationId.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "answer", answer,
                    "conversationId", conversationId
            ));
        } else {
            return ResponseEntity.ok(Map.of("answer", answer));
        }
    }

    /**
     * 清除对话历史
     * 删除指定对话ID的历史记录
     * @param conversationId 对话ID
     * @return 204 No Content
     */
    @DeleteMapping("/conversation/{conversationId}")
    public ResponseEntity<Void> clearConversation(@PathVariable String conversationId) {
        ragService.clearConversation(conversationId);
        return ResponseEntity.noContent().build();
    }
}