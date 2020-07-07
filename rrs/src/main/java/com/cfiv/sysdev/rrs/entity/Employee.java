package com.cfiv.sysdev.rrs.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;

import lombok.Data;

/**
 * 従業員情報 Entity
 */
@Entity
@Data
@Table(name="M_EMPLOYEE")
public class Employee {

    /**
     * ID
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * 企業コード
     */
    @Column(name="COMPANY_ID")
    private Long companyID;

    /**
     * 社員番号
     */
    @Column(name="EMPLOYEE_ID")
    private String employeeID;

    /**
     * 従業員名字
     */
    @Column(name="EMPLOYEE_FNAME")
    private String employeeFName;

    /**
     * 入社年月
     */
    @Column(name="HIRE_YM")
    private String hireYM;

    /**
     * 採用種別
     */
    @Column(name="ADOPT_CODE")
    private int adoptCode;

    /**
     * 扶養有無
     */
    @Column(name="SUPPORT_CODE")
    private int supportCode;

    /**
     * 就業種別
     */
    @Column(name="EMPLOY_CODE")
    private int employCode;

    /**
     * 削除
     */
    @Column(name="DELETED")
    private boolean deleted;

    /**
     * 登録日
     */
    @Column(name="REGIST_TIME")
    private Date registTime;

    /**
     * 登録者
     */
    @Column(name="REGIST_USER")
    private String registUser;

    /**
     * 更新日
     */
    @Column(name="UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name="UPDATE_USER")
    private String updateUser;

    /**
     * 更新回数
     */
    @Column(name="UPDATE_COUNT")
    private int updateCount;

    /**
     * 文字列形式のID
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public String getIdString(int nDigits) {
        return String.format("%0" + nDigits + "d", id);
    }

    /**
     * 文字列形式の企業コード
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public String getCompanyIDString(int nDigits) {
        return String.format("%0" + nDigits + "d", companyID);
    }

    /**
     * 文字列からの企業コード設定
     * @param cs 企業コード文字列
     */
    public void setCompanyIDFromString(String cs) {
        try {
            setCompanyID(Long.parseLong(cs));
        }
        catch (NumberFormatException e) {
            setCompanyID(0L);
        }
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
            LogUtils.info("入社年月が正しくありません。(" + hireYM + ")");
        }

        return hireDate;
    }

    /**
     * 文字列形式の採用種別
     * @return 採用種別文字列
     */
    public String getAdoptCodeString() {
        if (adoptCode == 0) {
            return "新卒採用";
        }
        else {
            return "中途採用";
        }
    }

    /**
     * 文字列からの採用種別設定
     * @param as 採用種別文字列
     */
    public void setAdoptCodeFromString(String as) {
        if (as.equals("新卒採用") || as.equals("0")) {
            setAdoptCode(0);
        }
        else {
            setAdoptCode(1);
        }
    }

    /**
     * 文字列形式の扶養有無
     * @return 扶養有無文字列
     */
    public String getSupportCodeString() {
        if (supportCode == 0) {
            return "扶養なし";
        }
        else {
            return "扶養あり";
        }
    }

    /**
     * 文字列からの扶養有無設定
     * @param ss 扶養有無文字列
     */
    public void setSupportCodeFromString(String ss) {
        if (ss.equals("扶養なし") || ss.equals("0")) {
            setSupportCode(0);
        }
        else {
            setSupportCode(1);
        }
    }

    /**
     * 文字列形式の就業種別
     * @return 就業種別文字列
     */
    public String getEmployCodeString() {
        if (employCode == 0) {
            return "在籍中";
        }
        else {
            return "退職済";
        }
    }

    /**
     * 文字列からの就業種別設定
     * @param es 就業種別文字列
     */
    public void setEmployCodeFromString(String es) {
        if (es.equals("在籍中") || es.equals("0")) {
            setEmployCode(0);
        }
        else {
            setEmployCode(1);
        }
    }

    /**
     * 従業員情報の型変換(Employee→EmployeeRequest)
     * @return 従業員情報(EmployeeRequest)
     */
    public EmployeeRequest toRequest() {
        return new EmployeeRequest(getIdString(1), getCompanyIDString(4), getEmployeeID(), getEmployeeFName(),
                getHireYM(), getAdoptCodeString(), getSupportCodeString(), getEmployCodeString());
    }
}