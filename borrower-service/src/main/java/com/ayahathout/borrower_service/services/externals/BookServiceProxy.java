package com.ayahathout.borrower_service.services.externals;

import com.ayahathout.borrower_service.clients.BookServiceClient;
import com.ayahathout.borrower_service.dtos.BookDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceProxy {
    private final BookServiceClient bookServiceClient;

    @CircuitBreaker(name = "bookService", fallbackMethod = "fallbackGetBook")
    @Retry(name = "bookService", fallbackMethod = "fallbackGetBook")
    public BookDTO getBook(Long id) {
        log.info("BookServiceProxy.getBook(id)");

        return bookServiceClient.getBook(id);
    }

    @CircuitBreaker(name = "bookService", fallbackMethod = "fallbackUpdateBook")
    @Retry(name = "bookService", fallbackMethod = "fallbackUpdateBook")
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        log.info("BookServiceProxy.updateBook(id,bookDTO)");

        return bookServiceClient.updateBook(id, bookDTO);
    }

    private BookDTO fallbackGetBook(Long id, Exception e) {
        log.error("Fallback triggered for getBook, id: {}, error: {}", id, e.getMessage());

        return new BookDTO(id, "Unavailable Book", 0L);
    }

    private BookDTO fallbackUpdateBook(Long id, BookDTO bookDTO, Exception e) {
        log.error("Fallback triggered for updateBook, id: {}, error: {}", id, e.getMessage());

        throw new RuntimeException("Book service unavailable for update", e);
    }
}