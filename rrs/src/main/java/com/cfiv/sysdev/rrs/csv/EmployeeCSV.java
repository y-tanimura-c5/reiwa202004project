package com.cfiv.sysdev.rrs.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.entity.Employee;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;

/**
 * 従業員情報CSV Entity
 */
@Data
public class EmployeeCSV {

    /**
     * 企業コード
     */
    @CsvBindByName(column = "企業コード", required = true)
    private String companyID;

    /**
     * 従業員番号
     */
    @CsvBindByName(column = "従業員番号", required = true)
    private String employeeCode;

    /**
     * 従業員氏名
     */
    @CsvBindByName(column = "従業員氏名", required = true)
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

    /**
     * バリデーションチェック
     * @param checkCompanyID ログインユーザの企業コード
     */
    public void check(String checkCompanyID) {
        checkCompanyID(checkCompanyID);
        checkEmployeeCode();
        checkEmployeeFName();
        checkHireYM();
        checkAdoptCode();
        checkSupportCode();
    }

    /**
     * 企業コードバリデーションチェック
     * @param checkCompanyID ログインユーザの企業コード
     */
    private void checkCompanyID(String checkCompanyID) {
        // 数字のみか
        if (!companyID.matches("^[0-9]*$")) {
            result = false;
            reason += "企業コードに数字以外の文字が含まれています。";
            return;
        }

        if (checkCompanyID != null) {
            Long mcID = Utils.getLongFromString(companyID);
            Long ccID = Utils.getLongFromString(checkCompanyID);
            if (mcID != ccID) {
                result = false;
                reason += "別の企業の従業員情報は登録できません。";
            }
        }
    }

    /**
     * 従業員番号バリデーションチェック
     */
    private void checkEmployeeCode() {
        // 50文字以内か
        if (employeeCode.length() > 50) {
            result = false;
            reason += "従業員番号が50文字を超えています。";
            return;
        }

        // 数字、アルファベット、記号のみか
        if (!employeeCode.matches("^[a-zA-Z0-9!-/:-@\\[-`{-~]*$")) {
            result = false;
            reason += "従業員番号に数字、アルファベット、記号以外の文字が含まれています。";
        }
    }

    /**
     * 従業員氏名バリデーションチェック
     */
    private void checkEmployeeFName() {
        // 20文字以内か
        if (employeeFName.length() > 20) {
            result = false;
            reason += "従業員氏名が20文字を超えています。";
            return;
        }

        // 半角英数記号、全角英数記号、ひらがな、カタカナ、漢字か
        if (!employeeFName.matches("^[a-zA-Z0-9 -/:-@\\[-`{-~　-〓ａ-ｚＡ-Ｚ０-９ぁ-んァ-ヶ\\p{InCjkUnifiedIdeographs}]*$")) {
            result = false;
            reason += "従業員氏名に登録不可能な文字が含まれています。";
        }
    }

    /**
     * 入社年月バリデーションチェック
     */
    private void checkHireYM() {
        // 6文字か
        if (hireYM.length() != 6) {
            result = false;
            reason += "入社年月は6桁(年(4桁)＋月(2桁))としてください。";
            return;
        }

        // 数字のみか
        if (!hireYM.matches("^[0-9]*$")) {
            result = false;
            reason += "入社年月に数字以外の文字が含まれています。";
            return;
        }

        // 年、月が妥当な値か
        int y = Utils.getIntegerFromString(hireYM.substring(0, 4));
        int m = Utils.getIntegerFromString(hireYM.substring(4, 6));
        int nowY = Utils.getNowYear();

        if (y < 1970 || y > nowY || m < 1 || m > 12) {
            result = false;
            reason += "入社年月が年(4桁)＋月(2桁)の形式になっていません。";
        }
    }

    /**
     * 採用種別バリデーションチェック
     */
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

    /**
     * 扶養有無バリデーションチェック
     */
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
     * 従業員情報の型変換(EmployeeCSV→Employee)
     * @return 従業員情報(Employee)
     */
    public Employee toEmployee() {
        Employee employee = new Employee();

        employee.setCompanyID(getCompanyIDLong());
        employee.setEmployeeCode(getEmployeeCode());
        employee.setEmployeeFName(getEmployeeFName());
        employee.setHireYM(getHireYMDate());
        employee.setAdoptCode(getAdoptCodeInteger());
        employee.setSupportCode(getSupportCodeInteger());

        return employee;
    }

    /**
     * Date形式の入社年月
     * @return 入社年月Dateクラス
     */
    public Date getHireYMDate() {
        Date hireDate = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        try {
            hireDate = sdf.parse(hireYM);
        }
        catch (ParseException e) {
            LogUtils.error("入社年月が正しくありません。(" + hireYM + ")");
        }

        return hireDate;
    }

    /**
     * Long形式の企業コード
     * @return Long形式の企業コード
     */
    public Long getCompanyIDLong() {
        return Utils.getLongFromString(companyID);
    }

    /**
     * Integer形式の採用種別
     * @return Integer形式の採用種別
     */
    public Integer getAdoptCodeInteger() {
        return Utils.getIntegerFromString(adoptCode);
    }

    /**
     * Integer形式の扶養有無
     * @return Integer形式の扶養有無
     */
    public Integer getSupportCodeInteger() {
        return Utils.getIntegerFromString(supportCode);
    }
}