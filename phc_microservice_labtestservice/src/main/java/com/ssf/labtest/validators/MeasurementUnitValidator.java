package com.ssf.labtest.validators;

import com.ssf.labtest.constants.Constant;
import com.ssf.labtest.constants.ValidatorConstants;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MeasurementUnitValidator implements ConstraintValidator<ValidateUnitOfMeasurement, String> {
    boolean isValid = true;
    StringBuilder message = null;

    private boolean required;

    @Override
    public void initialize(ValidateUnitOfMeasurement constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String unitOfMeasurement, ConstraintValidatorContext context) {
        message = new StringBuilder();
        if (StringUtils.isNotEmpty(unitOfMeasurement) && Constant.unitsOfMeasurement.contains(unitOfMeasurement)) {
            isValid = true;
        } else if (required) {
            message.append(ValidatorConstants.UNIT_OF_MEASUREMENT_EMPTY);
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
