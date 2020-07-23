package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cfiv.sysdev.rrs.entity.Employee;

import lombok.Data;

@Data
public class EmployeeRequest implements Serializable {
    public EmployeeRequest(String _id, String _companyID, String _employeeCode, String _employeeFName,
            Date _hireYM, String _adoptCode, String _supportCode, String _employCode) {
        setId(_id);
        setCompanyID(_companyID);
        setEmployeeCode(_employeeCode);
        setEmployeeFName(_employeeFName);
        setHireYMDate(_hireYM);
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
     * 従業員番号
     */
    private String employeeCode;

    /**
     * 従業員名字
     */
    private String employeeFName;

    /**
     * 入社年月
     */
    private Date hireYMDate;

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
     * 従業員情報の型変換(EmployeeRequest→Employee)
     * @return 従業員情報(Employee)
     */
    public Employee toEmployee() {
        Employee employee = new Employee();

        employee.setCompanyIDFromString(getCompanyID());
        employee.setEmployeeCode(getEmployeeCode());
        employee.setEmployeeFName(getEmployeeFName());
        employee.setHireYM(getHireYMDate());
        employee.setAdoptCodeFromString(getAdoptCode());
        employee.setSupportCodeFromString(getSupportCode());
        employee.setEmployCodeFromString(getEmployCode());

        return employee;
    }

    /**
     * 文字列形式の入社年月
     * @return 入社年月文字列
     */
    public String getHireYM() {
        if (hireYMDate == null) {
            return "";
        }

        int year = 0;
        try {
            DateFormat df = new SimpleDateFormat("yyyyMM");
            int hire = Integer.parseInt(df.format(hireYMDate));
            int now = Integer.parseInt(df.format(new Date()));
            year = (now - hire) / 100;
        }
        catch (NumberFormatException e) {
        }

        return new SimpleDateFormat("yyyy年MM月").format(hireYMDate) + "(" + year + "年勤続)";
    }

    /**
     * 文字列形式の入社年月
     * @return 入社年月文字列
     */
    public String getHireYMShort() {
        return new SimpleDateFormat("yyyy/MM").format(hireYMDate);
    }
}
