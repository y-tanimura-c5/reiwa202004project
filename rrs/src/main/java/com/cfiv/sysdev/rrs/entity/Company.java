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
 * ��Ə�� Entity
 */
@Entity
@Data
@Table(name="m_company")
public class Company implements Serializable {

    /**
     * ID
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * ���O
     */
    @Column(name="NAME")
    private String name;

    /**
     * �L���^����
     */
    @Column(name="ENABLED")
    private Boolean enabled;

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
