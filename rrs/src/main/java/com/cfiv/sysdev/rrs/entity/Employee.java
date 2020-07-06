package com.cfiv.sysdev.rrs.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cfiv.sysdev.rrs.LogUtils;

import lombok.Data;

/**
 * �]�ƈ���� Entity
 */
@Entity
@Data
@Table(name="M_EMPROYEE")
public class Employee {

    /**
     * ID
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * ��ƃR�[�h
     */
    @Column(name="COMPANY_ID")
    private Long companyID;

    /**
     * �Ј��ԍ�
     */
    @Column(name="EMPROYEE_ID")
    private String employeeID;

    /**
     * �]�ƈ�����
     */
    @Column(name="EMPROYEE_FNAME")
    private String employeeFName;

    /**
     * ���ДN��
     */
    @Column(name="HIRE_YM")
    private String hireYM;

    /**
     * �̗p���
     */
    @Column(name="ADOPT_CODE")
    private int adoptCode;

    /**
     * �}�{�L��
     */
    @Column(name="SUPPORT_CODE")
    private int supportCode;

    /**
     * �A�Ǝ��
     */
    @Column(name="EMPROY_CODE")
    private int employCode;

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
     * ������`���̊�ƃR�[�h
     * @param nDigits 0���ߌ���
     * @return �w�茅��0���ߌ��ID������
     */
    public String getCompanyIDString(int nDigits) {
        return String.format("%0" + nDigits + "d", id);
    }

    /**
     * Date�`���̓��ДN��
     * @return ���ДN��Date�N���X
     */
    public Date getHireYMDate() {
        Date hireDate = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        try {
            hireDate = sdf.parse(hireYM);
        }
        catch (ParseException e) {
            LogUtils.info("���ДN��������������܂���B(" + hireYM + ")");
        }

        return hireDate;
    }

    /**
     * ������`���̗̍p���
     * @return �̗p��ʕ�����
     */
    public String getAdoptCodeString() {
        if (adoptCode == 0) {
            return "�V���̗p";
        }
        else {
            return "���r�̗p";
        }
    }

    /**
     * �����񂩂�̗̍p��ʐݒ�
     * @param as �̗p��ʕ�����
     */
    public void setAdoptCodeFromString(String as) {
        if (as.equals("�V���̗p")) {
            setAdoptCode(0);
        }
        else {
            setAdoptCode(1);
        }
    }

    /**
     * ������`���̕}�{�L��
     * @return �}�{�L��������
     */
    public String getSupportCodeString() {
        if (supportCode == 0) {
            return "�}�{�Ȃ�";
        }
        else {
            return "�}�{����";
        }
    }

    /**
     * �����񂩂�̕}�{�L���ݒ�
     * @param ss �}�{�L��������
     */
    public void setSupportCodeFromString(String ss) {
        if (ss.equals("�}�{�Ȃ�")) {
            setSupportCode(0);
        }
        else {
            setSupportCode(1);
        }
    }

    /**
     * ������`���̏A�Ǝ��
     * @return �A�Ǝ�ʕ�����
     */
    public String getEmployCodeString() {
        if (employCode == 0) {
            return "�ݐВ�";
        }
        else {
            return "�ސE��";
        }
    }

    /**
     * �����񂩂�̏A�Ǝ�ʐݒ�
     * @param es �A�Ǝ�ʕ�����
     */
    public void setEmployCodeFromString(String es) {
        if (es.equals("�ݐВ�")) {
            setEmployCode(0);
        }
        else {
            setEmployCode(1);
        }
    }
}