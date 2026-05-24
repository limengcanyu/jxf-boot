package org.asura.ai.config;

import jakarta.annotation.Resource;
import org.asura.ai.entity.DocumentChunk;
import org.asura.ai.mapper.DocumentChunkMapper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 向量存储初始化器
 * 在应用启动时将数据库中的文档块加载到向量存储中，以便进行相似度检索
 */
@Component
public class VectorStoreInitializer implements ApplicationRunner {

    @Resource
    private DocumentChunkMapper documentChunkMapper;

    @Resource
    private VectorStore vectorStore;

    /**
     * 应用启动时执行的初始化逻辑
     * 从数据库读取所有文档块，转换为向量存储格式并加载
     * @param args 应用启动参数
     * @throws Exception 初始化过程中的异常
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DocumentChunk> chunks = documentChunkMapper.selectList(null);
        
        if (chunks.isEmpty()) {
            return;
        }

        List<Document> documents = new ArrayList<>();
        for (DocumentChunk chunk : chunks) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("documentId", chunk.getDocumentId());
            metadata.put("chunkIndex", chunk.getChunkIndex());
            
            Document doc = new Document(chunk.getContent(), metadata);
            documents.add(doc);
        }

        vectorStore.add(documents);
    }
}