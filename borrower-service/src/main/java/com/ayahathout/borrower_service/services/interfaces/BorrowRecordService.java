package com.ayahathout.borrower_service.services.interfaces;

import com.ayahathout.borrower_service.dtos.BorrowRecordCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordUpdateDTO;

import java.util.List;

public interface BorrowRecordService {
    List<BorrowRecordResponseDTO> getAllBorrowRecords();
    BorrowRecordResponseDTO getBorrowRecordById(Long id);
    BorrowRecordResponseDTO createBorrowRecord(BorrowRecordCreateDTO borrowRecordCreateDTO);
    BorrowRecordResponseDTO updateBorrowRecord(Long id, BorrowRecordUpdateDTO borrowRecordUpdateDTO);
    void deleteBorrowRecord(Long id);

    // To handle the book return case
    BorrowRecordResponseDTO returnBook(Long borrowRecordId);

    // To handle the book borrow case
    BorrowRecordResponseDTO borrowBook(Long borrowId, Long bookId);
}