package org.asura.ai.controller;

import jakarta.annotation.Resource;
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
     * @return 上传的文档信息
     */
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file) {
        Document document = documentService.uploadDocument(file);
        return ResponseEntity.ok(document);
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
     * 根据关键词搜索文档
     * @param request 包含keyword字段的请求体
     * @return 匹配的文档列表
     */
    @PostMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        return ResponseEntity.ok(documentService.searchDocuments(keyword));
    }
}