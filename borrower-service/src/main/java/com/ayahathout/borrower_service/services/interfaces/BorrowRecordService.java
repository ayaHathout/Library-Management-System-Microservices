package com.example.libraryManagementSystem.services.interfaces;

import com.example.libraryManagementSystem.dtos.BorrowRecordDTO;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordService {
    List<BorrowRecordDTO> getAllBorrowRecords();
    Optional<BorrowRecordDTO> getBorrowRecordById(Long id);
    BorrowRecordDTO createBorrowRecord(BorrowRecordDTO borrowRecordDTO);
    Optional<BorrowRecordDTO> updateBorrowRecord(Long id, BorrowRecordDTO borrowRecordDTO);
    Optional<BorrowRecordDTO> deleteBorrowRecord(Long id);

    // To handle the book return case
    BorrowRecordDTO returnBook(Long borrowRecordId);

    // To handle the book borrow case
    BorrowRecordDTO borrowBook(Long borrowId, Long bookId);
}
