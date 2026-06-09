package org.asura.ai.controller;

import jakarta.annotation.Resource;
import org.asura.ai.common.PageResponse;
import org.asura.ai.dto.AdvancedSearchRequest;
import org.asura.ai.entity.Document;
import org.asura.ai.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文档管理控制器
 * 提供文档的上传、查询、删除和搜索功能
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    /**
     * 上传文档
     * @param file 要上传的文件
     * @param categoryId 分类ID（可选）
     * @return 上传的文档信息
     */
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "categoryId", required = false) String categoryId) {
        Document document = documentService.uploadDocument(file, categoryId);
        return ResponseEntity.ok(document);
    }

    /**
     * 批量上传文档
     * @param files 要上传的文件列表
     * @param categoryId 分类ID（可选）
     * @return 上传的文档列表
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<List<Document>> batchUploadDocuments(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "categoryId", required = false) String categoryId) {
        List<Document> documents = documentService.batchUploadDocuments(files, categoryId);
        return ResponseEntity.ok(documents);
    }

    /**
     * 分页获取文档列表
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @param categoryId 分类ID（可选）
     * @return 分页文档列表
     */
    @GetMapping
    public ResponseEntity<PageResponse<Document>> getDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String categoryId) {
        return ResponseEntity.ok(documentService.getDocuments(page, size, categoryId));
    }

    /**
     * 获取所有文档列表
     * @return 文档列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    /**
     * 根据ID获取文档详情
     * @param id 文档ID
     * @return 文档详情，如果不存在返回404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable String id) {
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);
    }

    /**
     * 更新文档信息
     * @param id 文档ID
     * @param document 更新的文档信息
     * @return 更新后的文档
     */
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable String id, @RequestBody Document document) {
        Document updated = documentService.updateDocument(id, document);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * 根据ID删除文档
     * @param id 文档ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据关键词搜索文档（分页）
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页数量
     * @return 匹配的文档列表
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<Document>> searchDocuments(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentService.searchDocuments(keyword, page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Document>> advancedSearch(@RequestBody AdvancedSearchRequest request) {
        List<Document> documents = documentService.advancedSearch(request);
        return ResponseEntity.ok(documents);
    }

}