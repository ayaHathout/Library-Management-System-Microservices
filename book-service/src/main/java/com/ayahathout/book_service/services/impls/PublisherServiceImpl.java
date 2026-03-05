package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.PublisherDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.exceptions.BadRequestException;
import com.ayahathout.book_service.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.PublisherMapper;
import com.ayahathout.book_service.models.Publisher;
import com.ayahathout.book_service.repositories.PublisherRepository;
import com.ayahathout.book_service.services.interfaces.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;

    @Override
    public List<PublisherResponseDTO> getAllPublishers() {
        return publisherRepository.findAllWithBooks()
                .stream()
                .map(publisherMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PublisherResponseDTO getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id " + id));
    }

    @Override
    public PublisherResponseDTO createPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = publisherMapper.toEntity(publisherDTO);
        return publisherMapper.toResponseDTO(publisherRepository.save(publisher));
    }

    @Override
    public PublisherResponseDTO updatePublisher(Long id, PublisherDTO publisherDTO) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    if (publisherDTO.email() != null) {
                        publisher.setEmail(publisherDTO.email());
                    }
                    if (publisherDTO.address() != null) {
                        publisher.setAddress(publisherDTO.address());
                    }
                    if (publisherDTO.phone() != null) {
                        publisher.setPhone(publisherDTO.phone());
                    }
                    if (publisherDTO.name() != null) {
                        publisher.setName(publisherDTO.name());
                    }
                    return publisherRepository.save(publisher);
                })
                .map(publisherMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id " + id));
    }

    @Override
    public PublisherResponseDTO deletePublisher(Long id) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    if (!publisher.getBooks().isEmpty()) {
                        throw new BadRequestException("Cannot delete publisher with ID " + id + " because it has " + publisher.getBooks().size() + " associated books.");
                    }

                    PublisherResponseDTO deletedPublisher = publisherMapper.toResponseDTO(publisher);
                    publisherRepository.delete(publisher);
                    return deletedPublisher;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id " + id));
    }
}
