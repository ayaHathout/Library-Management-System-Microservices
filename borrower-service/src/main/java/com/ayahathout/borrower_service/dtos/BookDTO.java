package com.ayahathout.borrower_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO implements Serializable {
    private Long id;
    private String title;
    private Long availableCopies;
}