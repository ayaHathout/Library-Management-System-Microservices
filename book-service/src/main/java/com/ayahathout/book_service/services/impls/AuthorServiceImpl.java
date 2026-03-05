package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.AuthorDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;
import com.ayahathout.book_service.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.AuthorMapper;
import com.ayahathout.book_service.repositories.AuthorRepository;
import com.ayahathout.book_service.services.interfaces.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toResponseDTO)
                .toList();
    }

    @Override
    public AuthorResponseDTO getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Override
    public AuthorResponseDTO createAuthor(AuthorDTO authorDTO) {
        return authorMapper.toResponseDTO(authorRepository.save(authorMapper.toEntity(authorDTO)));
    }

    @Override
    public AuthorResponseDTO updateAuthor(Long id, AuthorDTO updateAuthorDTO) {
        return authorRepository.findById(id)
                .map(author -> {
                    if (updateAuthorDTO.firstName() != null) {
                        author.setFirstName(updateAuthorDTO.firstName());
                    }
                    if (updateAuthorDTO.lastName() != null) {
                        author.setLastName(updateAuthorDTO.lastName());
                    }
                    if (updateAuthorDTO.bio() != null) {
                        author.setBio(updateAuthorDTO.bio());
                    }
                    return authorRepository.save(author);
                })
                .map(authorMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Override
    public AuthorResponseDTO deleteAuthor(Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    AuthorResponseDTO dto = authorMapper.toResponseDTO(author);
                    authorRepository.delete(author);
                    return dto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }
}
