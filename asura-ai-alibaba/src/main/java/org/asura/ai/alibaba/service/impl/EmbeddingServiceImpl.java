
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

    /**
     * 生成文本向量嵌入实现
     * 生成模拟的随机向量作为占位符
     * 
     * @param request 嵌入请求
     * @return 嵌入响应，包含1024维的随机向量
     */
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

    /**
     * 存储文本向量到Redis实现
     * 将文本转换为VectorDocument后调用向量存储服务
     * 
     * @param texts 文本列表
     * @param namespace 命名空间
     */
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

    /**
     * 向量相似度搜索实现
     * 调用向量存储服务搜索并提取文档内容
     * 
     * @param query 查询文本
     * @param topK 返回数量
     * @return 匹配的文本内容列表
     */
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