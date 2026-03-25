package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.PublisherCreateDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.dtos.PublisherUpdateDTO;
import com.ayahathout.book_service.exceptions.BadRequestException;
import com.ayahathout.book_service.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.PublisherMapper;
import com.ayahathout.book_service.models.Publisher;
import com.ayahathout.book_service.repositories.PublisherRepository;
import com.ayahathout.book_service.services.interfaces.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    private final PublisherMapper publisherMapper;

    @Transactional(readOnly = true)
    @Override
    public List<PublisherResponseDTO> getAllPublishers() {
        return publisherRepository.findAllWithBooks()
                .stream()
                .map(publisherMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PublisherResponseDTO getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id " + id));
    }

    @Override
    public PublisherResponseDTO createPublisher(PublisherCreateDTO publisherCreateDTO) {
        Publisher publisher = publisherMapper.toEntity(publisherCreateDTO);

        // Validate name ==> Must be unique
        if (publisherRepository.existsByName(publisher.getName())) {
            throw new BadRequestException("Publisher already exists with name " + publisher.getName());
        }

        // Validate phone ==> Must be unique
        if (publisherRepository.existsByPhone(publisher.getPhone())) {
            throw new BadRequestException("Publisher already exists with phone " + publisher.getPhone());
        }

        // Validate email ==> Must be unique
        if (publisherCreateDTO.getEmail() != null && publisherRepository.existsByEmail(publisher.getEmail())) {
            throw new BadRequestException("Publisher already exists with email " + publisher.getEmail());
        }

        return publisherMapper.toResponseDTO(publisherRepository.save(publisher));
    }

    @Override
    public PublisherResponseDTO updatePublisher(Long id, PublisherUpdateDTO publisherUpdateDTO) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    // Validate email ==> Must be unique
                    if (publisherUpdateDTO.getEmail() != null && !publisherUpdateDTO.getEmail().equals(publisher.getEmail()) && publisherRepository.existsByEmail(publisherUpdateDTO.getEmail())) {
                        throw new BadRequestException("Publisher already exists with email " + publisherUpdateDTO.getEmail());
                    }

                    // Validate phone ==> Must be unique
                    if (publisherUpdateDTO.getPhone() != null && !publisherUpdateDTO.getPhone().equals(publisher.getPhone()) && publisherRepository.existsByPhone(publisherUpdateDTO.getPhone())) {
                        throw new BadRequestException("Publisher already exists with phone " + publisherUpdateDTO.getPhone());
                    }

                    // Validate name ==> Should not be empty
                    if (publisherUpdateDTO.getName() != null && publisherUpdateDTO.getName().isBlank()) {
                        throw new BadRequestException("Publisher name cannot be empty");
                    }

                    // Validate name ==> Must be unique
                    if (publisherUpdateDTO.getName() != null && !publisherUpdateDTO.getName().equals(publisher.getName()) && publisherRepository.existsByName(publisherUpdateDTO.getName())) {
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
