package com.example.libraryManagementSystem.dtos;

import com.example.libraryManagementSystem.enums.Status;

import java.time.LocalDate;

public record BorrowRecordDTO(
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        Status status,
        Double fine,
        Long borrowerId,
        Long bookId
) {
    public BorrowRecordDTO {
        if (borrowDate == null)  borrowDate = LocalDate.now();
        if (dueDate == null)  dueDate = LocalDate.now().plusWeeks(1);
        if (status == null)  status = Status.BORROWED;
        if (fine == null)  fine = 0.0;
    }
}
