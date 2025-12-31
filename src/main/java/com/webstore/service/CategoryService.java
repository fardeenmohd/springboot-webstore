package com.webstore.service;

import com.webstore.model.Category;
import com.webstore.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    String updateCategory(Category category);
}
