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

import lombok.Data;

/**
 * 面談結果 Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_RESULT")
public class InterviewResult implements Serializable {

    /**
     * 面談番号
     */
    @Id
    @Column(name="INTERVIEW_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String interviewNo;

    /**
     * 企業コード
     */
    @Column(name="COMPANY_ID")
    private int companyID;

    /**
     * 社員番号
     */
    @Column(name="EMPROYEE_ID")
    private String emproyeeID;

    /**
     * 面談日
     */
    @Column(name="INTERVIEW_DATETIME")
    private Date interviewDateTime;

    /**
     * リファイナー
     */
    @Column(name="REFINER_USER_ID")
    private String refinerUserID;

    /**
     * 面談時間コード
     */
    @Column(name="INTERVIEWTIME_CODE")
    private int interviewTimeCode;

    /**
     * 相談内容
     */
    @Column(name="INTERVIEWER_COMMENT")
    private String interviewComment;

    /**
     * 情報開示コード
     */
    @Column(name="DISCLOSE_CODE")
    private int discloseCode;

    /**
     * 管理者コメント
     */
    @Column(name="ADMIN_COMMENT")
    private String adminComment;

    /**
     * 削除フラグ
     */
    @Column(name="DELETE_FLAG")
    private int deleteFlag;

    /**
     * 登録者
     */
    @Column(name="REGIST_USER_ID")
    private String registUserID;

    /**
     * 登録日
     */
    @Column(name="REGIST_DATETIME")
    private Date registDateTime;

    /**
     * 更新者
     */
    @Column(name="UPDATE_USER_ID")
    private String updateUserID;

    /**
     * 更新日
     */
    @Column(name="UPDATE_DATETIME")
    private Date updateDateTime;

    /**
     * 更新回数
     */
    @Column(name="UPDATETIME_COUNT")
    private int updateCount;

    /**
     * 面談内容リスト
     */
    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "INTERVIEW_NO")
    private List<InterviewContent> interviewContentList;

    /**
     * 添付ファイルリスト
     */
    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "INTERVIEW_NO")
    private List<InterviewAttach> interviewAttachList;
}
