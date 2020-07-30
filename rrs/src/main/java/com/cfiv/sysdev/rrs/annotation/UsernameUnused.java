package com.cfiv.sysdev.rrs.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cfiv.sysdev.rrs.validator.UsernameUnusedValidator;

@Documented
@Constraint(validatedBy = {UsernameUnusedValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface UsernameUnused {

    String message() default "{com.cfiv.sysdev.rrs.annotation.UsernameUnused.message}";;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        UsernameUnused[] value();
    }
}