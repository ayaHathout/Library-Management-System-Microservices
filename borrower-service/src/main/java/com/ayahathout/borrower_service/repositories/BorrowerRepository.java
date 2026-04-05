package com.ayahathout.borrower_service.repositories;

import com.ayahathout.borrower_service.models.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
}