package com.cfiv.sysdev.rrs.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.cfiv.sysdev.rrs.annotation.EmployeeCodeUsed;
import com.cfiv.sysdev.rrs.request.EmployeeRequest;
import com.cfiv.sysdev.rrs.service.EmployeeService;

public class EmployeeCodeUsedValidator implements ConstraintValidator<EmployeeCodeUsed, Object> {
    private String field1;
    private String field2;
    private String message;

    @Autowired
    EmployeeService employeeService;

    public void initialize(EmployeeCodeUsed constraintAnnotation) {
        field1 = "companyID";
        field2 = "employeeCode";
        message = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String companyID = (String) beanWrapper.getPropertyValue(field1);
        String employeeCode = (String) beanWrapper.getPropertyValue(field2);

        EmployeeRequest req = employeeService.findOneRequestFromID(companyID, employeeCode);

        if(req == null){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(field2).addConstraintViolation();
            return false;
        }
        else {
            return true;
        }
    }
}
