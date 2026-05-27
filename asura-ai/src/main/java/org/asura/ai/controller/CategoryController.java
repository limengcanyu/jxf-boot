package org.asura.ai.controller;

import jakarta.annotation.Resource;
import org.asura.ai.entity.DocumentCategory;
import org.asura.ai.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<DocumentCategory> createCategory(@RequestBody DocumentCategory category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @GetMapping
    public ResponseEntity<List<DocumentCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/tree")
    public ResponseEntity<List<DocumentCategory>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentCategory> getCategoryById(@PathVariable String id) {
        DocumentCategory category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentCategory> updateCategory(@PathVariable String id, @RequestBody DocumentCategory category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}