package com.ayahathout.book_service.dtos;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherUpdateDTO implements Serializable {
    private String name;

    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
}
