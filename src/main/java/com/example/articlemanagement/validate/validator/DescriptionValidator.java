package com.example.articlemanagement.validate.validator;

import com.example.articlemanagement.validate.DescriptionContraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DescriptionValidator implements
        ConstraintValidator<DescriptionContraint, String> {

    private static final Integer MAX_NUMBER_OF_CHARACTERS = 4000;
    private static final Integer MAX_SIZE_IN_KB = 4 * 1024;

    @Override
    public void initialize(DescriptionContraint descriptionContraint) {
    }

    @Override
    public boolean isValid(String description,
                           ConstraintValidatorContext cxt) {
        return description.length() <= MAX_NUMBER_OF_CHARACTERS && description.getBytes().length <= MAX_SIZE_IN_KB;
    }
}