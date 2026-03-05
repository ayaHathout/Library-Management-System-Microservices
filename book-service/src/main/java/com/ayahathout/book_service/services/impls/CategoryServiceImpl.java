package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.CategoryDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;
import com.ayahathout.book_service.exceptions.BadRequestException;
import com.ayahathout.book_service.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.CategoryMapper;
import com.ayahathout.book_service.models.Category;
import com.ayahathout.book_service.repositories.CategoryRepository;
import com.ayahathout.book_service.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAllWithSubCategories()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
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
    public CategoryResponseDTO updateCategory(Long id, CategoryDTO updateCategoryDTO) {
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
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public CategoryResponseDTO deleteCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    if (!category.getSubCategories().isEmpty()) {
                        throw new BadRequestException("Cannot delete category with ID " + id + " because it has " + category.getSubCategories().size() + " associated sub categories.");
                    }

                    CategoryResponseDTO deletedCategory = categoryMapper.toResponseDTO(category);
                    categoryRepository.delete(category);
                    return deletedCategory;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
