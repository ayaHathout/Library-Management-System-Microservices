package com.ayahathout.book_service.repositories;

import com.ayahathout.book_service.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("SELECT DISTINCT p FROM Publisher p LEFT JOIN FETCH p.books") // Fetch join to avoid the n + 1 problem
    List<Publisher> findAllWithBooks();
}
