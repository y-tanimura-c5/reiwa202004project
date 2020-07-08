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
 * 添付ファイル Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_ATTACH")
public class InterviewAttach implements Serializable {

    /**
     * 添付ファイル番号
     */
    @Id
    @Column(name="ATTACH_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String attachNo;

    /**
     * 面談番号
     */
    @Column(name="INTERVIEW_NO")
    private String interviewNo;

    /**
     * 添付ファイルURL
     */
    @Column(name="ATTACH_URL")
    private String attachURL;

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
}
