package com.ayahathout.book_service.controllers;

import com.ayahathout.book_service.dtos.PublisherCreateDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.dtos.PublisherUpdateDTO;
import com.ayahathout.book_service.services.interfaces.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDTO> createPublisher(@Valid @RequestBody PublisherCreateDTO publisherCreateDTO) {
        PublisherResponseDTO retPublisher = publisherService.createPublisher(publisherCreateDTO);
        return ResponseEntity.created(URI.create("/publishers/" + retPublisher.id())).body(retPublisher);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(@PathVariable Long id, @Valid @RequestBody PublisherUpdateDTO publisherUpdateDTO) {
        PublisherResponseDTO publisher = publisherService.updatePublisher(id, publisherUpdateDTO);
        return ResponseEntity.ok(publisher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> deletePublisher(@PathVariable Long id) {
        PublisherResponseDTO publisher = publisherService.deletePublisher(id);
        return ResponseEntity.ok(publisher);
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponseDTO>> getAllPublishers() {
        List<PublisherResponseDTO> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(publishers);
    }

    @GetMapping("{id}")
    public ResponseEntity<PublisherResponseDTO> getPublisher(@PathVariable Long id) {
        PublisherResponseDTO publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(publisher);
    }
}
