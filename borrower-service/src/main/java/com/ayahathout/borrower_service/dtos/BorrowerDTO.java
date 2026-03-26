package com.example.libraryManagementSystem.dtos;

public record BorrowerDTO(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address
) {}
