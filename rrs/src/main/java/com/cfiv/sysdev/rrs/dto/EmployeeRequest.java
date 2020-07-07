package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import com.cfiv.sysdev.rrs.entity.Employee;

import lombok.Data;

@Data
public class EmployeeRequest implements Serializable {
    public EmployeeRequest(String _id, String _companyID, String _employeeID, String _employeeFName,
            String _hireYM, String _adoptCode, String _supportCode, String _employCode) {
        setId(_id);
        setCompanyID(_companyID);
        setEmployeeID(_employeeID);
        setEmployeeFName(_employeeFName);
        setHireYM(_hireYM);
        setAdoptCode(_adoptCode);
        setSupportCode(_supportCode);
        setEmployCode(_employCode);
    }

    public EmployeeRequest() {
    }

    /**
     * ID
     */
    private String id;

    /**
     * Šé‹ÆƒR[ƒh
     */
    private String companyID;

    /**
     * Ğˆõ”Ô†
     */
    private String employeeID;

    /**
     * ]‹Æˆõ–¼š
     */
    private String employeeFName;

    /**
     * “üĞ”NŒ
     */
    private String hireYM;

    /**
     * Ì—pí•Ê
     */
    private String adoptCode;

    /**
     * •}—{—L–³
     */
    private String supportCode;

    /**
     * A‹Æí•Ê
     */
    private String employCode;

    /**
     * ]‹Æˆõî•ñ‚ÌŒ^•ÏŠ·(EmployeeRequest¨Employee)
     * @return ]‹Æˆõî•ñ(Employee)
     */
    public Employee toEmployee() {
        Employee employee = new Employee();

        employee.setCompanyIDFromString(getCompanyID());
        employee.setEmployeeID(getEmployeeID());
        employee.setEmployeeFName(getEmployeeFName());
        employee.setHireYM(getHireYM());
        employee.setAdoptCodeFromString(getAdoptCode());
        employee.setSupportCodeFromString(getSupportCode());
        employee.setEmployCodeFromString(getEmployCode());

        return employee;
    }
}
