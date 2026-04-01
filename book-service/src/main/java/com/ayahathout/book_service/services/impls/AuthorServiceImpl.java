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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        log.info("Fetching all authors");

        List<AuthorResponseDTO> authors = authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponseDTO)
                .toList();

        log.info("Authors fetched successfully {}", authors.size());
        return authors;
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorResponseDTO getAuthorById(Long id) {
        log.info("Fetching author with id {}", id);

        AuthorResponseDTO author = authorRepository.findById(id)
                .map(authorMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Author not found with id {}", id);
                    return new ResourceNotFoundException("Author not found with id " + id);
                });

        log.info("Author fetched successfully with id {}", id);
        return author;
    }

    @Override
    public AuthorResponseDTO createAuthor(AuthorCreateDTO authorCreateDTO) {
        log.info("Creating new author");

        Author savedAuthor = authorRepository.save(authorMapper.toEntity(authorCreateDTO));

        log.info("Author created successfully with id {}", savedAuthor.getId());
        return authorMapper.toResponseDTO(savedAuthor);
    }

    @Override
    public AuthorResponseDTO updateAuthor(Long id, AuthorUpdateDTO authorUpdateDTO) {
        log.info("Updating author with id {}", id);

        AuthorResponseDTO updatedAuthor = authorRepository.findById(id)
                .map(author -> {
                    // Validate first name ==> Should not be empty
                    if (authorUpdateDTO.firstName() != null && authorUpdateDTO.firstName().isBlank()) {
                        log.error("First name is blank for author with id {}", id);
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
                .orElseThrow(() -> {
                    log.error("Author not found with id {}", id);
                    return new ResourceNotFoundException("Author not found with id " + id);
                });

        log.info("Author updated successfully with id {}", id);
        return updatedAuthor;
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Deleting author with id {}", id);

        Author authorToDelete = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Author not found with id {}", id);
                    return new ResourceNotFoundException("Author not found with id " + id);
                });

        authorRepository.delete(authorToDelete);
        log.info("Author deleted successfully with id {}", id);
    }
}