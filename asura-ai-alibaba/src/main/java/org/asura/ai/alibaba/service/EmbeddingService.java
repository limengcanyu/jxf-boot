
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.EmbeddingRequest;
import org.asura.ai.alibaba.dto.response.EmbeddingResponse;

import java.util.List;

/**
 * 向量嵌入服务接口
 * 提供文本向量生成、存储和相似度搜索功能
 */
public interface EmbeddingService {

    /**
     * 生成文本向量嵌入
     * 
     * @param request 嵌入请求
     * @return 嵌入响应
     */
    EmbeddingResponse generateEmbeddings(EmbeddingRequest request);

    /**
     * 存储文本向量到向量数据库
     * 
     * @param texts 文本列表
     * @param namespace 命名空间
     */
    void storeEmbeddings(List<String> texts, String namespace);

    /**
     * 向量相似度搜索
     * 
     * @param query 查询文本
     * @param topK 返回数量
     * @return 匹配的文本列表
     */
    List<String> searchEmbeddings(String query, int topK);
}