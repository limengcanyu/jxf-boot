package org.asura.ai.service;

import org.asura.ai.entity.DocumentCategory;

import java.util.List;

/**
 * 文档分类服务接口
 */
public interface CategoryService {

    DocumentCategory createCategory(DocumentCategory category);

    List<DocumentCategory> getAllCategories();

    List<DocumentCategory> getCategoryTree();

    DocumentCategory getCategoryById(String id);

    DocumentCategory updateCategory(String id, DocumentCategory category);

    void deleteCategory(String id);
}