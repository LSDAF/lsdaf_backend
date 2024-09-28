package com.lsadf.lsadf_backend.annotations.impl;

import com.lsadf.lsadf_backend.annotations.Nickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
    int MIN_LENGTH = 3;
    int MAX_LENGTH = 16;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            return false;
        }

        return value.matches("^[a-zA-Z0-9-]*$");
    }
}
