package com.ayahathout.book_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDTO implements Serializable {
    @NotBlank(message = "Category name cannot be empty")
    private String name;

    private Long parentId;
}
