package com.webstore.implementation;

import com.webstore.model.Category;
import com.webstore.repositories.CategoryRepository;
import com.webstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + " has been deleted";

    }

    @Override
    public String updateCategory(Category responseCategory) {
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> category = categories.stream()
                .filter(c -> c.getCategoryId().equals(responseCategory.getCategoryId()))
                .findFirst();

        if (category.isPresent()) {
            Category categoryToUpdate = category.get();
            categoryToUpdate.setCategoryName(responseCategory.getCategoryName());
            categoryRepository.save(categoryToUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        return "Category updated successfully";
    }
}
