package org.ptithcm2021.hr_management.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StringNumberValidator implements ConstraintValidator<StringNumberConstraint, String> {
    private StringNumberConstraint.NumberType numberType;

    @Override
    public void initialize(StringNumberConstraint constraintAnnotation) {
        this.numberType = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return false;
        }

        switch (numberType) {
            case CCCD:
                return s.matches("\\d{12}");
            case PHONE:
                return s.matches("0\\d{9}");
            default:
                return false;
        }
    }
}
