package com.ayahathout.borrower_service.clients;

import com.ayahathout.borrower_service.dtos.BookDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "book-service", path = "/books")
public interface BookServiceClient {
    @GetMapping("/{id}")
    BookDTO getBook(@PathVariable Long id);

    @PatchMapping("/{id}")
    BookDTO updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO);
}