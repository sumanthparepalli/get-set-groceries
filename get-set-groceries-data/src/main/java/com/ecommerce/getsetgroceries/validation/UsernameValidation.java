package com.ecommerce.getsetgroceries.validation;

import com.ecommerce.getsetgroceries.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidation implements ConstraintValidator<Username, String> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public void initialize(Username constraintAnnotation) {
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context) {
        System.out.println(value + " " + userRepo.existsByUsername(value));
        return !userRepo.existsByUsername(value);
    }
}
