package com.cfiv.sysdev.rrs.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cfiv.sysdev.rrs.validator.UnusedValidator;

@Documented
@Constraint(validatedBy = {UnusedValidator.class})
@Target({FIELD}) // ���ڂɑ΂��ăo���f�[�V������������ꍇ��FIELD��I�т܂��B
@Retention(RUNTIME)
public @interface Unused {

    String message() default "{com.cfiv.sysdev.rrs.annotation.Unused.message}";;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        Unused[] value();
    }
}