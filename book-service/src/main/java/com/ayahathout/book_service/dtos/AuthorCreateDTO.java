package com.ayahathout.book_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateDTO implements Serializable {
    @NotBlank(message = "Author first name cannot be empty")
    private String firstName;

    private String lastName;
    private String bio;
}
