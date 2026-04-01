package com.ayahathout.borrower_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerUpdateDTO implements Serializable {
    @Size(max = 255, message = "First name cannot exceed 255 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "First name cannot be empty or blank")
    private String firstName;

    @Size(max = 255, message = "Last name cannot exceed 255 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Last name cannot be empty or blank")
    private String lastName;

    @Email(message = "Invalid email format")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Invalid phone number")
    private String phone;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Address cannot be empty or blank")
    private String address;
}