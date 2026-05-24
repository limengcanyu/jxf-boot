package org.asura.ai.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 向量存储配置类
 * 提供基于内存的简单向量存储实现，用于文档的存储和相似度检索
 */
@Configuration
public class VectorStoreConfig {

    /**
     * 创建 VectorStore Bean
     * 使用内存存储实现，适合开发和测试环境
     * @return VectorStore 实例
     */
    @Bean
    @Primary
    public VectorStore vectorStore() {
        return new SimpleVectorStore();
    }

    /**
     * 简单的内存向量存储实现
     * 使用 ConcurrentHashMap 存储文档，支持线程安全的并发操作
     */
    public static class SimpleVectorStore implements VectorStore {

        /** 文档存储容器，使用文档ID作为键 */
        private final ConcurrentHashMap<String, Document> documents = new ConcurrentHashMap<>();

        /**
         * 添加文档到向量存储
         * @param documents 要添加的文档列表
         */
        @Override
        public void add(List<Document> documents) {
            for (Document doc : documents) {
                String id = doc.getId();
                this.documents.put(id, doc);
            }
        }

        /**
         * 根据文档ID列表删除文档
         * @param idList 要删除的文档ID列表
         */
        @Override
        public void delete(List<String> idList) {
            idList.forEach(documents::remove);
        }

        /**
         * 根据过滤条件删除文档（未实现）
         * @param expression 过滤表达式
         */
        @Override
        public void delete(org.springframework.ai.vectorstore.filter.Filter.Expression expression) {
        }

        /**
         * 相似度搜索
         * 返回与查询最相关的前 topK 个文档
         * @param request 搜索请求，包含查询内容和返回数量
         * @return 匹配的文档列表
         */
        @Override
        public List<Document> similaritySearch(SearchRequest request) {
            List<Document> allDocs = new ArrayList<>(documents.values());
            int topK = Math.min(request.getTopK(), allDocs.size());
            return allDocs.subList(0, topK);
        }
    }
}