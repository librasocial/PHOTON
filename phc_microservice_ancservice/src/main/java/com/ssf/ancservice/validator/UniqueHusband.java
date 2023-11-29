package com.ssf.ancservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueHusbandValidator.class)
public @interface UniqueHusband {
    String message() default "HusbandId already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
