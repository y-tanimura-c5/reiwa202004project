package com.cfiv.sysdev.rrs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cfiv.sysdev.rrs.Utils;
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
     * 従業員番号
     */
    @Column(name="EMPLOYEE_CODE")
    private String employeeCode;

    /**
     * 従業員名字
     */
    @Column(name="EMPLOYEE_FNAME")
    private String employeeFName;

    /**
     * 入社年月
     */
    @Column(name="HIRE_YM")
    private Date hireYM;

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
    private int deleted;

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
    public String getIdString(int n) {
        return Utils.getStringFromLong(id, n);
    }

    /**
     * 文字列形式の企業コード
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public String getCompanyIDString(int n) {
        return Utils.getStringFromLong(companyID, n);
    }

    /**
     * 従業員情報の型変換(Employee→EmployeeRequest)
     * @return 従業員情報(EmployeeRequest)
     */
    public EmployeeRequest toRequest() {
        return new EmployeeRequest(getIdString(1), getCompanyIDString(4), getEmployeeCode(), getEmployeeFName(),
                getHireYM(), getAdoptCode(), getSupportCode(), getEmployCode());
    }
}