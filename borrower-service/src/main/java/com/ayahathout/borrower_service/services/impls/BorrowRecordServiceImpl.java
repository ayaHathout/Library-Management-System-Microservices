package com.example.libraryManagementSystem.services.impls;

import com.example.libraryManagementSystem.dtos.BorrowRecordDTO;
import com.example.libraryManagementSystem.entities.Book;
import com.example.libraryManagementSystem.entities.BorrowRecord;
import com.example.libraryManagementSystem.entities.Borrower;
import com.example.libraryManagementSystem.enums.Status;
import com.example.libraryManagementSystem.mappers.BorrowRecordMapper;
import com.example.libraryManagementSystem.repositories.BookRepository;
import com.example.libraryManagementSystem.repositories.BorrowRecordRepository;
import com.example.libraryManagementSystem.repositories.BorrowerRepository;
import com.example.libraryManagementSystem.services.interfaces.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordServiceImpl implements BorrowRecordService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BorrowRecordDTO> getAllBorrowRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(borrowRecordMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BorrowRecordDTO> getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id)
                .map(borrowRecordMapper::toDTO);
    }

    @Override
    public BorrowRecordDTO createBorrowRecord(BorrowRecordDTO borrowRecordDTO) {
        BorrowRecord borrowRecord = borrowRecordMapper.toEntity(borrowRecordDTO);

        // Get the book
        Book book = bookRepository.findById(borrowRecordDTO.bookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + borrowRecordDTO.bookId()));

        // Get the borrower
        Borrower borrower = borrowerRepository.findById(borrowRecordDTO.borrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found with id: " + borrowRecordDTO.borrowerId()));

        if (book.getAvailableCopies() == 0) {
            throw new RuntimeException("No available copies for book: " + book.getTitle());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        borrowRecord.setBook(book);
        borrowRecord.setBorrower(borrower);

        return borrowRecordMapper.toDTO(borrowRecordRepository.save(borrowRecord));
    }

    @Override
    public Optional<BorrowRecordDTO> updateBorrowRecord(Long id, BorrowRecordDTO updateBorrowRecordDTO) {
        return borrowRecordRepository.findById(id)
                .map(borrowRecord -> {
                    if (updateBorrowRecordDTO.borrowDate() != null) {
                        borrowRecord.setBorrowDate(updateBorrowRecordDTO.borrowDate());
                    }
                    if (updateBorrowRecordDTO.dueDate() != null) {
                        if (updateBorrowRecordDTO.status() == Status.OVERDUE) {
                            if (ChronoUnit.DAYS.between(updateBorrowRecordDTO.dueDate(), LocalDate.now()) >= 0) {
                                borrowRecord.setStatus(Status.BORROWED);
                                borrowRecord.setFine(0.0);
                            }
                        }
                        borrowRecord.setDueDate(updateBorrowRecordDTO.dueDate());
                    }
                    if (updateBorrowRecordDTO.returnDate() != null) {
                        borrowRecord.setReturnDate(updateBorrowRecordDTO.returnDate());
                    }
                    if (updateBorrowRecordDTO.status() != null) {
                        borrowRecord.setStatus(updateBorrowRecordDTO.status());
                    }
                    if (updateBorrowRecordDTO.fine() != null) {
                        borrowRecord.setFine(updateBorrowRecordDTO.fine());
                    }
                    if (updateBorrowRecordDTO.borrowerId() != null) {
                        borrowerRepository.findById(updateBorrowRecordDTO.borrowerId())
                                .ifPresent(borrowRecord::setBorrower);
                    }
                    if (updateBorrowRecordDTO.bookId() != null) {
                        Optional<Book> book = bookRepository.findById(updateBorrowRecordDTO.bookId());
                        if (book.isPresent()) {
                            if (book.get().getAvailableCopies() == 0) {
                                throw new RuntimeException("No available copies for book: " + book.get().getTitle());
                            }
                            book.get().setAvailableCopies(book.get().getAvailableCopies() - 1);
                            borrowRecord.setBook(book.get());
                        }
                        else {
                            throw new RuntimeException("Book not found with id: " + updateBorrowRecordDTO.bookId());
                        }
                    }
                    return borrowRecordRepository.save(borrowRecord);
                })
                .map(borrowRecordMapper::toDTO);
    }

    @Override
    public Optional<BorrowRecordDTO> deleteBorrowRecord(Long id) {
        Optional<BorrowRecord> borrowRecord = borrowRecordRepository.findById(id);
        if (borrowRecord.isPresent()) {
            borrowRecordRepository.deleteById(id);
        }
        return borrowRecord.map(borrowRecordMapper::toDTO);
    }

    // To track the status and DueDate
    @Scheduled(cron = "0 0 0 * * ?") // Everyday at 12 AM
    public void updateOverdueRecords() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        for (BorrowRecord record : records) {
            if (record.getStatus() == Status.BORROWED && LocalDate.now().isAfter(record.getDueDate())) {
                record.setStatus(Status.OVERDUE);
                Long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
                record.setFine(overdueDays * 10.0);  // Everyday late = 10.0

                borrowRecordRepository.save(record);
            }
        }
    }

    // To handle the book return case
    @Override
    public BorrowRecordDTO returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new RuntimeException("BorrowRecord not found with id: " + borrowRecordId));

        record.setReturnDate(LocalDate.now());
        record.setStatus(Status.RETURNED);

        Book book = bookRepository.findById(record.getBook().getId())
                        .orElseThrow(() -> new RuntimeException("Book not found with id: " + record.getBook().getId()));

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        if (record.getReturnDate().isAfter(record.getDueDate())) {
            Long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), record.getReturnDate());
            record.setFine(overdueDays * 10.0);
        }

        return borrowRecordMapper.toDTO(borrowRecordRepository.save(record));
    }

    // To handle the book borrow case
    @Override
    public BorrowRecordDTO borrowBook(Long borrowId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Get the borrower
        Borrower borrower = borrowerRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrower not found with id: " + borrowId));

        if (book.getAvailableCopies() == 0) {
            throw new RuntimeException("No available copies for book: " + book.getTitle());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        BorrowRecord borrowRecord = BorrowRecord.builder()
                .borrower(borrower)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(1))
                .status(Status.BORROWED)
                .fine(0.0)
                .build();

        return borrowRecordMapper.toDTO(borrowRecordRepository.save(borrowRecord));
    }
}
