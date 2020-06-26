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
 * ユーザー情報 Entity
 */
@Entity
@Table(name = "M_ACCOUNT")
@Data
public class Account {

    protected Account() {
    }

    public Account(String username, String password, String displayname, int userrole,
            long company_id, boolean enabled, String createuser) {
        Date now = new Date();

        setId(0L);
        setUsername(username);
        setPassword(password);
        setDisplayname(displayname);
        setUserrole(userrole);
        setCompanyID(company_id);
        setEnabled(enabled);
        setDeleted(false);
        setRegistUser(createuser);
        setRegistTime(now);
        setUpdateUser(createuser);
        setUpdateTime(now);
        setUpdateCount(0);
    }

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
     * パスワード
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * 表示用ユーザー名
     */
    @Column(name = "DISPLAYNAME", nullable = false)
    private String displayname;

    /**
     * ユーザー権限
     */
    @Column(name = "USERROLE", nullable = false)
    private int userrole;

    /**
     * 企業コード
     */
    @Column(name = "COMPANY_ID", nullable = false)
    private Long companyID;

    /**
     * 有効／無効
     */
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

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