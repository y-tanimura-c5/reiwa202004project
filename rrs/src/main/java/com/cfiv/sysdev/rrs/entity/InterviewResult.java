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
 * �ʒk���� Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_RESULT")
public class InterviewResult implements Serializable {

    /**
     * �ʒk�ԍ�
     */
    @Id
    @Column(name="INTERVIEW_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String interviewNo;

    /**
     * ��ƃR�[�h
     */
    @Column(name="COMPANY_ID")
    private int companyID;

    /**
     * �Ј��ԍ�
     */
    @Column(name="EMPROYEE_ID")
    private String emproyeeID;

    /**
     * �ʒk��
     */
    @Column(name="INTERVIEW_DATETIME")
    private Date interviewDateTime;

    /**
     * ���t�@�C�i�[
     */
    @Column(name="REFINER_USER_ID")
    private String refinerUserID;

    /**
     * �ʒk���ԃR�[�h
     */
    @Column(name="INTERVIEWTIME_CODE")
    private int interviewTimeCode;

    /**
     * ���k���e
     */
    @Column(name="INTERVIEWER_COMMENT")
    private String interviewComment;

    /**
     * ���J���R�[�h
     */
    @Column(name="DISCLOSE_CODE")
    private int discloseCode;

    /**
     * �Ǘ��҃R�����g
     */
    @Column(name="ADMIN_COMMENT")
    private String adminComment;

    /**
     * �폜�t���O
     */
    @Column(name="DELETE_FLAG")
    private int deleteFlag;

    /**
     * �o�^��
     */
    @Column(name="REGIST_USER_ID")
    private String registUserID;

    /**
     * �o�^��
     */
    @Column(name="REGIST_DATETIME")
    private Date registDateTime;

    /**
     * �X�V��
     */
    @Column(name="UPDATE_USER_ID")
    private String updateUserID;

    /**
     * �X�V��
     */
    @Column(name="UPDATE_DATETIME")
    private Date updateDateTime;

    /**
     * �X�V��
     */
    @Column(name="UPDATETIME_COUNT")
    private int updateCount;

    /**
     * �ʒk���e���X�g
     */
    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "INTERVIEW_NO")
    private List<InterviewContent> interviewContentList;

    /**
     * �Y�t�t�@�C�����X�g
     */
    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name = "INTERVIEW_NO")
    private List<InterviewAttach> interviewAttachList;
}