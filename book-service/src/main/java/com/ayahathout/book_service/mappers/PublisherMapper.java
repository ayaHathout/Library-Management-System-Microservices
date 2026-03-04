package com.ayahathout.book_service.mappers;

import com.ayahathout.book_service.dtos.PublisherDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.models.Book;
import com.ayahathout.book_service.models.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    // For create and update
    PublisherDTO toDTO(Publisher publisher);

    Publisher toEntity(PublisherDTO publisherDTO);

    // For get
    @Mapping(target = "bookIds", source = "books")
    PublisherResponseDTO toResponseDTO(Publisher publisher);

    // Helper method to map books → bookIds
    default List<Long> mapBooksToIds(List<Book> books) {
        if (books == null)  return null;
        return books.stream()
                .map(Book::getId)
                .toList();
    }
}
