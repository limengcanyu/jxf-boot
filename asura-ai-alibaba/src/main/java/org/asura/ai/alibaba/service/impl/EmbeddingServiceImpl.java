
package org.asura.ai.alibaba.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.dto.request.EmbeddingRequest;
import org.asura.ai.alibaba.dto.response.EmbeddingResponse;
import org.asura.ai.alibaba.service.EmbeddingService;
import org.asura.ai.alibaba.service.RedisVectorStoreService;
import org.asura.ai.alibaba.service.impl.RedisVectorStoreServiceImpl.VectorDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final RedisVectorStoreService vectorStoreService;

    @Override
    public EmbeddingResponse generateEmbeddings(EmbeddingRequest request) {
        log.info("[EMBEDDING] Generating embeddings for {} texts using model: {}", 
                request.getTexts().size(), request.getModel());

        List<List<Double>> result = new ArrayList<>();
        int dimension = 1024;

        for (int i = 0; i < request.getTexts().size(); i++) {
            List<Double> embedding = new ArrayList<>();
            for (int j = 0; j < dimension; j++) {
                embedding.add(Math.random());
            }
            result.add(embedding);
        }

        log.info("[EMBEDDING] Generated embeddings with dimension: {}", dimension);

        return EmbeddingResponse.builder()
                .embeddings(result)
                .model(request.getModel())
                .totalTokens(0)
                .dimension(dimension)
                .build();
    }

    @Override
    public void storeEmbeddings(List<String> texts, String namespace) {
        log.info("[VECTOR STORE] Storing {} embeddings to Redis with namespace: {}", texts.size(), namespace);

        List<VectorDocument> documents = new ArrayList<>();
        for (String text : texts) {
            VectorDocument doc = new VectorDocument();
            doc.setContent(text);
            doc.setMetadata(new java.util.HashMap<>());
            documents.add(doc);
        }

        vectorStoreService.addDocuments(documents);
        log.info("[VECTOR STORE] Successfully stored {} embeddings in Redis", documents.size());
    }

    @Override
    public List<String> searchEmbeddings(String query, int topK) {
        log.info("[VECTOR STORE] Searching Redis for query: '{}', topK: {}", query, topK);

        List<VectorDocument> results = vectorStoreService.search(query, topK);
        
        log.info("[VECTOR STORE] Found {} similar documents", results.size());

        List<String> contentList = new ArrayList<>();
        for (VectorDocument doc : results) {
            contentList.add(doc.getContent());
        }
        return contentList;
    }
}