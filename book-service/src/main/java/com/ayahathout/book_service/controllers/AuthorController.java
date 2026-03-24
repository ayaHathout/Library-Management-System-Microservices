package com.ayahathout.book_service.controllers;

import com.ayahathout.book_service.dtos.AuthorCreateDTO;
import com.ayahathout.book_service.dtos.AuthorUpdateDTO;
import com.ayahathout.book_service.dtos.AuthorResponseDTO;
import com.ayahathout.book_service.services.interfaces.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@Valid @RequestBody AuthorCreateDTO authorCreateDTO) {
        AuthorResponseDTO retAuthor = authorService.createAuthor(authorCreateDTO);
        return ResponseEntity.created(URI.create("/authors/" + retAuthor.id())).body(retAuthor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorUpdateDTO authorUpdateDTO) {
        AuthorResponseDTO author = authorService.updateAuthor(id, authorUpdateDTO);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> deleteAuthor(@PathVariable Long id) {
        AuthorResponseDTO author = authorService.deleteAuthor(id);
        return ResponseEntity.ok(author);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<AuthorResponseDTO> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable Long id) {
        AuthorResponseDTO author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }
}