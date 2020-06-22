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
 * �ʒk���e Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_CONTENT")
public class InterviewContent implements Serializable {

    /**
     * �ʒk���e�ԍ�
     */
    @Id
    @Column(name="CONTENT_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String contentNo;

    /**
     * �ʒk�ԍ�
     */
    @Column(name="INTERVIEW_NO")
    private String interviewNo;

    /**
     *  �ʒk���e�R�[�h
     */
    @Column(name="CONTENT_CODE")
    private int contentCode;

    /**
     * �ʒk���e����
     */
    @Column(name="CONTENT_COMMENT")
    private String contentComment;

    /**
     * �폜
     */
    @Column(name="DELETED")
    private Boolean deleted;

    /**
     * �o�^��
     */
    @Column(name="REGIST_TIME")
    private Date registTime;

    /**
     * �o�^��
     */
    @Column(name="REGIST_USER")
    private String registUser;

    /**
     * �X�V��
     */
    @Column(name="UPDATE_TIME")
    private Date updateTime;

    /**
     * �X�V��
     */
    @Column(name="UPDATE_USER")
    private String updateUser;

    /**
     * �X�V��
     */
    @Column(name="UPDATE_COUNT")
    private int updateCount;
}
