package com.cfiv.sysdev.rrs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * �Y�t�t�@�C�� Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_ATTACH")
public class InterviewAttach implements Serializable {

    /**
     * �Y�t�t�@�C���ԍ�
     */
    @Id
    @Column(name="ATTACH_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String attachNo;

    /**
     * �ʒk�ԍ�
     */
    @Column(name="INTERVIEW_NO")
    private String interviewNo;

    /**
     * �Y�t�t�@�C��URL
     */
    @Column(name="ATTACH_URL")
    private String attachURL;

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
}
