package com.ayahathout.borrower_service.mappers;

import com.ayahathout.borrower_service.dtos.BorrowerCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowerResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowerUpdateDTO;
import com.ayahathout.borrower_service.models.Borrower;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowerMapper {
    // For create
    Borrower toEntity(BorrowerCreateDTO borrowerCreateDTO);

    // For update
    Borrower toEntity(BorrowerUpdateDTO borrowerUpdateDTO);

    // For get
    BorrowerResponseDTO toResponseDTO(Borrower borrower);
}
