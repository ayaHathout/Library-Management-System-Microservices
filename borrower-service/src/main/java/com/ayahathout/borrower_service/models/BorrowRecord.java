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
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Double fine;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    // -----------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower;
}