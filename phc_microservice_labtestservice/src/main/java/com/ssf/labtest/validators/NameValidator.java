package com.ssf.labtest.validators;

import com.ssf.labtest.constants.ValidatorConstants;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidateName, String> {

    boolean isValid = true;
    StringBuilder message = null;

    private boolean required;

    @Override
    public void initialize(ValidateName constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        message = new StringBuilder();
        if (StringUtils.isNotEmpty(name)) {
            isValid = true;
        } else if (required) {
            message.append(ValidatorConstants.NAME_EMPTY);
            errorMessage(context, message);
            return isValid;
        }
        return isValid;
    }

    private void errorMessage(ConstraintValidatorContext context, StringBuilder error) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(error.toString()).addConstraintViolation();
        isValid = false;
    }

}
