package com.cfiv.sysdev.rrs.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cfiv.sysdev.rrs.validator.EmployeeCodeUsedValidator;

@Documented
@Constraint(validatedBy = {EmployeeCodeUsedValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
public @interface EmployeeCodeUsed {

    String message() default "{com.cfiv.sysdev.rrs.annotation.EmployeeCodeUsed.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        EmployeeCodeUsed[] value();
    }
}