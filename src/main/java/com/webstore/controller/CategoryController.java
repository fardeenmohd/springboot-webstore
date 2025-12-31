package com.webstore.controller;

import com.webstore.model.Category;
import com.webstore.payload.CategoryDTO;
import com.webstore.payload.CategoryResponse;
import com.webstore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse categoriesResponse = categoryService.getAllCategories();
        return ResponseEntity.ok(categoriesResponse);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(savedCategoryDTO);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/admin/categories")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category) {
        String status = categoryService.updateCategory(category);
        return ResponseEntity.ok(status);
    }
}
