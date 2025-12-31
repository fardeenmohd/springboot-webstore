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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        
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
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Category deletedCategory = category.get();
            categoryRepository.delete(deletedCategory);
            CategoryDTO deletedCategoryDTO = modelMapper.map(deletedCategory, CategoryDTO.class);

            return deletedCategoryDTO;
        } else {
            throw new ResourceNotFoundException("Category", "categoryId", categoryId);
        }
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO responseCategoryDTO) {
        Category category = modelMapper.map(responseCategoryDTO, Category.class);
        Category categoryToUpdate = categoryRepository.findById(category.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", category.getCategoryId()));

        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new ApiException("Category already exists");
        }

        categoryToUpdate.setCategoryName(category.getCategoryName());
        categoryRepository.save(categoryToUpdate);

        CategoryDTO categoryToUpdateDTO = modelMapper.map(categoryToUpdate, CategoryDTO.class);

        return categoryToUpdateDTO;
    }
}
