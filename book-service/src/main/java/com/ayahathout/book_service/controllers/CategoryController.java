package com.ayahathout.book_service.controllers;

import com.ayahathout.book_service.dtos.CategoryDTO;
import com.ayahathout.book_service.dtos.CategoryResponseDTO;
import com.ayahathout.book_service.services.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO retCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.created(URI.create("/categories/" + retCategory.id())).body(retCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO updatedCategory) {
        CategoryResponseDTO category = categoryService.updateCategory(id, updatedCategory);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> deleteCategory(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.deleteCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategorys() {
        List<CategoryResponseDTO> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
}
