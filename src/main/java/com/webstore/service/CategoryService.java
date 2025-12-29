package com.webstore.service;

import com.webstore.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    String updateCategory(Category category);
}
