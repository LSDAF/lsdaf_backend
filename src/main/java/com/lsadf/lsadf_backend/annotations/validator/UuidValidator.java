package com.lsadf.lsadf_backend.annotations.validator;

import com.lsadf.lsadf_backend.annotations.Uuid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UuidValidator implements ConstraintValidator<Uuid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Check if the value is a valid UUID
        if (value == null) {
            return true;  // Assume null values are handled separately or allowed
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
