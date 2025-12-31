package com.webstore.service;

import com.webstore.model.Category;
import com.webstore.payload.CategoryDTO;
import com.webstore.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    String deleteCategory(Long categoryId);

    String updateCategory(Category category);
}
