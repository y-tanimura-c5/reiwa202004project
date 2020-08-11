package com.cfiv.sysdev.rrs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cfiv.sysdev.rrs.validator.FileRequiredValidator;

/**
 * 従業員情報一括登録CSVファイルバリデーションチェック用アノテーション
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileRequiredValidator.class)
public @interface FileRequired {
    String message() default "{com.cfiv.sysdev.rrs.annotation.FileRequired.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        FileRequired[] value();
    }
}