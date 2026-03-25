package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.CategoryCreateDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;
import com.ayahathout.book_service.dtos.CategoryUpdateDTO;
import com.ayahathout.book_service.exceptions.BadRequestException;
import com.ayahathout.book_service.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.CategoryMapper;
import com.ayahathout.book_service.models.Category;
import com.ayahathout.book_service.repositories.CategoryRepository;
import com.ayahathout.book_service.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAllWithSubCategories()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryMapper.toEntity(categoryCreateDTO);

        // Validate name ==> must be unique
        if (categoryRepository.existsByName(category.getName())) {
            throw new BadRequestException("Category already exists with name " + category.getName());
        }

        if (categoryCreateDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryCreateDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryCreateDTO.getParentId()));
            category.setParent(parent);
        }

        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    // Validate name ==> Should not be empty
                    if (categoryUpdateDTO.name() != null && categoryUpdateDTO.name().isBlank()) {
                        throw new BadRequestException("Category name cannot be empty");
                    }

                    // Validate name ==> must be unique
                    if (categoryUpdateDTO.name() != null && !categoryUpdateDTO.name().equals(category.getName()) && categoryRepository.existsByName(categoryUpdateDTO.name())) {
                        throw new BadRequestException("Category already exists with name " + categoryUpdateDTO.name());
                    }

                    if (categoryUpdateDTO.name() != null) {
                        category.setName(categoryUpdateDTO.name());
                    }
                    if (categoryUpdateDTO.parentId() != null) {
                        Category parent = categoryRepository.findById(categoryUpdateDTO.parentId())
                                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryUpdateDTO.parentId()));
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
