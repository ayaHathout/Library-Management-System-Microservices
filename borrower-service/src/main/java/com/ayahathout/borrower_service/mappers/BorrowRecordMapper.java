package com.ayahathout.borrower_service.mappers;

import com.ayahathout.borrower_service.dtos.BorrowRecordCreateDTO;
import com.ayahathout.borrower_service.dtos.BorrowRecordResponseDTO;
import com.ayahathout.borrower_service.models.BorrowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {
    BorrowRecord toEntity(BorrowRecordCreateDTO borrowRecordCreateDTO);

    @Mapping(source = "borrower.id", target = "borrowerId")
    BorrowRecordResponseDTO toResponseDTO(BorrowRecord borrowRecord);
}