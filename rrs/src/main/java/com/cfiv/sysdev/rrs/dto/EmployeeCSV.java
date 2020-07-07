package com.cfiv.sysdev.rrs.dto;

import com.cfiv.sysdev.rrs.entity.Employee;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;

/**
 * 従業員情報 Entity(CSV用)
 */
@Data
public class EmployeeCSV {

    /**
     * 企業コード
     */
    @CsvBindByName(column = "企業コード", required = true)
    private String companyID;

    /**
     * 社員番号
     */
    @CsvBindByName(column = "従業員番号", required = true)
    private String employeeID;

    /**
     * 従業員名字
     */
    @CsvBindByName(column = "従業員名字", required = true)
    private String employeeFName;

    /**
     * 入社年月
     */
    @CsvBindByName(column = "入社年月", required = true)
    private String hireYM;

    /**
     * 採用種別
     */
    @CsvBindByName(column = "採用種別", required = true)
    private String adoptCode;

    /**
     * 扶養有無
     */
    @CsvBindByName(column = "扶養有無", required = true)
    private String supportCode;


    private boolean result = true;

    private String reason = "";

    public void check() {
        checkCompanyID();
        checkEmployeeID();
        checkEmployeeFName();
        checkHireYM();
        checkAdoptCode();
        checkSupportCode();
    }

    private void checkCompanyID() {
        // 数字のみか
        if (!companyID.matches("^[0-9]*$")) {
            result = false;
            reason += "企業コードに数字以外の文字が含まれています。";
        }
    }

    private void checkEmployeeID() {
        // 50文字以内か
        if (employeeID.length() > 50) {
            result = false;
            reason += "従業員番号が50文字を超えています。";
        }

        // 数字、アルファベット、記号のみか
        if (!employeeID.matches("^[a-zA-Z0-9!-/:-@\\[-`{-~]*$")) {
            result = false;
            reason += "従業員番号に数字、アルファベット、記号以外の文字が含まれています。";
        }
    }

    private void checkEmployeeFName() {
        // 20文字以内か
        if (employeeFName.length() > 20) {
            result = false;
            reason += "従業員名字が20文字を超えています。";
        }

        // 全角カタカナのみか
        if (!employeeFName.matches("^[ァ-ンヴー]*$")) {
            result = false;
            reason += "従業員名字に全角カタカナ以外の文字が含まれています。";
        }
    }

    private void checkHireYM() {
        // 6文字か
        if (hireYM.length() != 6) {
            result = false;
            reason += "入社年月が年(4桁)＋月(2桁)の形式になっていません。";
        }

        // 数字のみか
        if (!hireYM.matches("^[0-9]*$")) {
            result = false;
            reason += "入社年月に数字以外の文字が含まれています。";
        }
    }

    private void checkAdoptCode() {
        // 数字のみか
        try {
            int code = Integer.parseInt(adoptCode);
            if (code != 0 && code != 1) {
                result = false;
                reason += "採用種別に0または1以外の文字が設定されています。";
            }
        }
        catch (NumberFormatException e) {
            result = false;
            reason += "採用種別に0または1以外の文字が設定されています。";
        }
    }

    private void checkSupportCode() {
        // 数字のみか
        try {
            int code = Integer.parseInt(supportCode);
            if (code != 0 && code != 1) {
                result = false;
                reason += "扶養有無に0または1以外の文字が設定されています。";
            }
        }
        catch (NumberFormatException e) {
            result = false;
            reason += "扶養有無に0または1以外の文字が設定されています。";
        }
    }

    /**
     * クラスの文字列表現
     */
    public String toString() {
        return "企業コード = " + companyID + ", 従業員番号 = " + employeeID + ", 従業員名字 = " + employeeFName +
                ", 入社年月 = " + hireYM + ", 採用種別 = " + adoptCode + ", 扶養有無 = " + supportCode;
    }

    /**
     * 従業員情報の型変換(EmployeeCSV→Employee)
     * @return 従業員情報(Employee)
     */
    public Employee toEmployee() {
        Employee employee = new Employee();

        employee.setCompanyIDFromString(getCompanyID());
        employee.setEmployeeID(getEmployeeID());
        employee.setEmployeeFName(getEmployeeFName());
        employee.setHireYM(getHireYM());
        employee.setAdoptCodeFromString(getAdoptCode());
        employee.setSupportCodeFromString(getSupportCode());

        return employee;
    }
}