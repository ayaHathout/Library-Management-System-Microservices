package com.ayahathout.borrower_service.services.impls;

import com.ayahathout.borrower_service.dtos.BorrowerCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowerResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowerUpdateDTO;
import com.ayahathout.borrower_service.mappers.BorrowerMapper;
import com.ayahathout.borrower_service.models.Borrower;
import com.ayahathout.borrower_service.repositories.BorrowerRepository;
import com.ayahathout.borrower_service.services.interfaces.BorrowerService;
import com.ayahathout.common_lib.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {
    private final BorrowerRepository borrowerRepository;

    private final BorrowerMapper borrowerMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BorrowerResponseDTO> getAllBorrowers() {
        log.info("Fetching all borrowers");

        List<BorrowerResponseDTO> borrowers = borrowerRepository.findAll()
                .stream()
                .map(borrowerMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Borrowers fetched successfully {}", borrowers.size());
        return borrowers;
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowerResponseDTO getBorrowerById(Long id) {
        log.info("Fetching borrower with id {}", id);

        BorrowerResponseDTO borrower = borrowerRepository.findById(id)
                .map(borrowerMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Borrower not found with id {}", id);
                    return new ResourceNotFoundException("Borrower not found with id " + id);
                });

        log.info("Borrower fetched successfully with id {}", id);
        return borrower;
    }

    @Override
    public BorrowerResponseDTO createBorrower(BorrowerCreateDTO borrowerCreateDTO) {
        log.info("Creating new borrower");

        Borrower borrower = borrowerMapper.toEntity(borrowerCreateDTO);
        Borrower savedBorrower = borrowerRepository.save(borrower);

        log.info("Borrower created successfully with id {}", savedBorrower.getId());
        return borrowerMapper.toResponseDTO(savedBorrower);
    }

    @Override
    public BorrowerResponseDTO updateBorrower(Long id, BorrowerUpdateDTO borrowerUpdateDTO) {
        log.info("Updating borrower with id {}", id);

        Borrower borrowerToUpdate = borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Borrower not found with id {}", id);
                    return new ResourceNotFoundException("Borrower not found with id " + id);
                });

        borrowerMapper.updateEntityFromDTO(borrowerUpdateDTO, borrowerToUpdate);
        Borrower updatedBorrower = borrowerRepository.save(borrowerToUpdate);

        log.info("Borrower updated successfully with id {}", id);
        return borrowerMapper.toResponseDTO(updatedBorrower);
    }

    @Override
    public void deleteBorrower(Long id) {
        log.info("Deleting borrower with id {}", id);

        Borrower borrowerToDelete = borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Borrower not found with id {}", id);
                    return new ResourceNotFoundException("Borrower not found with id " + id);
                });

        borrowerRepository.delete(borrowerToDelete);
        log.info("Borrower deleted successfully with id {}", id);
    }
}