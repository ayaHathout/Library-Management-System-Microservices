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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        return bookRepository.findAllWithDetails().stream().map(bookMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BookResponseDTO getBookById(Long id) {
        return bookRepository.findByIdWithDetails(id)
                .map(bookMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    @Override
    public BookResponseDTO createBook(BookCreateDTO bookCreateDTO) {
        Book book = bookMapper.toEntity(bookCreateDTO);

        // Validate ISBN ==> Must be unique
        if (bookRepository.existsByIsbn(bookCreateDTO.getIsbn())) {
            throw new BadRequestException("Book already exists with ISBN " + bookCreateDTO.getIsbn());
        }

        // Get the publisher
        Publisher publisher = publisherRepository.findById(bookCreateDTO.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + bookCreateDTO.getPublisherId()));

        // Get the authors
        List<Author> authors = authorRepository.findAllById(bookCreateDTO.getAuthorIds());
        if (authors.size() != bookCreateDTO.getAuthorIds().size()) {
            throw new ResourceNotFoundException("Some authors not found!");
        }

        // Get the categories
        List<Category> categories = categoryRepository.findAllById(bookCreateDTO.getCategoryIds());
        if (categories.size() != bookCreateDTO.getCategoryIds().size()) {
            throw new ResourceNotFoundException("Some categories not found!");
        }

        // To make availableCopies = totalCopies
        book.setAvailableCopies(book.getTotalCopies());

        book.setPublisher(publisher);
        book.setAuthors(new HashSet<>(authors));
        book.setCategories(new HashSet<>(categories));

        return bookMapper.toResponseDTO(bookRepository.save(book));
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    // Validate ISBN ==> Must be unique
                    if (bookUpdateDTO.getIsbn() != null && !bookUpdateDTO.getIsbn().equals(book.getIsbn()) && bookRepository.existsByIsbn(bookUpdateDTO.getIsbn())) {
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
                            throw new BadRequestException("Available copies can't be greater than the total copies");
                        }
                        book.setAvailableCopies(bookUpdateDTO.getAvailableCopies());
                    }

                    if (bookUpdateDTO.getPublisherId() != null) {
                        Publisher publisher = publisherRepository.findById(bookUpdateDTO.getPublisherId())
                                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + bookUpdateDTO.getPublisherId()));
                        book.setPublisher(publisher);
                    }

                    // Validate authors ==> Must have at least one author
                    if (bookUpdateDTO.getAuthorIds() != null && bookUpdateDTO.getAuthorIds().isEmpty()) {
                        throw new BadRequestException("Book must have at least one author");
                    }

                    if (bookUpdateDTO.getAuthorIds() != null && !bookUpdateDTO.getAuthorIds().isEmpty()) {
                        List<Author> authors = authorRepository.findAllById(bookUpdateDTO.getAuthorIds());
                        if (authors.size() != bookUpdateDTO.getAuthorIds().size()) {
                            throw new ResourceNotFoundException("Some authors not found!");
                        }
                        book.setAuthors(new HashSet<>(authors));
                    }

                    // Validate categories ==> Must have at least one category
                    if (bookUpdateDTO.getCategoryIds() != null && bookUpdateDTO.getCategoryIds().isEmpty()) {
                        throw new BadRequestException("Book must have at least one category");
                    }

                    if (bookUpdateDTO.getCategoryIds() != null && !bookUpdateDTO.getCategoryIds().isEmpty()) {
                        List<Category> categories = categoryRepository.findAllById(bookUpdateDTO.getCategoryIds());
                        if (categories.size() != bookUpdateDTO.getCategoryIds().size()) {
                            throw new ResourceNotFoundException("Some categories not found!");
                        }
                        book.setCategories(new HashSet<>(categories));
                    }

                    return bookRepository.save(book);
                })
                .map(bookMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    @Override
    public BookResponseDTO deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    BookResponseDTO dto = bookMapper.toResponseDTO(book);
                    bookRepository.delete(book);
                    return dto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }
}
