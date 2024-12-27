package com.devteria.identity_service.validator;

import com.devteria.identity_service.validator.impl.BirthValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BirthValidatorImpl.class})
public @interface BirthConstraint {
    String message() default "{Invalid date of birth}";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
