package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.AuthorCreateDTO;
import com.ayahathout.book_service.dtos.AuthorUpdateDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;
import com.ayahathout.book_service.models.Author;
import com.ayahathout.common_lib.exceptions.BadRequestException;
import com.ayahathout.common_lib.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.AuthorMapper;
import com.ayahathout.book_service.repositories.AuthorRepository;
import com.ayahathout.book_service.services.interfaces.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorResponseDTO getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Override
    public AuthorResponseDTO createAuthor(AuthorCreateDTO authorCreateDTO) {
        return authorMapper.toResponseDTO(authorRepository.save(authorMapper.toEntity(authorCreateDTO)));
    }

    @Override
    public AuthorResponseDTO updateAuthor(Long id, AuthorUpdateDTO authorUpdateDTO) {
        return authorRepository.findById(id)
                .map(author -> {
                    // Validate first name ==> Should not be empty
                    if (authorUpdateDTO.firstName() != null && authorUpdateDTO.firstName().isBlank()) {
                        throw new BadRequestException("Author first name cannot be empty");
                    }

                    if (authorUpdateDTO.firstName() != null) {
                        author.setFirstName(authorUpdateDTO.firstName());
                    }
                    if (authorUpdateDTO.lastName() != null) {
                        author.setLastName(authorUpdateDTO.lastName());
                    }
                    if (authorUpdateDTO.bio() != null) {
                        author.setBio(authorUpdateDTO.bio());
                    }
                    return authorRepository.save(author);
                })
                .map(authorMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Override
    public void deleteAuthor(Long id) {
        Author authorToDelete = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));

        authorRepository.delete(authorToDelete);
    }
}