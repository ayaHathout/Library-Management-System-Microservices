package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.CategoryDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;
import com.ayahathout.book_service.exceptions.BadRequestException;
import com.ayahathout.book_service.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.CategoryMapper;
import com.ayahathout.book_service.models.Category;
import com.ayahathout.book_service.repositories.CategoryRepository;
import com.ayahathout.book_service.services.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAllWithSubCategories()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Optional<CategoryResponseDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO);
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);

        if (categoryDTO.parentId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDTO.parentId()));
            category.setParent(parent);
        }

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    public Optional<CategoryResponseDTO> updateCategory(Long id, CategoryDTO updateCategoryDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    if (updateCategoryDTO.name() != null) {
                        category.setName(updateCategoryDTO.name());
                    }
                    if (updateCategoryDTO.parentId() != null) {
                        Category parent = categoryRepository.findById(updateCategoryDTO.parentId())
                                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + updateCategoryDTO.parentId()));
                        category.setParent(parent);
                    }

                    return categoryRepository.save(category);
                })
                .map(categoryMapper::toResponseDTO);
    }

    @Override
    public Optional<CategoryResponseDTO> deleteCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    if (!category.getSubCategories().isEmpty()) {
                        throw new BadRequestException("Cannot delete category with ID " + id + " because it has " + category.getSubCategories().size() + " associated sub categories.");
                    }

                    CategoryResponseDTO deletedCategory = categoryMapper.toResponseDTO(category);
                    categoryRepository.delete(category);
                    return deletedCategory;
                });
    }
}
