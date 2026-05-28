
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.service.impl.RedisVectorStoreServiceImpl.VectorDocument;

import java.util.List;

/**
 * Redis向量存储服务接口
 * 提供向量文档的增删查功能
 */
public interface RedisVectorStoreService {
    
    /**
     * 添加文档到向量存储
     * 
     * @param documents 文档列表
     */
    void addDocuments(List<VectorDocument> documents);
    
    /**
     * 删除指定ID的文档
     * 
     * @param ids 文档ID列表
     */
    void deleteDocuments(List<String> ids);
    
    /**
     * 搜索相似文档
     * 
     * @param query 查询文本
     * @param topK 返回数量
     * @return 相似文档列表
     */
    List<VectorDocument> search(String query, int topK);
    
    /**
     * 清空所有文档
     */
    void clear();
}