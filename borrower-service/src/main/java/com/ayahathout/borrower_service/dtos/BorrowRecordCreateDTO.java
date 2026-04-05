package com.ayahathout.borrower_service.dtos;

import com.ayahathout.borrower_service.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecordCreateDTO implements Serializable {
    private LocalDate borrowDate = LocalDate.now();
    private LocalDate dueDate = LocalDate.now().plusWeeks(1);
    private LocalDate returnDate;
    private Status status = Status.BORROWED;
    private Double fine = 0.0;

    @NotNull(message = "Book id is required")
    private Long bookId;

    @NotNull(message = "Borrower id is required")
    private Long borrowerId;
}