package com.ayahathout.book_service.validations.annotations;

import com.ayahathout.book_service.validations.validators.CurrentYearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrentYearValidator.class)
public @interface MaxCurrentYear {
    String message() default "{validation.maxCurrentYear.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
