package com.lsadf.core.annotations.validator;

import com.lsadf.core.annotations.StageConsistency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldComparisonValidator implements ConstraintValidator<StageConsistency, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(StageConsistency constraintAnnotation) {
        this.firstField = String.valueOf(constraintAnnotation.currentStageField());
        this.secondField = String.valueOf(constraintAnnotation.maxStageField());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object firstFieldValue = new BeanWrapperImpl(value).getPropertyValue(firstField);
            Object secondFieldValue = new BeanWrapperImpl(value).getPropertyValue(secondField);

            if (firstFieldValue == null || secondFieldValue == null) {
                return true;  // Null values can be considered valid or you can adjust the logic here
            }

            if (firstFieldValue instanceof Comparable c1 && secondFieldValue instanceof Comparable c2) {
                return c1.compareTo(c2) <= 0;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
