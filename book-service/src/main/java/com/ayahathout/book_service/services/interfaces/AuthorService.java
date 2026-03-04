package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.AuthorDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorResponseDTO> getAllAuthors();
    Optional<AuthorResponseDTO> getAuthorById(Long id);
    AuthorResponseDTO createAuthor(AuthorDTO authorDTO);
    Optional<AuthorResponseDTO> updateAuthor(Long id, AuthorDTO authorDTO);
    Optional<AuthorResponseDTO> deleteAuthor(Long id);
}
