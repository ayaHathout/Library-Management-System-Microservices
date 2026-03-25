package com.ayahathout.book_service.dtos;

public record CategoryUpdateDTO(
        String name,
        Long parentId
) {
}
