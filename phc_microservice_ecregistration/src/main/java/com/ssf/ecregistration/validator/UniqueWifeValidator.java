package com.ssf.ecregistration.validator;

import com.ssf.ecregistration.repository.IEligibleRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueWifeValidator implements ConstraintValidator<UniqueWife, String> {

    @Autowired
    private IEligibleRegistrationRepository repository;

    @Override
    public boolean isValid(String wifeId, ConstraintValidatorContext constraintValidatorContext) {
         return wifeId!=null && !repository.existByWifeId(wifeId);
    }
}
