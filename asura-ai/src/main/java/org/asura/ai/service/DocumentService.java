package org.asura.ai.service;

import org.asura.ai.common.PageResponse;
import org.asura.ai.dto.AdvancedSearchRequest;
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
     * 上传文档（带分类）
     * @param file 上传的文件
     * @param categoryId 分类ID
     * @return 文档实体
     */
    Document uploadDocument(MultipartFile file, String categoryId);

    /**
     * 批量上传文档
     * @param files 上传的文件列表
     * @param categoryId 分类ID
     * @return 文档列表
     */
    List<Document> batchUploadDocuments(List<MultipartFile> files, String categoryId);

    /**
     * 根据ID获取文档
     * @param id 文档ID
     * @return 文档实体，如果不存在返回null
     */
    Document getDocumentById(String id);

    /**
     * 分页获取文档列表
     * @param page 页码
     * @param size 每页数量
     * @param categoryId 分类ID（可选）
     * @return 分页文档列表
     */
    PageResponse<Document> getDocuments(int page, int size, String categoryId);

    /**
     * 获取所有文档
     * @return 文档列表
     */
    List<Document> getAllDocuments();

    /**
     * 更新文档
     * @param id 文档ID
     * @param document 更新的文档信息
     * @return 更新后的文档
     */
    Document updateDocument(String id, Document document);

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

    /**
     * 根据关键词搜索文档（分页）
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 分页匹配的文档列表
     */
    PageResponse<Document> searchDocuments(String keyword, int page, int size);

    /**
     * 高级搜索文档
     * @param request 高级搜索请求参数
     * @return 匹配的文档列表
     */
    List<Document> advancedSearch(AdvancedSearchRequest request);
}