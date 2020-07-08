package com.cfiv.sysdev.rrs.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.cfiv.sysdev.rrs.annotation.PasswordConfirm;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, Object> {

    private String field1;
    private String field2;
    private String message;

    public void initialize(PasswordConfirm constraintAnnotation) {
        field1 = "password";
        field2 = "passwordCheck";
        message = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object field1Value = beanWrapper.getPropertyValue(field1);
        Object field2Value = beanWrapper.getPropertyValue(field2);

        if (Objects.equals(field1Value, field2Value)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(field2).addConstraintViolation();
            return false;
        }
    }
}