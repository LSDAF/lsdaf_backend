package com.lsadf.core.annotations;

import com.lsadf.core.annotations.validator.NicknameValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NicknameValidator.class)
@Documented
public @interface Nickname {
  String message() default "Invalid nickname";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};
}
