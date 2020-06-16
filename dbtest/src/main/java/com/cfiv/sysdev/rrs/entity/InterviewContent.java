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
 * 面談内容 Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_CONTENT")
public class InterviewContent implements Serializable {

    /**
     * 面談内容番号
     */
    @Id
    @Column(name="CONTENT_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String contentNo;

    /**
     * 面談番号
     */
    @Column(name="INTERVIEW_NO")
    private String interviewNo;

    /**
     *  面談内容コード
     */
    @Column(name="CONTENT_CODE")
    private int contentCode;

    /**
     * 面談内容メモ
     */
    @Column(name="CONTENT_COMMENT")
    private String contentComment;

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
}
