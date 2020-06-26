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
 * ���[�U�[��� Entity
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
     * ���[�U�[��
     */
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    /**
     * �p�X���[�h
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * �\���p���[�U�[��
     */
    @Column(name = "DISPLAYNAME", nullable = false)
    private String displayname;

    /**
     * ���[�U�[����
     */
    @Column(name = "USERROLE", nullable = false)
    private int userrole;

    /**
     * ��ƃR�[�h
     */
    @Column(name = "COMPANY_ID", nullable = false)
    private Long companyID;

    /**
     * �L���^����
     */
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    /**
     * �폜
     */
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    /**
     * �o�^��
     */
    @Column(name = "REGIST_TIME", nullable = false)
    private Date registTime;

    /**
     * �o�^��
     */
    @Column(name = "REGIST_USER", nullable = false)
    private String registUser;

    /**
     * �X�V��
     */
    @Column(name = "UPDATE_TIME", nullable = false)
    private Date updateTime;

    /**
     * �X�V��
     */
    @Column(name = "UPDATE_USER", nullable = false)
    private String updateUser;

    /**
     * �X�V��
     */
    @Column(name = "UPDATE_COUNT", nullable = false)
    private int updateCount;
}