
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.EmbeddingRequest;
import org.asura.ai.alibaba.dto.response.EmbeddingResponse;

import java.util.List;

public interface EmbeddingService {

    EmbeddingResponse generateEmbeddings(EmbeddingRequest request);

    void storeEmbeddings(List<String> texts, String namespace);

    List<String> searchEmbeddings(String query, int topK);
}