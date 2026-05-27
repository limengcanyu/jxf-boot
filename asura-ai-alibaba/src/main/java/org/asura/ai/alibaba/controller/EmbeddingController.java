
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

    @PostMapping
    public ResponseEntity<EmbeddingResponse> generateEmbeddings(@Valid @RequestBody EmbeddingRequest request) {
        log.info("Received embedding request for {} texts", request.getTexts().size());
        EmbeddingResponse response = embeddingService.generateEmbeddings(request);
        return ResponseEntity.ok(response);
    }

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