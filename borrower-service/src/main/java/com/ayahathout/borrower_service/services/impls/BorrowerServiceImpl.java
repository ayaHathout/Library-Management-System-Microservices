package com.example.libraryManagementSystem.services.impls;

import com.example.libraryManagementSystem.dtos.BorrowerDTO;
import com.example.libraryManagementSystem.dtos.BorrowerResponseDTO;
import com.example.libraryManagementSystem.entities.Borrower;
import com.example.libraryManagementSystem.mappers.BorrowerMapper;
import com.example.libraryManagementSystem.repositories.BorrowerRepository;
import com.example.libraryManagementSystem.services.interfaces.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowerServiceImpl implements BorrowerService {
    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BorrowerMapper borrowerMapper;

    @Override
    public List<BorrowerResponseDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream().map(borrowerMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<BorrowerResponseDTO> getBorrowerById(Long id) {
        return borrowerRepository.findById(id).map(borrowerMapper::toResponseDTO);
    }

    @Override
    public BorrowerResponseDTO createBorrower(BorrowerDTO borrowerDTO) {
        Borrower borrower = borrowerMapper.toEntity(borrowerDTO);
        return borrowerMapper.toResponseDTO(borrowerRepository.save(borrower));
    }

    @Override
    public Optional<BorrowerResponseDTO> updateBorrower(Long id, BorrowerDTO borrowerDTO) {
        return borrowerRepository.findById(id)
                .map(borrower -> {
                    if (borrowerDTO.email() != null) {
                        borrower.setEmail(borrowerDTO.email());
                    }
                    if (borrowerDTO.firstName() != null) {
                        borrower.setFirstName(borrowerDTO.firstName());
                    }
                    if (borrowerDTO.lastName() != null) {
                        borrower.setLastName(borrowerDTO.lastName());
                    }
                    if (borrowerDTO.phone() != null) {
                        borrower.setPhone(borrowerDTO.phone());
                    }
                    if (borrowerDTO.address() != null) {
                        borrower.setAddress(borrowerDTO.address());
                    }
                    return borrowerRepository.save(borrower);
                }).map(borrowerMapper::toResponseDTO);
    }

    @Override
    public Optional<BorrowerResponseDTO> deleteBorrower(Long id) {
        Optional<BorrowerResponseDTO> borrower = borrowerRepository.findById(id).map(borrowerMapper::toResponseDTO);
        if (borrower.isPresent()) {
            borrowerRepository.deleteById(id);
        }
        return borrower;
    }
}