package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.CategoryCreateDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;
import com.ayahathout.book_service.dtos.CategoryUpdateDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(Long id);
    CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO);
    CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO);
    CategoryResponseDTO deleteCategory(Long id);
}
