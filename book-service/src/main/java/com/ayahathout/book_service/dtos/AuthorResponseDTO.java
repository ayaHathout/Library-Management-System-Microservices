package com.ayahathout.book_service.dtos;

import java.util.List;

public record AuthorResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String bio,
        List<String> bookTitles
) {
}
