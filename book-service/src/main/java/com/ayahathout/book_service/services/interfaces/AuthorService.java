package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.AuthorCreateDTO;
import com.ayahathout.book_service.dtos.AuthorUpdateDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;

import java.util.List;

public interface AuthorService {
    List<AuthorResponseDTO> getAllAuthors();
    AuthorResponseDTO getAuthorById(Long id);
    AuthorResponseDTO createAuthor(AuthorCreateDTO authorCreateDTO);
    AuthorResponseDTO updateAuthor(Long id, AuthorUpdateDTO authorUpdateDTO);
    AuthorResponseDTO deleteAuthor(Long id);
}
