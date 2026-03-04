package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.PublisherDTO;
import com.ayahathout.book_service.dtos.PublisherResponseDTO;
import com.ayahathout.book_service.mappers.PublisherMapper;
import com.ayahathout.book_service.models.Publisher;
import com.ayahathout.book_service.repositories.BookRepository;
import com.ayahathout.book_service.repositories.PublisherRepository;
import com.ayahathout.book_service.services.interfaces.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherServiceImpl implements PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherMapper publisherMapper;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<PublisherResponseDTO> getAllPublishers() {
        return publisherRepository.findAllWithBooks()
                .stream()
                .map(publisherMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Optional<PublisherResponseDTO> getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toResponseDTO);
    }

    @Override
    public PublisherResponseDTO createPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = publisherMapper.toEntity(publisherDTO);
        return publisherMapper.toResponseDTO(publisherRepository.save(publisher));
    }

    @Override
    public Optional<PublisherResponseDTO> updatePublisher(Long id, PublisherDTO publisherDTO) {
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
                .map(publisherMapper::toResponseDTO);
    }

    @Override
    public Optional<PublisherResponseDTO> deletePublisher(Long id) {
        return publisherRepository.findById(id)
                .map(publisher -> {
                    if (!publisher.getBooks().isEmpty()) {
                        throw new RuntimeException("Cannot delete publisher with ID " + id + " because it has " + publisher.getBooks().size() + " associated books.");
                    }

                    PublisherResponseDTO deletedPublisher = publisherMapper.toResponseDTO(publisher);
                    publisherRepository.delete(publisher);
                    return deletedPublisher;
                });
    }
}
