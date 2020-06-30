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
        field1 = "password"; // 下記のisValidで使うので、ここでメンバ変数に項目名を入れておいてください。
        field2 = "passwordCheck"; // ここも同じ
        message = constraintAnnotation.message(); // Confirmクラスのmessage()です。isValidで使用します。
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object field1Value = beanWrapper.getPropertyValue(field1);
        Object field2Value = beanWrapper.getPropertyValue(field2);

        if (Objects.equals(field1Value, field2Value)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message) // このようにmessageの設定を入れないと、エラー内容が出力されません。
                    .addPropertyNode(field2).addConstraintViolation(); // field2の箇所にエラー内容が出力されるようにしています。
            return false;
        }
    }
}