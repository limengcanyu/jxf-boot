
package org.asura.ai.alibaba.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.service.RedisVectorStoreService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisVectorStoreServiceImpl implements RedisVectorStoreService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String DOC_PREFIX = "asura:vector:doc:";
    private static final String VEC_PREFIX = "asura:vector:vec:";
    private static final String IDX_PREFIX = "asura:vector:idx:";

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VectorDocument {
        private String id;
        private String content;
        private Map<String, Object> metadata;
    }

    @Override
    public void addDocuments(List<VectorDocument> documents) {
        log.info("[VECTOR STORE] Adding {} documents", documents.size());

        for (VectorDocument doc : documents) {
            String docId = doc.getId() != null ? doc.getId() : UUID.randomUUID().toString();
            doc.setId(docId);

            try {
                String docJson = objectMapper.writeValueAsString(doc);
                redisTemplate.opsForValue().set(DOC_PREFIX + docId, docJson);
                redisTemplate.opsForSet().add(IDX_PREFIX + "all", docId);

                log.debug("[VECTOR STORE] Added document: {}", docId);
            } catch (JsonProcessingException e) {
                log.error("[VECTOR STORE] Failed to store document: {}", e.getMessage());
            }
        }
        log.info("[VECTOR STORE] Successfully added {} documents", documents.size());
    }

    @Override
    public void deleteDocuments(List<String> ids) {
        log.info("[VECTOR STORE] Deleting {} documents", ids.size());

        for (String id : ids) {
            redisTemplate.delete(DOC_PREFIX + id);
            redisTemplate.delete(VEC_PREFIX + id);
            redisTemplate.opsForSet().remove(IDX_PREFIX + "all", id);
        }
        log.info("[VECTOR STORE] Successfully deleted {} documents", ids.size());
    }

    @Override
    public List<VectorDocument> search(String query, int topK) {
        log.info("[VECTOR STORE] Searching for: '{}', topK: {}", query, topK);

        Set<String> docIds = redisTemplate.opsForSet().members(IDX_PREFIX + "all");
        if (docIds == null || docIds.isEmpty()) {
            log.debug("[VECTOR STORE] No documents in store");
            return List.of();
        }

        List<VectorDocument> results = new ArrayList<>();
        int count = Math.min(topK, docIds.size());
        int idx = 0;
        for (String docId : docIds) {
            if (idx++ >= count) break;
            String docJson = redisTemplate.opsForValue().get(DOC_PREFIX + docId);
            if (docJson != null) {
                try {
                    VectorDocument doc = objectMapper.readValue(docJson, VectorDocument.class);
                    results.add(doc);
                } catch (JsonProcessingException e) {
                    log.error("[VECTOR STORE] Failed to deserialize document: {}", e.getMessage());
                }
            }
        }

        log.info("[VECTOR STORE] Found {} results", results.size());
        return results;
    }

    @Override
    public void clear() {
        log.info("[VECTOR STORE] Clearing all documents");
        Set<String> docIds = redisTemplate.opsForSet().members(IDX_PREFIX + "all");
        if (docIds != null) {
            for (String id : docIds) {
                redisTemplate.delete(DOC_PREFIX + id);
                redisTemplate.delete(VEC_PREFIX + id);
            }
            redisTemplate.delete(IDX_PREFIX + "all");
        }
        log.info("[VECTOR STORE] All documents cleared");
    }
}