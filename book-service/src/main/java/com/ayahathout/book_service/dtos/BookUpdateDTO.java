package com.ayahathout.book_service.dtos;

import com.ayahathout.book_service.enums.Language;

import java.util.Set;

public record BookUpdateDTO(
        String title,
        String isbn,
        String edition,
        Integer publicationYear,
        Language language,
        String summary,
        String coverImage,
        Long totalCopies,
        Long availableCopies,
        Long publisherId,
        Set<Long> authorIds,
        Set<Long> categoryIds
) {}
