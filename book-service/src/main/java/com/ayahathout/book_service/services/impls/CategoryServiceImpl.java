package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.CategoryCreateDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;
import com.ayahathout.book_service.dtos.CategoryUpdateDTO;
import com.ayahathout.common_lib.exceptions.BadRequestException;
import com.ayahathout.common_lib.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.CategoryMapper;
import com.ayahathout.book_service.models.Category;
import com.ayahathout.book_service.repositories.CategoryRepository;
import com.ayahathout.book_service.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        log.info("Fetching all categories");

        List<CategoryResponseDTO> categories = categoryRepository.findAllWithSubCategories()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();

        log.info("Categories fetched successfully {}", categories.size());
        return categories;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        log.info("Fetching category with id {}", id);

        CategoryResponseDTO category = categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Category not found with id {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });

        log.info("Category fetched successfully with id {}", id);
        return category;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        log.info("Creating new category");

        Category category = categoryMapper.toEntity(categoryCreateDTO);

        // Validate name ==> must be unique
        if (categoryRepository.existsByName(category.getName())) {
            log.error("Category with name {} already exists", category.getName());
            throw new BadRequestException("Category already exists with name " + category.getName());
        }

        if (categoryCreateDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryCreateDTO.getParentId())
                    .orElseThrow(() -> {
                        log.error("Parent category not found with id {}", categoryCreateDTO.getParentId());
                        return new ResourceNotFoundException("Parent category not found with id " + categoryCreateDTO.getParentId());
                    });
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with id {}", savedCategory.getId());
        return categoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        log.info("Updating category with id {}", id);

        CategoryResponseDTO updatedCategory = categoryRepository.findById(id)
                .map(category -> {
                    // Validate name ==> Should not be empty
                    if (categoryUpdateDTO.name() != null && categoryUpdateDTO.name().isBlank()) {
                        log.error("Category name cannot be empty for category with id {}", id);
                        throw new BadRequestException("Category name cannot be empty");
                    }

                    // Validate name ==> must be unique
                    if (categoryUpdateDTO.name() != null && !categoryUpdateDTO.name().equals(category.getName()) && categoryRepository.existsByName(categoryUpdateDTO.name())) {
                        log.error("Category with name {} already exists", categoryUpdateDTO.name());
                        throw new BadRequestException("Category already exists with name " + categoryUpdateDTO.name());
                    }

                    if (categoryUpdateDTO.name() != null) {
                        category.setName(categoryUpdateDTO.name());
                    }
                    if (categoryUpdateDTO.parentId() != null) {
                        Category parent = categoryRepository.findById(categoryUpdateDTO.parentId())
                                .orElseThrow(() -> {
                                    log.error("Parent category not found with id {}", categoryUpdateDTO.parentId());
                                    return new ResourceNotFoundException("Parent category not found with id " + categoryUpdateDTO.parentId());
                                });
                        category.setParent(parent);
                    }

                    return categoryRepository.save(category);
                })
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Category not found with id {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });

        log.info("Category updated successfully with id {}", id);
        return updatedCategory;
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with id {}", id);

        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with id {}", id);
                    return new ResourceNotFoundException("Category not found with id " + id);
                });

        if (!categoryToDelete.getSubCategories().isEmpty()) {
            log.error("Cannot delete category with id {}", id);
            throw new BadRequestException("Cannot delete category with id " + id + " because it has " + categoryToDelete.getSubCategories().size() + " associated sub categories.");
        }

        categoryRepository.delete(categoryToDelete);
        log.info("Category deleted successfully with id {}", id);
    }
}