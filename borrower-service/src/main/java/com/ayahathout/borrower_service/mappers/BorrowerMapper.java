package com.ayahathout.borrower_service.mappers;

import com.ayahathout.borrower_service.dtos.BorrowerCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowerResponseDTO;
import com.ayahathout.borrower_service.dtos.BorrowerUpdateDTO;
import com.ayahathout.borrower_service.models.Borrower;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BorrowerMapper {
    Borrower toEntity(BorrowerCreateDTO borrowerCreateDTO);

    BorrowerResponseDTO toResponseDTO(Borrower borrower);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(BorrowerUpdateDTO borrowerUpdateDTO, @MappingTarget Borrower borrower);
}