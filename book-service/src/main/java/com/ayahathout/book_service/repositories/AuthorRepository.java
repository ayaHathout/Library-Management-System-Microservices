package com.ayahathout.book_service.repositories;

import com.ayahathout.book_service.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
