
package org.asura.ai.alibaba.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.EmbeddingRequest;
import org.asura.ai.alibaba.dto.response.EmbeddingResponse;
import org.asura.ai.alibaba.service.EmbeddingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai/embedding")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    /**
     * 生成文本向量嵌入
     * 
     * @param request 嵌入请求对象，包含待转换的文本列表和模型参数
     * @return 返回向量嵌入结果，包含嵌入向量列表、维度等信息
     */
    @PostMapping
    public ResponseEntity<EmbeddingResponse> generateEmbeddings(@Valid @RequestBody EmbeddingRequest request) {
        log.info("Received embedding request for {} texts", request.getTexts().size());
        EmbeddingResponse response = embeddingService.generateEmbeddings(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 存储文本向量嵌入到向量数据库
     * 
     * @param request 请求体，包含texts字段（文本列表）和可选的namespace字段（命名空间）
     * @return 返回存储状态信息
     */
    @PostMapping("/store")
    public ResponseEntity<Map<String, String>> storeEmbeddings(
            @RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<String> texts = (List<String>) request.get("texts");
        String namespace = (String) request.getOrDefault("namespace", "default");
        
        log.info("Received store request for {} texts, namespace: {}", texts.size(), namespace);
        embeddingService.storeEmbeddings(texts, namespace);
        
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Embeddings stored successfully",
                "count", String.valueOf(texts.size()),
                "namespace", namespace
        ));
    }

    /**
     * 向量相似度搜索接口
     * 
     * @param query 查询文本
     * @param topK 返回结果数量，默认为5
     * @return 返回搜索结果，包含查询词、返回数量和匹配的文本列表
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchEmbeddings(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK) {
        log.info("Received search request: '{}', topK: {}", query, topK);
        List<String> results = embeddingService.searchEmbeddings(query, topK);
        
        return ResponseEntity.ok(Map.of(
                "query", query,
                "topK", topK,
                "count", results.size(),
                "results", results
        ));
    }
}