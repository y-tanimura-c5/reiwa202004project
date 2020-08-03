package com.cfiv.sysdev.rrs.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.cfiv.sysdev.rrs.annotation.UsernameUnused;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.service.UserService;

public class UsernameUnusedValidator implements ConstraintValidator<UsernameUnused, String> {

    @Autowired
    UserService userService;

    public void initialize(UsernameUnused constraintAnnotation) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {

        Account account = userService.findByUsername(value);

        if(account == null){
            return true;
        }
        return false;
    }
}