package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.PublisherDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;

import java.util.List;

public interface PublisherService {
    List<PublisherResponseDTO> getAllPublishers();
    PublisherResponseDTO getPublisherById(Long id);
    PublisherResponseDTO createPublisher(PublisherDTO publisherDTO);
    PublisherResponseDTO updatePublisher(Long id, PublisherDTO publisherDTO);
    PublisherResponseDTO deletePublisher(Long id);
}
