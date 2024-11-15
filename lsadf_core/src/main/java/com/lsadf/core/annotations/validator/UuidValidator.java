package com.lsadf.core.annotations.validator;

import com.lsadf.core.annotations.Uuid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UuidValidator implements ConstraintValidator<Uuid, String> {

    private boolean nullable;

    @Override
    public void initialize(Uuid constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Check if the value is a valid UUID
        if (value == null) {
            return nullable;
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
