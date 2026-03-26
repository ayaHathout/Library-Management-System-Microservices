package com.example.libraryManagementSystem.dtos;

public record BorrowerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {
}
