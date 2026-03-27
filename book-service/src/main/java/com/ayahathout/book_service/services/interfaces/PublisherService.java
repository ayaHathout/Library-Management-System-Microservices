package com.ayahathout.book_service.services.interfaces;

import com.ayahathout.book_service.dtos.PublisherCreateDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.dtos.PublisherUpdateDTO;

import java.util.List;

public interface PublisherService {
    List<PublisherResponseDTO> getAllPublishers();
    PublisherResponseDTO getPublisherById(Long id);
    PublisherResponseDTO createPublisher(PublisherCreateDTO publisherCreateDTO);
    PublisherResponseDTO updatePublisher(Long id, PublisherUpdateDTO publisherUpdateDTO);
    void deletePublisher(Long id);
}
