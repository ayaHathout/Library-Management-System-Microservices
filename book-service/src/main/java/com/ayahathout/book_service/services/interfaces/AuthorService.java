package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.AuthorDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;

import java.util.List;

public interface AuthorService {
    List<AuthorResponseDTO> getAllAuthors();
    AuthorResponseDTO getAuthorById(Long id);
    AuthorResponseDTO createAuthor(AuthorDTO authorDTO);
    AuthorResponseDTO updateAuthor(Long id, AuthorDTO authorDTO);
    AuthorResponseDTO deleteAuthor(Long id);
}
