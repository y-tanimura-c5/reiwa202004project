package com.cfiv.sysdev.rrs.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.cfiv.sysdev.rrs.annotation.Unused;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.service.UserAccountService;

public class UnusedValidator implements ConstraintValidator<Unused, String> {

    @Autowired
    UserAccountService userAccountService;

    public void initialize(Unused constraintAnnotation) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {

        Account account = userAccountService.findByUsername(value);

        if(account == null){
            return true;
        }
        return false;
    }
}