package com.ayahathout.borrower_service.services.interfaces;

import com.ayahathout.borrower_service.dtos.BorrowerCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowerResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowerUpdateDTO;

import java.util.List;

public interface BorrowerService {
    List<BorrowerResponseDTO> getAllBorrowers();
    BorrowerResponseDTO getBorrowerById(Long id);
    BorrowerResponseDTO createBorrower(BorrowerCreateDTO borrowerCreateDTO);
    BorrowerResponseDTO updateBorrower(Long id, BorrowerUpdateDTO borrowerUpdateDTO);
    void deleteBorrower(Long id);
}