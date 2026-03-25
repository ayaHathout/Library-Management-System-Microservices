package com.ayahathout.book_service.dtos;

import com.ayahathout.book_service.enums.Language;
import com.ayahathout.book_service.validations.annotations.MaxCurrentYear;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDTO implements Serializable {
    @Size(min = 1, max = 255, message = "Title cannot be empty or exceed 255 characters")
    private String title;

    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Invalid ISBN")
    private String isbn;

    @Size(min = 1, message = "Edition cannot be empty")
    private String edition;

    @Min(value = 1500, message = "Publication year cannot be earlier than 1500")
    @MaxCurrentYear(message = "Publication year cannot be in the future")
    private Integer publicationYear;

    private Language language;
    private String summary;
    private String coverImage;

    @Min(value = 1, message = "Total copies must be at least 1")
    private Long totalCopies;

    private Long availableCopies;
    private Long publisherId;
    private Set<Long> authorIds;
    private Set<Long> categoryIds;
}