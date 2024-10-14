package com.lsadf.lsadf_backend.annotations;

import com.lsadf.lsadf_backend.annotations.impl.FieldComparisonValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldComparisonValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StageConsistency {

    String message() default "Provided stages are invalid";

    String currentStageField();

    String maxStageField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
