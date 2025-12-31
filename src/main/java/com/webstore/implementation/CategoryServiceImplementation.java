package com.webstore.implementation;

import com.webstore.exceptions.ApiException;
import com.webstore.exceptions.ResourceNotFoundException;
import com.webstore.model.Category;
import com.webstore.payload.CategoryDTO;
import com.webstore.payload.CategoryResponse;
import com.webstore.repositories.CategoryRepository;
import com.webstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {
    @Autowired
    private ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;


    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new ApiException("No categories found");
        }

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map((category -> modelMapper.map(category, CategoryDTO.class)))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new ApiException("Category already exists");
        }

        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);

        return savedCategoryDTO;
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
