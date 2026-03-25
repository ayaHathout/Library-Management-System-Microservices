package com.ayahathout.book_service.dtos;

public record AuthorUpdateDTO(
        String firstName,
        String lastName,
        String bio
) {
}
