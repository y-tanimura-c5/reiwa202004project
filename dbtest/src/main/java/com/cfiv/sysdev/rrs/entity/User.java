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
 * ���[�U�[��� Entity
 */
@Entity
@Data
@Table(name="user")
public class User implements Serializable {

    /**
     * ID
     */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * ���O
     */
    @Column(name="name")
    private String name;

    /**
     * �Z��
     */
    @Column(name="address")
    private String address;

    /**
     * �d�b�ԍ�
     */
    @Column(name="phone")
    private String phone;

    /**
     * �X�V����
     */
    @Column(name="update_date")
    private Date updateDate;

    /**
     * �o�^����
     */
    @Column(name="create_date")
    private Date createDate;

    /**
     * �폜����
     */
    @Column(name="delete_date")
    private Date deleteDate;
}