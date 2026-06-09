package org.asura.ai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 向量存储配置类
 * 提供基于内存的向量存储实现，用于开发和测试环境
 */
@Configuration
public class VectorStoreConfig {

    private static final Logger logger = LoggerFactory.getLogger(VectorStoreConfig.class);

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
     * 适用于开发和测试环境，生产环境建议使用 Redis 或专业向量数据库
     */
    public static class SimpleVectorStore implements VectorStore {

        private static final Logger logger = LoggerFactory.getLogger(SimpleVectorStore.class);

        /** 文档存储容器，使用文档ID作为键 */
        private final ConcurrentHashMap<String, Document> documents = new ConcurrentHashMap<>();

        /**
         * 添加文档到向量存储
         * @param documents 要添加的文档列表
         */
        @Override
        public void add(List<Document> documents) {
            int count = 0;
            for (Document doc : documents) {
                String id = doc.getId();
                this.documents.put(id, doc);
                count++;
            }
            logger.info("向量存储新增文档: {} 条，当前总数: {}", count, this.documents.size());
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
         * 根据过滤条件删除文档（暂未实现）
         * @param expression 过滤表达式
         */
        @Override
        public void delete(@NonNull Filter.Expression expression) {
        }

        /**
         * 执行相似度搜索
         * 根据查询问题检索相关文档，支持同义词匹配
         * @param request 搜索请求，包含查询问题和返回数量
         * @return 检索到的相关文档列表
         */
        @NonNull
        @Override
        public List<Document> similaritySearch(SearchRequest request) {
            String query = request.getQuery();
            List<Document> allDocs = new ArrayList<>(documents.values());
            int topK = Math.min(request.getTopK(), allDocs.size());
            
            logger.info("===== 向量检索开始 =====");
            logger.info("查询问题: {}", query);
            logger.info("向量存储中文档总数: {}", allDocs.size());
            logger.info("请求返回数量: {}", topK);
            
            if (allDocs.isEmpty()) {
                logger.warn("⚠️ 向量存储为空，无法检索相关文档");
                logger.info("===== 向量检索结束 =====");
                return new ArrayList<>();
            }
            
            allDocs.sort((doc1, doc2) -> {
                int score1 = countKeywordMatches(doc1.getText(), query);
                int score2 = countKeywordMatches(doc2.getText(), query);
                return Integer.compare(score2, score1);
            });
            
            List<Document> result = allDocs.subList(0, Math.max(0, topK));
            logger.info("实际返回文档数量: {}", result.size());
            
            int index = 0;
            for (Document doc : result) {
                String content = doc.getText();
                String snippet = content.length() > 100 ? content.substring(0, 100) + "..." : content;
                logger.info("检索到文档 {}: {}", index + 1, snippet);
                index++;
            }
            
            logger.info("===== 向量检索结束 =====");
            return result;
        }

        /**
         * 计算关键词匹配得分
         * 将查询字符串拆分为关键词，统计文档中匹配的关键词数量（包括同义词）
         * @param text 文档内容
         * @param query 查询字符串
         * @return 匹配得分，得分越高表示相关性越强
         */
        private int countKeywordMatches(String text, String query) {
            if (text == null || query == null) return 0;
            
            int count = 0;
            String[] queryWords = query.split("[\\s\\p{Punct}]+");
            
            for (String word : queryWords) {
                if (!word.isEmpty()) {
                    // 精确匹配
                    if (text.contains(word)) {
                        count++;
                    }
                    // 同义词匹配
                    String synonym = getSynonym(word);
                    if (synonym != null && text.contains(synonym)) {
                        count++;
                    }
                }
            }
            return count;
        }

        /**
         * 获取关键词的同义词
         * 支持AI领域常见缩写与全称的映射
         * @param word 关键词
         * @return 同义词，如果没有则返回null
         */
        private String getSynonym(String word) {
            Map<String, String> synonyms = new HashMap<>();
            synonyms.put("AI", "人工智能");
            synonyms.put("人工智能", "AI");
            synonyms.put("ML", "机器学习");
            synonyms.put("机器学习", "ML");
            synonyms.put("NLP", "自然语言处理");
            synonyms.put("自然语言处理", "NLP");
            synonyms.put("DL", "深度学习");
            synonyms.put("深度学习", "DL");
            return synonyms.get(word);
        }
    }
}