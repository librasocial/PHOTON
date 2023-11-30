package com.ssf.labtest.validators;

import com.ssf.labtest.constants.ValidatorConstants;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {
    StringBuilder message = null;

    private boolean required;

    private ValidateEnum constraintAnnotation;

    @Override
    public void initialize(ValidateEnum constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;
        message = new StringBuilder();
        if (value != null) {
            Object[] enumValues = this.constraintAnnotation.enumClass().getEnumConstants();

            if (enumValues != null) {
                for (Object enumValue : enumValues) {
                    if (StringUtils.deleteWhitespace(value).equalsIgnoreCase(enumValue.toString())) {
                        isValid = true;
                        break;
                    }
                }
            }
        } else if (required) {
            message.append(ValidatorConstants.ENUM_EMPTY);
            errorMessage(context, message);
        } else
            isValid = true;
        return isValid;
    }

    private void errorMessage(ConstraintValidatorContext context, StringBuilder error) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(error.toString()).addConstraintViolation();
    }

}
