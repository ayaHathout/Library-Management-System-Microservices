package com.ayahathout.book_service.models;

import com.ayahathout.book_service.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String edition;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    private Language language = Language.ARABIC;

    @Column(length = 500)
    private String summary;

    @Column(name = "cover_image", length = 2048)
    private String coverImage;

    @Column(name = "total_copies")
    private Long totalCopies = 1L;

    @Column(name = "available_copies", nullable = false)
    private Long availableCopies;

    // ------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}
