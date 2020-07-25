package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.cfiv.sysdev.rrs.Const;
import com.cfiv.sysdev.rrs.entity.Employee;

import lombok.Data;

@Data
public class EmployeeRequest implements Serializable {

    public EmployeeRequest() {
        employItems = Arrays.asList(Const.EMPLOY_NAMES);
    }

    public EmployeeRequest(String _id, String _companyID, String _employeeCode, String _employeeFName,
            Date _hireYM, int _adoptCode, int _supportCode, int _employCode) {
        this();

        setId(_id);
        setCompanyID(_companyID);
        setEmployeeCode(_employeeCode);
        setEmployeeFName(_employeeFName);
        setHireYMDate(_hireYM);
        setAdoptCode(_adoptCode);
        setSupportCode(_supportCode);
        setEmployCode(_employCode);
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
     * 採用種別コード
     */
    private int adoptCode;

    /**
     * 扶養有無コード
     */
    private int supportCode;

    /**
     * 就業種別コード
     */
    private int employCode;

    /**
     * 就業種別ラジオボタン表示内容リスト
     */
    private List<String> employItems;

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
        employee.setAdoptCode(getAdoptCode());
        employee.setSupportCode(getSupportCode());
        employee.setEmployCode(getEmployCode());

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

    /**
     * 文字列形式の採用種別
     * @return 採用種別文字列
     */
    public String getAdopt() {
        if (id == null) {
            return "";
        }

        try {
            return Const.ADOPT_NAMES[adoptCode];
        }
        catch (Exception e) {
            return Const.ADOPT_NAMES[0];
        }
    }

    /**
     * 文字列形式の扶養有無
     * @return 扶養有無文字列
     */
    public String getSupport() {
        if (id == null) {
            return "";
        }

        try {
            return Const.SUPPORT_NAMES[supportCode];
        }
        catch (Exception e) {
            return Const.SUPPORT_NAMES[0];
        }
    }

    /**
     * 文字列形式の就業種別
     * @return 就業種別文字列
     */
    public String getEmploy() {
        if (id == null) {
            return "";
        }

        try {
            return Const.EMPLOY_NAMES[employCode];
        }
        catch (Exception e) {
            return Const.EMPLOY_NAMES[0];
        }
    }
}
