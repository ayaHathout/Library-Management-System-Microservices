package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.BookCreateDTO;
import com.ayahathout.book_service.dtos.BookResponseDTO;
import com.ayahathout.book_service.dtos.BookUpdateDTO;

import java.util.List;

public interface BookService {
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(Long id);
    BookResponseDTO createBook(BookCreateDTO bookCreateDTO);
    BookResponseDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO);
    BookResponseDTO deleteBook(Long id);
}
