package com.ayahathout.borrower_service.controllers;

import com.ayahathout.borrower_service.dtos.BorrowRecordCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordUpdateDTO;
import com.ayahathout.borrower_service.services.interfaces.BorrowRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/borrow-records")
@RequiredArgsConstructor
public class BorrowRecordController {
    private final BorrowRecordService borrowRecordService;

    @PostMapping
    public ResponseEntity<BorrowRecordResponseDTO> createBorrowRecord(@Valid @RequestBody BorrowRecordCreateDTO borrowRecordCreateDTO) {
        BorrowRecordResponseDTO retBorrowRecord = borrowRecordService.createBorrowRecord(borrowRecordCreateDTO);
        return ResponseEntity.created(URI.create("/borrow-records/" + retBorrowRecord.id())).body(retBorrowRecord);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BorrowRecordResponseDTO> updateBorrowRecord(@PathVariable Long id, @Valid @RequestBody BorrowRecordUpdateDTO borrowRecordUpdateDTO) {
        BorrowRecordResponseDTO borrowRecord = borrowRecordService.updateBorrowRecord(id, borrowRecordUpdateDTO);
        return ResponseEntity.ok(borrowRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowRecord(@PathVariable Long id) {
        borrowRecordService.deleteBorrowRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BorrowRecordResponseDTO>> getAllBorrowRecords() {
        List<BorrowRecordResponseDTO> borrowRecords = borrowRecordService.getAllBorrowRecords();
        return ResponseEntity.ok(borrowRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecordResponseDTO> getBorrowRecord(@PathVariable Long id) {
        BorrowRecordResponseDTO borrowRecord = borrowRecordService.getBorrowRecordById(id);
        return ResponseEntity.ok(borrowRecord);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BorrowRecordResponseDTO> returnBook(@PathVariable Long id) {
        BorrowRecordResponseDTO borrowRecordDTO = borrowRecordService.returnBook(id);
        return ResponseEntity.ok(borrowRecordDTO);
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecordResponseDTO> borrowBook(@Valid @RequestBody BorrowRecordCreateDTO borrowRecordCreateDTO) {
        BorrowRecordResponseDTO created = borrowRecordService.borrowBook(borrowRecordCreateDTO.getBorrowerId(), borrowRecordCreateDTO.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}