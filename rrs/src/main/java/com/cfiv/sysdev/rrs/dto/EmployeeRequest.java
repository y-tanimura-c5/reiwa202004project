package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

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
     * 企業コード
     */
    private String companyID;

    /**
     * 社員番号
     */
    private String employeeID;

    /**
     * 従業員名字
     */
    private String employeeFName;

    /**
     * 入社年月
     */
    private String hireYM;

    /**
     * 採用種別
     */
    private String adoptCode;

    /**
     * 扶養有無
     */
    private String supportCode;

    /**
     * 就業種別
     */
    private String employCode;
}
