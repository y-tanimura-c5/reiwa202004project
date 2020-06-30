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
        field1 = "password"; // ���L��isValid�Ŏg���̂ŁA�����Ń����o�ϐ��ɍ��ږ������Ă����Ă��������B
        field2 = "passwordCheck"; // ����������
        message = constraintAnnotation.message(); // Confirm�N���X��message()�ł��BisValid�Ŏg�p���܂��B
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object field1Value = beanWrapper.getPropertyValue(field1);
        Object field2Value = beanWrapper.getPropertyValue(field2);

        if (Objects.equals(field1Value, field2Value)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message) // ���̂悤��message�̐ݒ�����Ȃ��ƁA�G���[���e���o�͂���܂���B
                    .addPropertyNode(field2).addConstraintViolation(); // field2�̉ӏ��ɃG���[���e���o�͂����悤�ɂ��Ă��܂��B
            return false;
        }
    }
}