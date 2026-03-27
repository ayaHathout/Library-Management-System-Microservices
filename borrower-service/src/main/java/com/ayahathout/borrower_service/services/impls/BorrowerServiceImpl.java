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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {
    private final BorrowerRepository borrowerRepository;

    private final BorrowerMapper borrowerMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BorrowerResponseDTO> getAllBorrowers() {
        return borrowerRepository.findAll()
                .stream()
                .map(borrowerMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowerResponseDTO getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .map(borrowerMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id " + id));
    }

    @Override
    public BorrowerResponseDTO createBorrower(BorrowerCreateDTO borrowerCreateDTO) {
        Borrower borrower = borrowerMapper.toEntity(borrowerCreateDTO);
        return borrowerMapper.toResponseDTO(borrowerRepository.save(borrower));
    }

    @Override
    public BorrowerResponseDTO updateBorrower(Long id, BorrowerUpdateDTO borrowerUpdateDTO) {
        Borrower borrowerToUpdate = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id " + id));

        borrowerMapper.updateEntityFromDTO(borrowerUpdateDTO, borrowerToUpdate);
        return borrowerMapper.toResponseDTO(borrowerRepository.save(borrowerToUpdate));
    }

    @Override
    public void deleteBorrower(Long id) {
        Borrower borrowerToDelete = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id " + id));

        borrowerRepository.delete(borrowerToDelete);
    }
}