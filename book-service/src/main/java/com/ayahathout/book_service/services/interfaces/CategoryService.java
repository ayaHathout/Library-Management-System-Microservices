package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.CategoryDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    Optional<CategoryResponseDTO> getCategoryById(Long id);
    CategoryResponseDTO createCategory(CategoryDTO categoryDTO);
    Optional<CategoryResponseDTO> updateCategory(Long id, CategoryDTO categoryDTO);
    Optional<CategoryResponseDTO> deleteCategory(Long id);
}
