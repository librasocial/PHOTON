package com.ssf.ancservice.validator;

import com.ssf.ancservice.repository.IANCServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueHusbandValidator implements ConstraintValidator<UniqueHusband, String> {
    @Autowired
    private IANCServiceRepository repository;

    @Override
    public void initialize(UniqueHusband constraintAnnotation) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(String husbandId, ConstraintValidatorContext constraintValidatorContext) {
        return husbandId!=null && !repository.existByHusbandId(husbandId);
    }
}
