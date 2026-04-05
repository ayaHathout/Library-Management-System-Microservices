package com.ayahathout.borrower_service.repositories;

import com.ayahathout.borrower_service.models.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
}