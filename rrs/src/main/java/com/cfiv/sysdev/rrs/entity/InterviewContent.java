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
 * –Ê’k“à—e Entity
 */
@Entity
@Data
@Table(name="D_INTERVIEW_CONTENT")
public class InterviewContent implements Serializable {

    /**
     * –Ê’k“à—e”Ô†
     */
    @Id
    @Column(name="CONTENT_NO")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String contentNo;

    /**
     * –Ê’k”Ô†
     */
    @Column(name="INTERVIEW_NO")
    private String interviewNo;

    /**
     *  –Ê’k“à—eƒR[ƒh
     */
    @Column(name="CONTENT_CODE")
    private int contentCode;

    /**
     * –Ê’k“à—eƒƒ‚
     */
    @Column(name="CONTENT_COMMENT")
    private String contentComment;

    /**
     * íœ
     */
    @Column(name="DELETED")
    private Boolean deleted;

    /**
     * “o˜^“ú
     */
    @Column(name="REGIST_TIME")
    private Date registTime;

    /**
     * “o˜^Ò
     */
    @Column(name="REGIST_USER")
    private String registUser;

    /**
     * XV“ú
     */
    @Column(name="UPDATE_TIME")
    private Date updateTime;

    /**
     * XVÒ
     */
    @Column(name="UPDATE_USER")
    private String updateUser;

    /**
     * XV‰ñ”
     */
    @Column(name="UPDATE_COUNT")
    private int updateCount;
}
