package com.ayahathout.book_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid phone number")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
}
