package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class InterviewSearchRequest implements Serializable {
    public InterviewSearchRequest(String _companyID, String _companyName, String _employeeID, String _employeeFName,
            String _hireYM, String _adoptCode, String _supportCode, String _employCode) {
        setCompanyID(_companyID);
        setCompanyName(_companyName);
        setEmployeeID(_employeeID);
        setEmployeeFName(_employeeFName);
        setHireYM(_hireYM);
        setAdoptCode(_adoptCode);
        setSupportCode(_supportCode);
        setEmployCode(_employCode);
    }

    public InterviewSearchRequest() {
    }

    /**
     * 企業コード
     */
    private String companyID;

    /**
     * 企業名称
     */
    private String companyName;

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

    /**
     * 企業コードLong値
     * @param cs 企業コード文字列
     */
    public Long getCompanyIDLong() {
        try {
            return Long.parseLong(companyID);
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }
}
