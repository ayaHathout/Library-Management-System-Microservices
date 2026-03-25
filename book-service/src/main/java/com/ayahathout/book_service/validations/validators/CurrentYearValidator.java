package com.ayahathout.book_service.validations.validators;

import com.ayahathout.book_service.validations.annotations.MaxCurrentYear;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class CurrentYearValidator implements ConstraintValidator<MaxCurrentYear, Integer> {
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer == null) {
            return true;
        }

        int currentYear = Year.now().getValue();
        return integer <= currentYear;
    }
}
