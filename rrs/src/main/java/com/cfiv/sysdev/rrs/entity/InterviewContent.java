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
     * ID
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * 面談結果ID
     */
    @Column(name="RESULT_ID")
    private Long resultID;

    /**
     *  面談内容種別
     */
    @Column(name="CONTENT_KIND")
    private int contentKind;

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
}
