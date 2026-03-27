package com.ayahathout.borrower_service.models;

import com.ayahathout.borrower_service.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate = LocalDate.now();

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.BORROWED;

    private Double fine = 0.0;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    // -----------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower;
}
