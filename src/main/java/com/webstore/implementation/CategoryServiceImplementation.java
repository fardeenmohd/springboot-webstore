package com.webstore.implementation;

import com.webstore.exceptions.ApiException;
import com.webstore.exceptions.ResourceNotFoundException;
import com.webstore.model.Category;
import com.webstore.repositories.CategoryRepository;
import com.webstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new ApiException("No categories found");
        }

        return categories;
    }

    @Override
    public void createCategory(Category category) {
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new ApiException("Category already exists");
        }

        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Category deleteCategory = category.get();
            categoryRepository.delete(deleteCategory);
            return "Category with categoryId: " + categoryId + " has been deleted";
        } else {
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        }
    }

    @Override
    public String updateCategory(Category responseCategory) {
        Category category = categoryRepository.findById(responseCategory.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", responseCategory.getCategoryId()));

        if (categoryRepository.findByCategoryName(responseCategory.getCategoryName()) != null) {
            throw new ApiException("Category already exists");
        }
        category.setCategoryName(responseCategory.getCategoryName());
        categoryRepository.save(category);
        return "Category with categoryId: " + category.getCategoryId() + " has been updated";
    }
}
