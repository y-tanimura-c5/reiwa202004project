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
    private String displayName;

    /**
     * ���[�U�[����
     */
    @Column(name = "USERROLE", nullable = false)
    private int userRole;

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

    /**
     * ������`����ID
     * @param nDigits 0���ߌ���
     * @return �w�茅��0���ߌ��ID������
     */
    public String idToString(int nDigits) {
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
     * �u�L���v�^�u�����v�����񂩂�̗L���^�����ݒ�
     * @param nDigits 0���ߌ���
     * @return �w�茅��0���ߌ��ID������
     */
    public void setEnabledFromString(String es) {
        if (es.equals("�L��")) {
            setEnabled(true);
        }
        else {
            setEnabled(false);
        }
    }

    /**
     * ������`���̃��[�U�[����
     * @return ���[�U�[����������
     */
    public String getUserRoleString() {
        if (userRole == 0) {
            return "�S�̊Ǘ���";
        }
        else if (userRole == 1){
            return "��ƊǗ���";
        }
        else {
            return "���t�@�C�i�[";
        }
    }

    /**
     * �����񂩂�̃��[�U�[�����ݒ�
     * @param rs ���[�U�[����������
     */
    public void setUserRoleFromString(String rs) {
        if (rs.equals("�S�̊Ǘ���")) {
            setUserRole(0);
        }
        else if (rs.equals("��ƊǗ���")) {
            setUserRole(1);
        }
        else {
            setUserRole(10);
        }
    }

    /**
     * ��Ɩ��̕����񂩂�̊�ƃR�[�h�ݒ�
     * @param cs ��Ɩ��̕�����
     */
    public void setCompanyIDFromName(String cs) {
        String[] cs_list = cs.split(":");

        if (cs_list.length >= 1) {

            try {
                setCompanyID(Long.parseLong(cs_list[0]));
            }
            catch (NumberFormatException e) {
                // �������Ȃ�
            }
        }
    }
}