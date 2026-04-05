package com.ayahathout.borrower_service.dtos;

import com.ayahathout.borrower_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecordUpdateDTO implements Serializable {
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Status status;
    private Double fine;
    private Long bookId;
    private Long borrowerId;
}