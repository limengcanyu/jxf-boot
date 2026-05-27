package org.asura.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.asura.ai.common.PageResponse;
import org.asura.ai.dto.AdvancedSearchRequest;
import org.asura.ai.entity.Document;
import org.asura.ai.mapper.DocumentMapper;
import org.asura.ai.service.DocumentProcessingService;
import org.asura.ai.service.DocumentService;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文档服务实现类
 * 提供文档的CRUD操作
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

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
        return uploadDocument(file, null);
    }

    /**
     * 上传文档（带分类）
     * 创建文档记录并处理文件内容存储到向量数据库
     * @param file 上传的文件
     * @param categoryId 分类ID
     * @return 文档实体
     */
    @Override
    @Transactional
    public Document uploadDocument(MultipartFile file, String categoryId) {
        Document document = new Document();
        document.setId(UUID.randomUUID().toString());
        document.setFilename(UUID.randomUUID().toString() + "-" + file.getOriginalFilename());
        document.setOriginalFilename(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setCategoryId(categoryId);
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        documentMapper.insert(document);
        documentProcessingService.processAndStore(file);

        logger.info("上传文档成功: {}", document.getOriginalFilename());
        return document;
    }

    /**
     * 批量上传文档
     * @param files 上传的文件列表
     * @param categoryId 分类ID
     * @return 文档列表
     */
    @Override
    @Transactional
    public List<Document> batchUploadDocuments(List<MultipartFile> files, String categoryId) {
        List<Document> documents = new ArrayList<>();
        for (MultipartFile file : files) {
            documents.add(uploadDocument(file, categoryId));
        }
        logger.info("批量上传完成，共 {} 个文件", files.size());
        return documents;
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
     * 分页获取文档列表
     * @param page 页码
     * @param size 每页数量
     * @param categoryId 分类ID（可选）
     * @return 分页文档列表
     */
    @Override
    public PageResponse<Document> getDocuments(int page, int size, String categoryId) {
        List<Document> allDocuments = documentMapper.selectList(null);
        
        // 根据分类筛选
        if (categoryId != null && !categoryId.isEmpty()) {
            allDocuments = allDocuments.stream()
                    .filter(doc -> categoryId.equals(doc.getCategoryId()))
                    .toList();
        }
        
        long totalElements = allDocuments.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, (int) totalElements);
        
        // 使用 ArrayList 而不是 subList，避免序列化问题
        List<Document> content = new ArrayList<>();
        if (fromIndex < totalElements) {
            for (int i = fromIndex; i < toIndex; i++) {
                content.add(allDocuments.get(i));
            }
        }

        return new PageResponse<>(content, totalElements, page, size);
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
     * 更新文档
     * @param id 文档ID
     * @param document 更新的文档信息
     * @return 更新后的文档
     */
    @Override
    @Transactional
    public Document updateDocument(String id, Document document) {
        Document existing = documentMapper.selectById(id);
        if (existing != null) {
            existing.setOriginalFilename(document.getOriginalFilename());
            existing.setFileType(document.getFileType());
            existing.setCategoryId(document.getCategoryId());
            existing.setUpdatedAt(LocalDateTime.now());
            documentMapper.updateById(existing);
            logger.info("更新文档: {}", id);
        }
        return existing;
    }

    /**
     * 删除文档
     * @param id 文档ID
     */
    @Override
    @Transactional
    public void deleteDocument(String id) {
        documentMapper.deleteById(id);
        logger.info("删除文档: {}", id);
    }

    /**
     * 根据关键词搜索文档
     * @param keyword 关键词
     * @return 匹配的文档列表
     */
    @Override
    public List<Document> searchDocuments(String keyword) {
        return sqlSessionTemplate.selectList("org.asura.ai.mapper.DocumentMapper.searchByKeyword", keyword);
    }

    /**
     * 根据关键词搜索文档（分页）
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 分页匹配的文档列表
     */
    @Override
    public PageResponse<Document> searchDocuments(String keyword, int page, int size) {
        int offset = page * size;
        
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("offset", offset);
        params.put("size", size);

        List<Document> content = sqlSessionTemplate.selectList("org.asura.ai.mapper.DocumentMapper.searchByKeywordPage", params);
        long totalElements = sqlSessionTemplate.selectOne("org.asura.ai.mapper.DocumentMapper.countByKeyword", keyword);

        return new PageResponse<>(content, totalElements, page, size);
    }

    @Override
    public List<Document> advancedSearch(AdvancedSearchRequest request) {
        LambdaQueryWrapper<Document> query = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(request.getKeyword())) {
            query.and(q -> q.like(Document::getTitle, request.getKeyword())
                    .or().like(Document::getContent, request.getKeyword()));
        }

        if (request.getCategoryId() != null) {
            query.eq(Document::getCategoryId, request.getCategoryId());
        }

        if (StringUtils.hasText(request.getAuthor())) {
            query.like(Document::getAuthor, request.getAuthor());
        }

        if (request.getStartDate() != null) {
            query.ge(Document::getCreatedAt, request.getStartDate());
        }

        if (request.getEndDate() != null) {
            query.le(Document::getCreatedAt, request.getEndDate());
        }

        query.orderByDesc(Document::getCreatedAt);

        return documentMapper.selectList(query);
    }
}