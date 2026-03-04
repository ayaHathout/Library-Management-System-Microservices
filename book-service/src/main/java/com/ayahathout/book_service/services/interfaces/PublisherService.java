package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.PublisherDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;

import java.util.List;
import java.util.Optional;

public interface PublisherService {
    List<PublisherResponseDTO> getAllPublishers();
    Optional<PublisherResponseDTO> getPublisherById(Long id);
    PublisherResponseDTO createPublisher(PublisherDTO publisherDTO);
    Optional<PublisherResponseDTO> updatePublisher(Long id, PublisherDTO publisherDTO);
    Optional<PublisherResponseDTO> deletePublisher(Long id);
}
