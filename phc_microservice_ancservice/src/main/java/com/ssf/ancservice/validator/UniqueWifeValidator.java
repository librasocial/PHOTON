package com.ssf.ancservice.validator;

import com.ssf.ancservice.repository.IANCServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueWifeValidator implements ConstraintValidator<UniqueWife, String> {

    @Autowired
    private IANCServiceRepository repository;

    @Override
    public boolean isValid(String wifeId, ConstraintValidatorContext constraintValidatorContext) {
         return wifeId!=null && !repository.existByWifeId(wifeId);
    }
}
