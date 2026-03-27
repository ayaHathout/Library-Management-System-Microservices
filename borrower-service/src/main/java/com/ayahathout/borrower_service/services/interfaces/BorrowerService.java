package com.example.libraryManagementSystem.services.interfaces;

import com.example.libraryManagementSystem.dtos.BorrowerDTO;
import com.example.libraryManagementSystem.dtos.BorrowerResponseDTO;
import com.example.libraryManagementSystem.entities.Borrower;

import java.util.List;
import java.util.Optional;

public interface BorrowerService {
    List<BorrowerResponseDTO> getAllBorrowers();
    Optional<BorrowerResponseDTO> getBorrowerById(Long id);
    BorrowerResponseDTO createBorrower(BorrowerDTO borrowerDTO);
    Optional<BorrowerResponseDTO> updateBorrower(Long id, BorrowerDTO borrowerDTO);
    Optional<BorrowerResponseDTO> deleteBorrower(Long id);
}
