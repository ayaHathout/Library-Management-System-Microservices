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
public class BookCreateDTO implements Serializable {
    @NotBlank(message = "Book title cannot be empty")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Book isbn cannot be empty")
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Invalid ISBN")
    private String isbn;

    @NotBlank(message = "Book edition cannot be empty")
    private String edition;

    @NotNull(message = "Publication year is required")
    @Min(value = 1500, message = "Publication year cannot be earlier than 1500")
    @MaxCurrentYear(message = "Publication year cannot be in the future")
    private Integer publicationYear;

    private Language language = Language.ARABIC;
    private String summary;
    private String coverImage;

    @Min(value = 1, message = "Total copies must be at least 1")
    private Long totalCopies = 1L;

    @NotNull(message = "Publisher is required")
    private Long publisherId;

    @NotEmpty(message = "At least one author is required")
    private Set<Long> authorIds;

    private Set<Long> categoryIds;
}
