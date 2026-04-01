package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.PublisherCreateDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.dtos.PublisherUpdateDTO;
import com.ayahathout.common_lib.exceptions.BadRequestException;
import com.ayahathout.common_lib.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.PublisherMapper;
import com.ayahathout.book_service.models.Publisher;
import com.ayahathout.book_service.repositories.PublisherRepository;
import com.ayahathout.book_service.services.interfaces.PublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;

    @Transactional(readOnly = true)
    @Override
    public List<PublisherResponseDTO> getAllPublishers() {
        log.info("Fetching all publishers");

        List<PublisherResponseDTO> publishers = publisherRepository.findAllWithBooks()
                .stream()
                .map(publisherMapper::toResponseDTO)
                .toList();

        log.info("Publishers fetched successfully {}", publishers.size());
        return publishers;
    }

    @Transactional(readOnly = true)
    @Override
    public PublisherResponseDTO getPublisherById(Long id) {
        log.info("Fetching publisher with id {}", id);

        PublisherResponseDTO publisher = publisherRepository.findById(id)
                .map(publisherMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Publisher with id {} not found", id);
                    return new ResourceNotFoundException("Publisher not found with id " + id);
                });

        log.info("Publisher fetched successfully with id {}", id);
        return publisher;
    }

    @Override
    public PublisherResponseDTO createPublisher(PublisherCreateDTO publisherCreateDTO) {
        log.info("Creating new publisher");

        Publisher publisher = publisherMapper.toEntity(publisherCreateDTO);

        // Validate name ==> Must be unique
        if (publisherRepository.existsByName(publisher.getName())) {
            log.error("Publisher with name {} already exists", publisher.getName());
            throw new BadRequestException("Publisher already exists with name " + publisher.getName());
        }

        // Validate phone ==> Must be unique
        if (publisherRepository.existsByPhone(publisher.getPhone())) {
            log.error("Publisher with phone {} already exists", publisher.getPhone());
            throw new BadRequestException("Publisher already exists with phone " + publisher.getPhone());
        }

        // Validate email ==> Must be unique
        if (publisherCreateDTO.getEmail() != null && publisherRepository.existsByEmail(publisher.getEmail())) {
            log.error("Publisher with email {} already exists", publisher.getEmail());
            throw new BadRequestException("Publisher already exists with email " + publisher.getEmail());
        }

        Publisher savedPublisher = publisherRepository.save(publisher);
        log.info("Publisher created successfully with id {}", savedPublisher.getId());
        return publisherMapper.toResponseDTO(savedPublisher);
    }

    @Override
    public PublisherResponseDTO updatePublisher(Long id, PublisherUpdateDTO publisherUpdateDTO) {
        log.info("Updating publisher with id {}", id);

        PublisherResponseDTO updatedPublisher = publisherRepository.findById(id)
                .map(publisher -> {
                    // Validate email ==> Must be unique
                    if (publisherUpdateDTO.getEmail() != null && !publisherUpdateDTO.getEmail().equals(publisher.getEmail()) && publisherRepository.existsByEmail(publisherUpdateDTO.getEmail())) {
                        log.error("Publisher with email {} already exists", publisherUpdateDTO.getEmail());
                        throw new BadRequestException("Publisher already exists with email " + publisherUpdateDTO.getEmail());
                    }

                    // Validate phone ==> Must be unique
                    if (publisherUpdateDTO.getPhone() != null && !publisherUpdateDTO.getPhone().equals(publisher.getPhone()) && publisherRepository.existsByPhone(publisherUpdateDTO.getPhone())) {
                        log.error("Publisher with phone {} already exists", publisherUpdateDTO.getPhone());
                        throw new BadRequestException("Publisher already exists with phone " + publisherUpdateDTO.getPhone());
                    }

                    // Validate name ==> Should not be empty
                    if (publisherUpdateDTO.getName() != null && publisherUpdateDTO.getName().isBlank()) {
                        log.error("Publisher name is blank for publisher with id {}", id);
                        throw new BadRequestException("Publisher name cannot be empty");
                    }

                    // Validate name ==> Must be unique
                    if (publisherUpdateDTO.getName() != null && !publisherUpdateDTO.getName().equals(publisher.getName()) && publisherRepository.existsByName(publisherUpdateDTO.getName())) {
                        log.error("Publisher with name {} already exists", publisherUpdateDTO.getName());
                        throw new BadRequestException("Publisher already exists with name " + publisherUpdateDTO.getName());
                    }

                    // Update email
                    if (publisherUpdateDTO.getEmail() != null) {
                        publisher.setEmail(publisherUpdateDTO.getEmail());
                    }

                    // Update phone
                    if (publisherUpdateDTO.getPhone() != null) {
                        publisher.setPhone(publisherUpdateDTO.getPhone());
                    }

                    // Update name
                    if (publisherUpdateDTO.getName() != null) {
                        publisher.setName(publisherUpdateDTO.getName());
                    }

                    // Update address
                    if (publisherUpdateDTO.getAddress() != null) {
                        publisher.setAddress(publisherUpdateDTO.getAddress());
                    }

                    return publisherRepository.save(publisher);
                })
                .map(publisherMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Publisher with id {} not found", id);
                    return new ResourceNotFoundException("Publisher not found with id " + id);
                });

        log.info("Publisher updated successfully with id {}", id);
        return updatedPublisher;
    }

    @Override
    public void deletePublisher(Long id) {
        log.info("Deleting publisher with id {}", id);

        Publisher publisherToDelete = publisherRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Publisher with id {} not found", id);
                    return new ResourceNotFoundException("Publisher not found with id " + id);
                });

        if (!publisherToDelete.getBooks().isEmpty()) {
            log.error("Cannot delete publisher with id {} because it has {} associated books.", id, publisherToDelete.getBooks().size());
            throw new BadRequestException("Cannot delete publisher with id " + id + " because it has " + publisherToDelete.getBooks().size() + " associated books.");
        }

        publisherRepository.delete(publisherToDelete);
        log.info("Publisher deleted successfully with id {}", id);
    }
}