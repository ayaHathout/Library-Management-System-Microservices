package com.ayahathout.book_service.services.impls;

import com.ayahathout.book_service.dtos.BookCreateDTO;
import com.ayahathout.book_service.dtos.BookResponseDTO;
import com.ayahathout.book_service.dtos.BookUpdateDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAllWithDetails().stream().map(bookMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<BookResponseDTO> getBookById(Long id) {
        return bookRepository.findByIdWithDetails(id).map(bookMapper::toResponseDTO);
    }

    @Override
    public BookResponseDTO createBook(BookCreateDTO bookCreateDTO) {
        Book book = bookMapper.toEntity(bookCreateDTO);

        // Get the publisher
        Publisher publisher = publisherRepository.findById(bookCreateDTO.publisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + bookCreateDTO.publisherId()));

        // Get the authors
        List<Author> authors = authorRepository.findAllById(bookCreateDTO.authorIds());
        if (authors.size() != bookCreateDTO.authorIds().size()) {
            throw new RuntimeException("Some authors not found!");
        }

        // Get the categories
        List<Category> categories = categoryRepository.findAllById(bookCreateDTO.categoryIds());
        if (categories.size() != bookCreateDTO.categoryIds().size()) {
            throw new RuntimeException("Some categories not found!");
        }

        // To make availableCopies = totalCopies
        book.setAvailableCopies(book.getTotalCopies());

        book.setPublisher(publisher);
        book.setAuthors(new HashSet<>(authors));
        book.setCategories(new HashSet<>(categories));

        return bookMapper.toResponseDTO(bookRepository.save(book));
    }

    @Override
    public Optional<BookResponseDTO> updateBook(Long id, BookUpdateDTO updatedBookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    if (updatedBookDTO.title() != null) {
                        book.setTitle(updatedBookDTO.title());
                    }
                    if (updatedBookDTO.isbn() != null) {
                        book.setIsbn(updatedBookDTO.isbn());
                    }
                    if (updatedBookDTO.language() != null) {
                        book.setLanguage(updatedBookDTO.language());
                    }
                    if (updatedBookDTO.edition() != null) {
                        book.setEdition(updatedBookDTO.edition());
                    }
                    if (updatedBookDTO.publicationYear() != null) {
                        book.setPublicationYear(updatedBookDTO.publicationYear());
                    }
                    if (updatedBookDTO.summary() != null) {
                        book.setSummary(updatedBookDTO.summary());
                    }
                    if (updatedBookDTO.coverImage() != null) {
                        book.setCoverImage(updatedBookDTO.coverImage());
                    }

                    // Handle the total copies
                    if (updatedBookDTO.totalCopies() != null) {
                        Long currentTotal = book.getTotalCopies();
                        Long newTotal = updatedBookDTO.totalCopies();
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

                    if (updatedBookDTO.availableCopies() != null) {
                        if (updatedBookDTO.availableCopies() > book.getTotalCopies()) {
                            throw new RuntimeException("Available copies can't be greater than the total copies");
                        }
                        book.setAvailableCopies(updatedBookDTO.availableCopies());
                    }

                    if (updatedBookDTO.publisherId() != null) {
                        Publisher publisher = publisherRepository.findById(updatedBookDTO.publisherId())
                                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + updatedBookDTO.publisherId()));
                        book.setPublisher(publisher);
                    }

                    if (updatedBookDTO.authorIds() != null && !updatedBookDTO.authorIds().isEmpty()) {
                        List<Author> authors = authorRepository.findAllById(updatedBookDTO.authorIds());
                        if (authors.size() != updatedBookDTO.authorIds().size()) {
                            throw new RuntimeException("Some authors not found!");
                        }
                        book.setAuthors(new HashSet<>(authors));
                    }

                    if (updatedBookDTO.categoryIds() != null && !updatedBookDTO.categoryIds().isEmpty()) {
                        List<Category> categories = categoryRepository.findAllById(updatedBookDTO.categoryIds());
                        if (categories.size() != updatedBookDTO.categoryIds().size()) {
                            throw new RuntimeException("Some categories not found!");
                        }
                        book.setCategories(new HashSet<>(categories));
                    }

                    return bookRepository.save(book);
                }).map(bookMapper::toResponseDTO);
    }

    @Override
    public Optional<BookResponseDTO> deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    BookResponseDTO dto = bookMapper.toResponseDTO(book);
                    bookRepository.delete(book);
                    return dto;
                });
    }
}
