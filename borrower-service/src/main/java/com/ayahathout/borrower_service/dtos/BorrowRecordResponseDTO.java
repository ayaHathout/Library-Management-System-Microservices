package com.ayahathout.borrower_service.dtos;

import com.ayahathout.borrower_service.enums.Status;

import java.time.LocalDate;

public record BorrowRecordResponseDTO(
        Long id,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        Status status,
        Double fine,
        Long borrowerId,
        Long bookId
){}