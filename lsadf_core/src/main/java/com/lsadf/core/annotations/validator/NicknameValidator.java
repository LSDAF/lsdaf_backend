package com.lsadf.core.annotations.validator;

import com.lsadf.core.annotations.Nickname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 16;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // If nickname is null, then should return false
    if (value == null) {
      return false;
    }

    if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
      return false;
    }

    return value.matches("^[a-zA-Z0-9-]*$");
  }
}
