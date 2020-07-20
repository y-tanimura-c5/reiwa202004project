package com.cfiv.sysdev.rrs.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private String interviewerComment;

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
     *  添付ファイル名
     */
    @Column (name="FILENAME")
    private String filename;

    /**
     *  添付ファイルデータ
     */
    @Column (name="FILEDATA")
    private byte filedata[];

    /**
     * 削除
     */
    @Column(name="DELETED")
    private Boolean deleted;

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
     * 面談内容リスト
     */
    @OneToMany(mappedBy="result", fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    private List<InterviewContent> interviewContentList;

    /**
     * 文字列形式の面談時間
     * @return 面談時間文字列
     */
    public String getInterviewTimeString() {
        if (interviewTimeCode == 0) {
            return "1時間未満";
        }
        else if (interviewTimeCode == 1) {
            return "1時間から2時間";
        }
        else {
            return "2時間超";
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列
     */
    public String getDiscloseString() {
        if (discloseCode == 0) {
            return "勤務先への情報開示を認める";
        }
        else if (discloseCode == 1) {
            return "勤務先へ一部情報については開示して欲しくない";
        }
        else {
            return "勤務先へ全ての情報を開示して欲しくない";
        }
    }

    /**
     * 文字列形式の面談日
     * @return 面談日文字列
     */
    public String getInterviewDateTimeString() {
        return new SimpleDateFormat("yyyy/MM/dd").format(interviewDateTime);
    }
}
