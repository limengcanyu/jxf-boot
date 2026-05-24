package org.asura.ai.service;

import org.asura.ai.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档服务接口
 * 提供文档的CRUD操作
 */
public interface DocumentService {

    /**
     * 上传文档
     * @param file 上传的文件
     * @return 文档实体
     */
    Document uploadDocument(MultipartFile file);

    /**
     * 根据ID获取文档
     * @param id 文档ID
     * @return 文档实体，如果不存在返回null
     */
    Document getDocumentById(String id);

    /**
     * 获取所有文档
     * @return 文档列表
     */
    List<Document> getAllDocuments();

    /**
     * 删除文档
     * @param id 文档ID
     */
    void deleteDocument(String id);

    /**
     * 根据关键词搜索文档
     * @param keyword 关键词
     * @return 匹配的文档列表
     */
    List<Document> searchDocuments(String keyword);
}