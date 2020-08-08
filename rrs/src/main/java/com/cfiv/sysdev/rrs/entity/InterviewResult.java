package com.cfiv.sysdev.rrs.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cfiv.sysdev.rrs.Utils;

import lombok.Data;

/**
 * 面談結果 Entity
 */
@Entity
@Data
@Table(name = "D_INTERVIEW_RESULT")
public class InterviewResult implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    /**
     * 企業コード
     */
    @Column(name = "COMPANY_ID", nullable = false)
    private Long companyID;

    /**
     * 従業員番号
     */
    @Column(name = "EMPLOYEE_CODE", nullable = false)
    private String employeeCode;

    /**
     * 面談日
     */
    @Column(name = "INTERVIEW_DATETIME", nullable = false)
    private Date interviewDate;

    /**
     * リファイナー
     */
    @Column(name = "REFINER_USER_ID", nullable = false)
    private String refinerUserID;

    /**
     * 面談時間コード
     */
    @Column(name = "INTERVIEWTIME_CODE", nullable = false)
    private int interviewTimeCode;

    /**
     * 相談内容
     */
    @Column(name = "INTERVIEWER_COMMENT", nullable = true)
    private String interviewerComment;

    /**
     * 情報開示コード
     */
    @Column(name = "DISCLOSE_CODE", nullable = false)
    private int discloseCode;

    /**
     * 管理者コメント
     */
    @Column(name = "ADMIN_COMMENT", nullable = true)
    private String adminComment;

    /**
     * 削除
     */
    @Column(name = "DELETED", nullable = false)
    private int deleted;

    /**
     * 登録日
     */
    @Column(name = "REGIST_TIME", nullable = false)
    private Date registTime;

    /**
     * 登録者
     */
    @Column(name = "REGIST_USER", nullable = false)
    private String registUser;

    /**
     * 更新日
     */
    @Column(name = "UPDATE_TIME", nullable = false)
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name = "UPDATE_USER", nullable = false)
    private String updateUser;

    /**
     * 更新回数
     */
    @Column(name = "UPDATE_COUNT", nullable = false)
    private int updateCount;

    /**
     * 面談内容
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "RESULT_ID")
    private List<InterviewContent> interviewContents;

    /**
     * 面談添付ファイル
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RESULT_ID")
    private List<InterviewAttach> interviewAttaches;

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
     * 文字列形式の面談日時
     * @return YYYY-MM-DD形式文字列
     */
    public String getInteviewDateStringDash() {
        return Utils.getStringDashFromDate(interviewDate);
    }

    /**
     * 文字列形式の面談日時
     * @return YYYY/MM/DD形式文字列
     */
    public String getInteviewDateStringSlash() {
        return Utils.getStringSlashFromDate(interviewDate);
    }
}
