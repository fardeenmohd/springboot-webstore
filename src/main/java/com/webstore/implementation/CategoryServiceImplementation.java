package com.webstore.implementation;

import com.webstore.model.Category;
import com.webstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {
    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        categories.remove(category);
        return "Category with categoryId: " + categoryId + " has been deleted";

    }

    @Override
    public String updateCategory(Category responseCategory) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(responseCategory.getCategoryId()))
                .findFirst().
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        int index = categories.indexOf(category);

        categories.set(index, responseCategory);
        return "Category updated successfully";
    }
}
