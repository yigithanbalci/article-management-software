package com.example.articlemanagement.validate;

import com.example.articlemanagement.validate.validator.DescriptionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DescriptionValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DescriptionContraint {
    String message() default "Description limit exceeded";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}