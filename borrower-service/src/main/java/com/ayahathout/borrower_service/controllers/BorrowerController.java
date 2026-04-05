package com.ayahathout.borrower_service.controllers;

import com.ayahathout.borrower_service.dtos.BorrowerCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowerResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowerUpdateDTO;
import com.ayahathout.borrower_service.services.interfaces.BorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/borrowers")
@RequiredArgsConstructor
public class BorrowerController {
    private final BorrowerService borrowerService;

    @PostMapping
    public ResponseEntity<BorrowerResponseDTO> createBorrower(@Valid @RequestBody BorrowerCreateDTO borrowerCreateDTO) {
        BorrowerResponseDTO retBorrower = borrowerService.createBorrower(borrowerCreateDTO);
        return ResponseEntity.created(URI.create("/borrowers/" + retBorrower.id())).body(retBorrower);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> updateBorrower(@PathVariable Long id, @Valid @RequestBody BorrowerUpdateDTO borrowerUpdateDTO) {
        BorrowerResponseDTO borrower = borrowerService.updateBorrower(id, borrowerUpdateDTO);
        return ResponseEntity.ok(borrower);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BorrowerResponseDTO>> getAllBorrowers() {
        List<BorrowerResponseDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> getBorrower(@PathVariable Long id) {
        BorrowerResponseDTO borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(borrower);
    }
}