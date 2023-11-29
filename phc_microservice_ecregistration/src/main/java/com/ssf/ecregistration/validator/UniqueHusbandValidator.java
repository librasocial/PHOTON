package com.ssf.ecregistration.validator;

import com.ssf.ecregistration.repository.IEligibleRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueHusbandValidator implements ConstraintValidator<UniqueHusband, String> {
    @Autowired
    private IEligibleRegistrationRepository repository;

    @Override
    public void initialize(UniqueHusband constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(String husbandId, ConstraintValidatorContext constraintValidatorContext) {
        return husbandId!=null && !repository.existByHusbandId(husbandId);
    }
}
