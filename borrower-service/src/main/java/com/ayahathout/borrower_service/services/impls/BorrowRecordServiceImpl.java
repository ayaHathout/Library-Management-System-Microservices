package com.ayahathout.borrower_service.services.impls;

import com.ayahathout.borrower_service.clients.BookServiceClient;
import com.ayahathout.borrower_service.dtos.BookDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordUpdateDTO;
import com.ayahathout.borrower_service.enums.Status;
import com.ayahathout.borrower_service.mappers.BorrowRecordMapper;
import com.ayahathout.borrower_service.models.BorrowRecord;
import com.ayahathout.borrower_service.models.Borrower;
import com.ayahathout.borrower_service.repositories.BorrowRecordRepository;
import com.ayahathout.borrower_service.repositories.BorrowerRepository;
import com.ayahathout.borrower_service.services.interfaces.BorrowRecordService;
import com.ayahathout.common_lib.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;

    private final BorrowRecordMapper borrowRecordMapper;

    private final BorrowerRepository borrowerRepository;

    private final BookServiceClient bookServiceClient;

    @Transactional(readOnly = true)
    @Override
    public List<BorrowRecordResponseDTO> getAllBorrowRecords() {
        log.info("Fetching all borrow records");

        List<BorrowRecordResponseDTO> borrowRecords = borrowRecordRepository.findAll()
                .stream()
                .map(borrowRecordMapper::toResponseDTO)
                .toList();

        log.info("Borrow records fetched successfully {}", borrowRecords.size());
        return borrowRecords;
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowRecordResponseDTO getBorrowRecordById(Long id) {
        log.info("Fetching borrow record with id {}", id);

        BorrowRecordResponseDTO borrowRecord = borrowRecordRepository.findById(id)
                .map(borrowRecordMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Borrow record with id {} not found", id);
                    return new ResourceNotFoundException("Borrower record not found with id " + id);
                });

        log.info("Borrow record fetched successfully with id {}", id);
        return borrowRecord;
    }

    @Override
    public BorrowRecordResponseDTO createBorrowRecord(BorrowRecordCreateDTO borrowRecordCreateDTO) {
        log.info("Creating new borrow record");

        BorrowRecord borrowRecord = borrowRecordMapper.toEntity(borrowRecordCreateDTO);

        // Get the book
        BookDTO book = bookServiceClient.getBook(borrowRecordCreateDTO.getBookId());

        // Get the borrower
        Borrower borrower = borrowerRepository.findById(borrowRecordCreateDTO.getBorrowerId())
                .orElseThrow(() -> {
                    log.error("Borrower with id {} not found", borrowRecordCreateDTO.getBorrowerId());
                    return new ResourceNotFoundException("Borrower not found with id " + borrowRecordCreateDTO.getBorrowerId());
                });

        if (book.getAvailableCopies() == 0) {
            log.error("Copies not available for book with id {}", borrowRecordCreateDTO.getBookId());
            throw new ResourceNotFoundException("No available copies for book " + book.getTitle());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        bookServiceClient.updateBook(book.getId(), book);

        borrowRecord.setBookId(book.getId());
        borrowRecord.setBorrower(borrower);

        BorrowRecord savedBorrowRecord = borrowRecordRepository.save(borrowRecord);
        log.info("Borrow record created successfully with id {}", savedBorrowRecord.getId());
        return borrowRecordMapper.toResponseDTO(savedBorrowRecord);
    }

    @Override
    public BorrowRecordResponseDTO updateBorrowRecord(Long id, BorrowRecordUpdateDTO borrowRecordUpdateDTO) {
        log.info("Updating borrow record with id {}", id);

        BorrowRecordResponseDTO updatedBorrowRecord = borrowRecordRepository.findById(id)
                .map(borrowRecord -> {
                    if (borrowRecordUpdateDTO.getBorrowDate() != null) {
                        borrowRecord.setBorrowDate(borrowRecordUpdateDTO.getBorrowDate());
                    }
                    if (borrowRecordUpdateDTO.getDueDate() != null) {
                        if (borrowRecordUpdateDTO.getStatus() == Status.OVERDUE) {
                            if (ChronoUnit.DAYS.between(borrowRecordUpdateDTO.getDueDate(), LocalDate.now()) >= 0) {
                                borrowRecord.setStatus(Status.BORROWED);
                                borrowRecord.setFine(0.0);
                            }
                        }
                        borrowRecord.setDueDate(borrowRecordUpdateDTO.getDueDate());
                    }
                    if (borrowRecordUpdateDTO.getReturnDate() != null) {
                        borrowRecord.setReturnDate(borrowRecordUpdateDTO.getReturnDate());
                    }
                    if (borrowRecordUpdateDTO.getStatus() != null) {
                        borrowRecord.setStatus(borrowRecordUpdateDTO.getStatus());
                    }
                    if (borrowRecordUpdateDTO.getFine() != null) {
                        borrowRecord.setFine(borrowRecordUpdateDTO.getFine());
                    }
                    if (borrowRecordUpdateDTO.getBorrowerId() != null) {
                        borrowerRepository.findById(borrowRecordUpdateDTO.getBorrowerId())
                                .ifPresent(borrowRecord::setBorrower);
                    }
                    if (borrowRecordUpdateDTO.getBookId() != null) {
                        BookDTO book = bookServiceClient.getBook(borrowRecordUpdateDTO.getBookId());
                        if (book.getAvailableCopies() == 0) {
                            log.error("No available copies for book with id {}", borrowRecordUpdateDTO.getBookId());
                            throw new ResourceNotFoundException("No available copies for book " + book.getTitle());
                        }
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        bookServiceClient.updateBook(book.getId(), book);
                        borrowRecord.setBookId(book.getId());
                    }

                    return borrowRecordRepository.save(borrowRecord);
                })
                .map(borrowRecordMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Borrow record with id {} not found", id);
                    return new ResourceNotFoundException("Borrower record not found with id " + id);
                });

        log.info("Borrow record updated successfully with id {}", id);
        return updatedBorrowRecord;
    }

    @Override
    public void deleteBorrowRecord(Long id) {
        log.info("Deleting borrow record with id {}", id);

        BorrowRecord borrowRecord = borrowRecordRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Borrow record with id {} not found", id);
                    return new ResourceNotFoundException("Borrower record not found with id " + id);
                });

        borrowRecordRepository.deleteById(id);
        log.info("Borrower record deleted successfully with id {}", id);
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
    public BorrowRecordResponseDTO returnBook(Long borrowRecordId) {
        log.info("Returning book for borrow record with id {}", borrowRecordId);

        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> {
                    log.error("Borrow record with id {} not found", borrowRecordId);
                    return new ResourceNotFoundException("BorrowRecord not found with id " + borrowRecordId);
                });

        record.setReturnDate(LocalDate.now());
        record.setStatus(Status.RETURNED);

        BookDTO book = bookServiceClient.getBook(record.getBookId());

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookServiceClient.updateBook(book.getId(), book);

        if (record.getReturnDate().isAfter(record.getDueDate())) {
            Long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), record.getReturnDate());
            record.setFine(overdueDays * 10.0);
        }

        BorrowRecord savedBorrowRecord = borrowRecordRepository.save(record);
        log.info("Book returned successfully for borrow record with id {}", savedBorrowRecord.getId());
        return borrowRecordMapper.toResponseDTO(savedBorrowRecord);
    }

    // To handle the book borrow case
    @Override
    public BorrowRecordResponseDTO borrowBook(Long borrowId, Long bookId) {
        log.info("Borrower with id {} is borrowing book with id {}", borrowId, bookId);

        BookDTO book = bookServiceClient.getBook(bookId);

        // Get the borrower
        Borrower borrower = borrowerRepository.findById(borrowId)
                .orElseThrow(() -> {
                    log.error("Borrower with id {} not found", borrowId);
                    return new ResourceNotFoundException("Borrower not found with id " + borrowId);
                });

        if (book.getAvailableCopies() == 0) {
            log.error("No available copies for book with id {}", bookId);
            throw new ResourceNotFoundException("No available copies for book " + book.getTitle());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookServiceClient.updateBook(bookId, book);

        BorrowRecord borrowRecord = BorrowRecord.builder()
                .borrower(borrower)
                .bookId(book.getId())
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(1))
                .status(Status.BORROWED)
                .fine(0.0)
                .build();

        BorrowRecord savedBorrowRecord = borrowRecordRepository.save(borrowRecord);
        log.info("Borrower with id {} borrowed book with id {} successfully", borrowId, bookId);
        return borrowRecordMapper.toResponseDTO(savedBorrowRecord);
    }
}