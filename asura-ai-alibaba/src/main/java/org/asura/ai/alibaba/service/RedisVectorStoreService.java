
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.service.impl.RedisVectorStoreServiceImpl.VectorDocument;

import java.util.List;

public interface RedisVectorStoreService {
    
    void addDocuments(List<VectorDocument> documents);
    
    void deleteDocuments(List<String> ids);
    
    List<VectorDocument> search(String query, int topK);
    
    void clear();
}