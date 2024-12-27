package com.devteria.identity_service.validator.impl;

import com.devteria.identity_service.validator.BirthConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthValidatorImpl implements ConstraintValidator<BirthConstraint, LocalDate> {

    private int min;
    @Override
    public void initialize(BirthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        long years= ChronoUnit.YEARS.between(value,LocalDate.now());
        return years>=min;
    }
}
