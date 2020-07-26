package com.cfiv.sysdev.rrs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * ログイン日時情報 Entity
 */
@Entity
@Table(name = "D_LOGINTIME")
@Data
public class LoginTime {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    /**
     * ユーザー名
     */
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    /**
     * 企業コード
     */
    @Column(name = "COMPANY_ID", nullable = false)
    private Long companyID;

    /**
     * ログイン日時
     */
    @Column(name="LOGIN_TIME", nullable = false)
    private Date loginTime;

    /**
     * 削除
     */
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

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
}