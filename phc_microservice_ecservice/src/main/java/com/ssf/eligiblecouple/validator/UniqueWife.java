package com.ssf.eligiblecouple.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueWifeValidator.class)
public @interface UniqueWife {
    String message() default "WifeId already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
