package com.ayahathout.book_service.dtos;

import java.util.List;

public record CategoryResponseDTO(
        Long id,
        String name,
        Long parentId,
        List<Long> subCategoryIds,
        List<Long> bookIds
) {
}
