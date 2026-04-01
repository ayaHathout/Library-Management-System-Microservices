package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.BookCreateDTO;
import com.ayahathout.book_service.dtos.BookResponseDTO;
import com.ayahathout.book_service.dtos.BookUpdateDTO;
import com.ayahathout.common_lib.exceptions.BadRequestException;
import com.ayahathout.common_lib.exceptions.ResourceNotFoundException;
import com.ayahathout.book_service.mappers.BookMapper;
import com.ayahathout.book_service.models.Author;
import com.ayahathout.book_service.models.Book;
import com.ayahathout.book_service.models.Category;
import com.ayahathout.book_service.models.Publisher;
import com.ayahathout.book_service.repositories.AuthorRepository;
import com.ayahathout.book_service.repositories.BookRepository;
import com.ayahathout.book_service.repositories.CategoryRepository;
import com.ayahathout.book_service.repositories.PublisherRepository;
import com.ayahathout.book_service.services.interfaces.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final PublisherRepository publisherRepository;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public List<BookResponseDTO> getAllBooks() {
        log.info("Fetching all books");

        List<BookResponseDTO> books = bookRepository.findAllWithDetails().stream().map(bookMapper::toResponseDTO).collect(Collectors.toList());

        log.info("Books fetched successfully {}", books.size());
        return books;
    }

    @Transactional(readOnly = true)
    @Override
    public BookResponseDTO getBookById(Long id) {
        log.info("Fetching book with id {}", id);

        BookResponseDTO book = bookRepository.findByIdWithDetails(id)
                .map(bookMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Book not found with id {}", id);
                    return new ResourceNotFoundException("Book not found with id " + id);
                });

        log.info("Book fetched successfully with id {}", id);
        return book;
    }

    @Override
    public BookResponseDTO createBook(BookCreateDTO bookCreateDTO) {
        log.info("Creating new book");

        Book book = bookMapper.toEntity(bookCreateDTO);

        // Validate ISBN ==> Must be unique
        if (bookRepository.existsByIsbn(bookCreateDTO.getIsbn())) {
            log.error("Book already exists with ISBN {} for book with id {}", bookCreateDTO.getIsbn(), book.getId());
            throw new BadRequestException("Book already exists with ISBN " + bookCreateDTO.getIsbn());
        }

        // Get the publisher
        Publisher publisher = publisherRepository.findById(bookCreateDTO.getPublisherId())
                .orElseThrow(() -> {
                    log.error("Publisher not found with id {}", bookCreateDTO.getPublisherId());
                    return new ResourceNotFoundException("Publisher not found with id " + bookCreateDTO.getPublisherId());
                });

        // Get the authors
        List<Author> authors = authorRepository.findAllById(bookCreateDTO.getAuthorIds());
        if (authors.size() != bookCreateDTO.getAuthorIds().size()) {
            log.error("Some authors not found. Requested IDs: {}, Found IDs: {}", bookCreateDTO.getAuthorIds(), authors.stream().map(Author::getId).toList());
            throw new ResourceNotFoundException("Some authors not found!");
        }

        // Get the categories
        List<Category> categories = categoryRepository.findAllById(bookCreateDTO.getCategoryIds());
        if (categories.size() != bookCreateDTO.getCategoryIds().size()) {
            log.error("Some categories do not exist");
            throw new ResourceNotFoundException("Some categories not found!");
        }

        // To make availableCopies = totalCopies
        book.setAvailableCopies(book.getTotalCopies());

        book.setPublisher(publisher);
        book.setAuthors(new HashSet<>(authors));
        book.setCategories(new HashSet<>(categories));

        Book savedBook = bookRepository.save(book);

        log.info("Book created successfully with id {}", savedBook.getId());
        return bookMapper.toResponseDTO(savedBook);
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO) {
        log.info("Updating book with id {}", id);

        BookResponseDTO updatedBook = bookRepository.findById(id)
                .map(book -> {
                    // Validate ISBN ==> Must be unique
                    if (bookUpdateDTO.getIsbn() != null && !bookUpdateDTO.getIsbn().equals(book.getIsbn()) && bookRepository.existsByIsbn(bookUpdateDTO.getIsbn())) {
                        log.error("Book already exists with ISBN {}", bookUpdateDTO.getIsbn());
                        throw new BadRequestException("Book already exists with ISBN " + bookUpdateDTO.getIsbn());
                    }

                    if (bookUpdateDTO.getTitle() != null) {
                        book.setTitle(bookUpdateDTO.getTitle());
                    }
                    if (bookUpdateDTO.getIsbn() != null) {
                        book.setIsbn(bookUpdateDTO.getIsbn());
                    }
                    if (bookUpdateDTO.getLanguage() != null) {
                        book.setLanguage(bookUpdateDTO.getLanguage());
                    }
                    if (bookUpdateDTO.getEdition() != null) {
                        book.setEdition(bookUpdateDTO.getEdition());
                    }
                    if (bookUpdateDTO.getPublicationYear() != null) {
                        book.setPublicationYear(bookUpdateDTO.getPublicationYear());
                    }
                    if (bookUpdateDTO.getSummary() != null) {
                        book.setSummary(bookUpdateDTO.getSummary());
                    }
                    if (bookUpdateDTO.getCoverImage() != null) {
                        book.setCoverImage(bookUpdateDTO.getCoverImage());
                    }

                    // Handle the total copies
                    if (bookUpdateDTO.getTotalCopies() != null) {
                        Long currentTotal = book.getTotalCopies();
                        Long newTotal = bookUpdateDTO.getTotalCopies();
                        Long currentAvailable = book.getAvailableCopies();

                        book.setTotalCopies(newTotal);

                        if (newTotal > currentTotal) {
                            book.setAvailableCopies(currentAvailable + (newTotal - currentTotal));
                        }
                        else if (newTotal < currentTotal) {
                            Long borrowedCount = currentTotal - currentAvailable;
                            Long newAvailable = Math.max(0, newTotal - borrowedCount);
                            book.setAvailableCopies(newAvailable);
                        }
                    }

                    if (bookUpdateDTO.getAvailableCopies() != null) {
                        if (bookUpdateDTO.getAvailableCopies() > book.getTotalCopies()) {
                            log.error("Available copies can't be greater than the total copies");
                            throw new BadRequestException("Available copies can't be greater than the total copies");
                        }
                        book.setAvailableCopies(bookUpdateDTO.getAvailableCopies());
                    }

                    if (bookUpdateDTO.getPublisherId() != null) {
                        Publisher publisher = publisherRepository.findById(bookUpdateDTO.getPublisherId())
                                .orElseThrow(() -> {
                                    log.error("Publisher not found with id {}", bookUpdateDTO.getPublisherId());
                                    return new ResourceNotFoundException("Publisher not found with id " + bookUpdateDTO.getPublisherId());
                                });
                        book.setPublisher(publisher);
                    }

                    // Validate authors ==> Must have at least one author
                    if (bookUpdateDTO.getAuthorIds() != null && bookUpdateDTO.getAuthorIds().isEmpty()) {
                        log.error("Authors list cannot be empty for book with id {}", id);
                        throw new BadRequestException("Book must have at least one author");
                    }

                    if (bookUpdateDTO.getAuthorIds() != null && !bookUpdateDTO.getAuthorIds().isEmpty()) {
                        List<Author> authors = authorRepository.findAllById(bookUpdateDTO.getAuthorIds());
                        if (authors.size() != bookUpdateDTO.getAuthorIds().size()) {
                            log.error("Some authors not found");
                            throw new ResourceNotFoundException("Some authors not found!");
                        }
                        book.setAuthors(new HashSet<>(authors));
                    }

                    // Validate categories ==> Must have at least one category
                    if (bookUpdateDTO.getCategoryIds() != null && bookUpdateDTO.getCategoryIds().isEmpty()) {
                        log.error("Categories can't be empty");
                        throw new BadRequestException("Book must have at least one category");
                    }

                    if (bookUpdateDTO.getCategoryIds() != null && !bookUpdateDTO.getCategoryIds().isEmpty()) {
                        List<Category> categories = categoryRepository.findAllById(bookUpdateDTO.getCategoryIds());
                        if (categories.size() != bookUpdateDTO.getCategoryIds().size()) {
                            log.error("Some categories not found");
                            throw new ResourceNotFoundException("Some categories not found!");
                        }
                        book.setCategories(new HashSet<>(categories));
                    }

                    return bookRepository.save(book);
                })
                .map(bookMapper::toResponseDTO)
                .orElseThrow(() -> {
                    log.error("Book not found with id {}", id);
                    return new ResourceNotFoundException("Book not found with id " + id);
                });

        log.info("Book updated successfully with id {}", id);
        return updatedBook;
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with id {}", id);

        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id {}", id);
                    return new ResourceNotFoundException("Book not found with id " + id);
                });

        bookRepository.delete(bookToDelete);
        log.info("Book deleted successfully with id {}", id);
    }
}