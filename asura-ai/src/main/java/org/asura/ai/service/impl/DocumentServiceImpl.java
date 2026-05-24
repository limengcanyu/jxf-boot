package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.entity.Document;
import org.asura.ai.mapper.DocumentMapper;
import org.asura.ai.service.DocumentProcessingService;
import org.asura.ai.service.DocumentService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 文档服务实现类
 * 提供文档的CRUD操作
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    private DocumentProcessingService documentProcessingService;

    /**
     * 上传文档
     * 创建文档记录并处理文件内容存储到向量数据库
     * @param file 上传的文件
     * @return 文档实体
     */
    @Override
    @Transactional
    public Document uploadDocument(MultipartFile file) {
        Document document = new Document();
        document.setId(UUID.randomUUID().toString());
        document.setFilename(UUID.randomUUID().toString() + "-" + file.getOriginalFilename());
        document.setOriginalFilename(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        documentMapper.insert(document);

        documentProcessingService.processAndStore(file);

        return document;
    }

    /**
     * 根据ID获取文档
     * @param id 文档ID
     * @return 文档实体，如果不存在返回null
     */
    @Override
    public Document getDocumentById(String id) {
        return documentMapper.selectById(id);
    }

    /**
     * 获取所有文档
     * @return 文档列表
     */
    @Override
    public List<Document> getAllDocuments() {
        return documentMapper.selectList(null);
    }

    /**
     * 删除文档
     * @param id 文档ID
     */
    @Override
    @Transactional
    public void deleteDocument(String id) {
        documentMapper.deleteById(id);
    }

    /**
     * 根据关键词搜索文档
     * @param keyword 关键词
     * @return 匹配的文档列表
     */
    @Override
    public List<Document> searchDocuments(String keyword) {
        return sqlSessionTemplate.selectList("org.asura.ai.agent.mapper.DocumentMapper.searchByKeyword", keyword);
    }
}