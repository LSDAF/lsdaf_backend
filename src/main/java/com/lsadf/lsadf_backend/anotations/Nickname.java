package com.lsadf.lsadf_backend.anotations;

import com.lsadf.lsadf_backend.anotations.impl.NicknameImpl;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NicknameImpl.class)
@Documented
public @interface Nickname {
}
