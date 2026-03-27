package com.ayahathout.borrower_service.mappers;

import com.ayahathout.borrower_service.dtos.BorrowRecordDTO;
import com.ayahathout.borrower_service.models.BorrowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "borrower.id", target = "borrowerId")
    BorrowRecordDTO toDTO(BorrowRecord borrowRecord);

    @Mapping(source = "bookId", target = "book.id")
    @Mapping(source = "borrowerId", target = "borrower.id")
    BorrowRecord toEntity(BorrowRecordDTO borrowRecordDTO);
}
