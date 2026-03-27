package com.ayahathout.borrower_service.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerCreateDTO implements Serializable {
    @NotBlank(message = "First name cannot be empty")
    @Size(max = 255, message = "First name cannot exceed 255 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 255, message = "Last name cannot exceed 255 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;
}