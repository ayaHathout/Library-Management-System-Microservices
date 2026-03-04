package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.CategoryDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(Long id);
    CategoryResponseDTO createCategory(CategoryDTO categoryDTO);
    CategoryResponseDTO updateCategory(Long id, CategoryDTO categoryDTO);
    CategoryResponseDTO deleteCategory(Long id);
}
