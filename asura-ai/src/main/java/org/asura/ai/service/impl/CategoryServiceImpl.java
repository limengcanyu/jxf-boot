package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.entity.DocumentCategory;
import org.asura.ai.mapper.DocumentCategoryMapper;
import org.asura.ai.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文档分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private DocumentCategoryMapper categoryMapper;

    @Override
    @Transactional
    public DocumentCategory createCategory(DocumentCategory category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.insert(category);
        logger.info("创建分类: {}", category.getName());
        return category;
    }

    @Override
    public List<DocumentCategory> getAllCategories() {
        return categoryMapper.selectList(null);
    }

    @Override
    public List<DocumentCategory> getCategoryTree() {
        List<DocumentCategory> allCategories = categoryMapper.selectList(null);
        
        Map<String, List<DocumentCategory>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != null && !c.getParentId().isEmpty())
                .collect(Collectors.groupingBy(DocumentCategory::getParentId));

        List<DocumentCategory> rootCategories = allCategories.stream()
                .filter(c -> c.getParentId() == null || c.getParentId().isEmpty())
                .collect(Collectors.toList());

        buildTree(rootCategories, childrenMap);
        return rootCategories;
    }

    private void buildTree(List<DocumentCategory> categories, Map<String, List<DocumentCategory>> childrenMap) {
        for (DocumentCategory category : categories) {
            List<DocumentCategory> children = childrenMap.getOrDefault(category.getId(), new ArrayList<>());
            children.sort((a, b) -> {
                if (a.getSortOrder() == null && b.getSortOrder() == null) return 0;
                if (a.getSortOrder() == null) return 1;
                if (b.getSortOrder() == null) return -1;
                return a.getSortOrder().compareTo(b.getSortOrder());
            });
            buildTree(children, childrenMap);
        }
    }

    @Override
    public DocumentCategory getCategoryById(String id) {
        return categoryMapper.selectById(id);
    }

    @Override
    @Transactional
    public DocumentCategory updateCategory(String id, DocumentCategory category) {
        DocumentCategory existing = categoryMapper.selectById(id);
        if (existing != null) {
            existing.setName(category.getName());
            existing.setParentId(category.getParentId());
            existing.setDescription(category.getDescription());
            existing.setSortOrder(category.getSortOrder());
            existing.setUpdatedAt(LocalDateTime.now());
            categoryMapper.updateById(existing);
            logger.info("更新分类: {}", id);
        }
        return existing;
    }

    @Override
    @Transactional
    public void deleteCategory(String id) {
        categoryMapper.deleteById(id);
        logger.info("删除分类: {}", id);
    }
}