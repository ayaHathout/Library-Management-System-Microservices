package com.ayahathout.book_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherCreateDTO implements Serializable {
    @NotBlank(message = "Publisher name cannot be empty")
    private String name;

    @NotBlank(message = "Publisher phone cannot be empty")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
}
