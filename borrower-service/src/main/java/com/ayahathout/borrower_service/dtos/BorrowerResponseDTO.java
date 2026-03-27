package com.ayahathout.borrower_service.dtos;

public record BorrowerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {
}
