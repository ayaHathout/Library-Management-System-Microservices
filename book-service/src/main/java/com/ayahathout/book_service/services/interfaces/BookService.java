package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.BookCreateDTO;
import com.ayahathout.book_service.dtos.BookResponseDTO;
import com.ayahathout.book_service.dtos.BookUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponseDTO> getAllBooks();
    Optional<BookResponseDTO> getBookById(Long id);
    BookResponseDTO createBook(BookCreateDTO bookDTO);
    Optional<BookResponseDTO> updateBook(Long id, BookUpdateDTO bookDTO);
    Optional<BookResponseDTO> deleteBook(Long id);
}
