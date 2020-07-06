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
@Table(name="M_COMPANY")
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
    private boolean enabled;

    /**
     * �폜
     */
    @Column(name="DELETED")
    private boolean deleted;

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

    /**
     * ������`����ID
     * @param nDigits 0���ߌ���
     * @return �w�茅��0���ߌ��ID������
     */
    public String getIdString(int nDigits) {
        return String.format("%0" + nDigits + "d", id);
    }

    /**
     * ������`���̗L���^����
     * @return �u�L���v�܂��́u�����v������
     */
    public String getEnabledString() {
        if (enabled) {
            return "�L��";
        }
        else {
            return "����";
        }
    }

    /**
     * �����񂩂�̗L���^�����ݒ�
     * @param es �u�L���v�^�u�����v������
     */
    public void setEnabledFromString(String es) {
        if (es.equals("�L��")) {
            setEnabled(true);
        }
        else {
            setEnabled(false);
        }
    }
}
