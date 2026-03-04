package com.ayahathout.book_service.dtos;

public record CategoryDTO(
        String name,
        Long parentId
) {
}
