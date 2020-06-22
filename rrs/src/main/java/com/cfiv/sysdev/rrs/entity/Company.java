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
 * Šé‹Æî•ñ Entity
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
     * –¼‘O
     */
    @Column(name="NAME")
    private String name;

    /**
     * —LŒø^–³Œø
     */
    @Column(name="ENABLED")
    private Boolean enabled;

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
