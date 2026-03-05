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
        if (publisherRepository.existsByEmail(publisher.getEmail())) {
            throw new BadRequestException("Publisher already exists with email " + publisher.getEmail());
        }

        return publisherMapper.toResponseDTO(publisherRepository.save(publisher));
    }

    @Override
    public PublisherResponseDTO updatePublisher(Long id, PublisherUpdateDTO publisherUpdateDTO) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    if (publisherUpdateDTO.getEmail() != null) {
                        // Validate email ==> Must be unique
                        if (publisherRepository.existsByEmail(publisherUpdateDTO.getEmail())) {
                            throw new BadRequestException("Publisher already exists with email " + publisherUpdateDTO.getEmail());
                        }

                        publisher.setEmail(publisherUpdateDTO.getEmail());
                    }
                    if (publisherUpdateDTO.getAddress() != null) {
                        publisher.setAddress(publisherUpdateDTO.getAddress());
                    }
                    if (publisherUpdateDTO.getPhone() != null) {
                        // Validate phone ==> Must be unique
                        if (publisherRepository.existsByPhone(publisherUpdateDTO.getPhone())) {
                            throw new BadRequestException("Publisher already exists with phone " + publisherUpdateDTO.getPhone());
                        }

                        publisher.setPhone(publisherUpdateDTO.getPhone());
                    }
                    if (publisherUpdateDTO.getName() != null) {
                        // Validate name ==> Must be unique
                        if (publisherRepository.existsByName(publisherUpdateDTO.getName())) {
                            throw new BadRequestException("Publisher already exists with name " + publisherUpdateDTO.getName());
                        }

                        publisher.setName(publisherUpdateDTO.getName());
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
