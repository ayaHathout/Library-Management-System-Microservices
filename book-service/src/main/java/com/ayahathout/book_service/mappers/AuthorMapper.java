package com.ayahathout.book_service.mappers;

import com.ayahathout.book_service.dtos.AuthorDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;
import com.ayahathout.book_service.models.Author;
import com.ayahathout.book_service.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    // For create
    AuthorDTO toDTO(Author author);

    Author toEntity(AuthorDTO authorDTO);

    // For get
    @Mapping(target = "bookTitles", expression = "java(mapBooks(author.getBooks()))")
    AuthorResponseDTO toResponseDTO(Author author);

    // Helper method to map books → bookTitles
    default List<String> mapBooks(List<Book> books) {
        if (books == null) return null;
        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
