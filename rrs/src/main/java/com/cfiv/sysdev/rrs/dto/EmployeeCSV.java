package com.cfiv.sysdev.rrs.dto;

import com.cfiv.sysdev.rrs.entity.Employee;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;

/**
 * �]�ƈ���� Entity(CSV�p)
 */
@Data
public class EmployeeCSV {

    /**
     * ��ƃR�[�h
     */
    @CsvBindByName(column = "��ƃR�[�h", required = true)
    private String companyID;

    /**
     * �Ј��ԍ�
     */
    @CsvBindByName(column = "�]�ƈ��ԍ�", required = true)
    private String employeeID;

    /**
     * �]�ƈ�����
     */
    @CsvBindByName(column = "�]�ƈ�����", required = true)
    private String employeeFName;

    /**
     * ���ДN��
     */
    @CsvBindByName(column = "���ДN��", required = true)
    private String hireYM;

    /**
     * �̗p���
     */
    @CsvBindByName(column = "�̗p���", required = true)
    private String adoptCode;

    /**
     * �}�{�L��
     */
    @CsvBindByName(column = "�}�{�L��", required = true)
    private String supportCode;


    private boolean result = true;

    private String reason = "";

    public void check() {
        checkCompanyID();
        checkEmployeeID();
        checkEmployeeFName();
        checkHireYM();
        checkAdoptCode();
        checkSupportCode();
    }

    private void checkCompanyID() {
        // �����݂̂�
        if (!companyID.matches("^[0-9]*$")) {
            result = false;
            reason += "��ƃR�[�h�ɐ����ȊO�̕������܂܂�Ă��܂��B";
        }
    }

    private void checkEmployeeID() {
        // 50�����ȓ���
        if (employeeID.length() > 50) {
            result = false;
            reason += "�]�ƈ��ԍ���50�����𒴂��Ă��܂��B";
        }

        // �����A�A���t�@�x�b�g�A�L���݂̂�
        if (!employeeID.matches("^[a-zA-Z0-9!-/:-@\\[-`{-~]*$")) {
            result = false;
            reason += "�]�ƈ��ԍ��ɐ����A�A���t�@�x�b�g�A�L���ȊO�̕������܂܂�Ă��܂��B";
        }
    }

    private void checkEmployeeFName() {
        // 20�����ȓ���
        if (employeeFName.length() > 20) {
            result = false;
            reason += "�]�ƈ�������20�����𒴂��Ă��܂��B";
        }

        // �S�p�J�^�J�i�݂̂�
        if (!employeeFName.matches("^[�@-�����[]*$")) {
            result = false;
            reason += "�]�ƈ������ɑS�p�J�^�J�i�ȊO�̕������܂܂�Ă��܂��B";
        }
    }

    private void checkHireYM() {
        // 6������
        if (hireYM.length() != 6) {
            result = false;
            reason += "���ДN�����N(4��)�{��(2��)�̌`���ɂȂ��Ă��܂���B";
        }

        // �����݂̂�
        if (!hireYM.matches("^[0-9]*$")) {
            result = false;
            reason += "���ДN���ɐ����ȊO�̕������܂܂�Ă��܂��B";
        }
    }

    private void checkAdoptCode() {
        // �����݂̂�
        try {
            int code = Integer.parseInt(adoptCode);
            if (code != 0 && code != 1) {
                result = false;
                reason += "�̗p��ʂ�0�܂���1�ȊO�̕������ݒ肳��Ă��܂��B";
            }
        }
        catch (NumberFormatException e) {
            result = false;
            reason += "�̗p��ʂ�0�܂���1�ȊO�̕������ݒ肳��Ă��܂��B";
        }
    }

    private void checkSupportCode() {
        // �����݂̂�
        try {
            int code = Integer.parseInt(supportCode);
            if (code != 0 && code != 1) {
                result = false;
                reason += "�}�{�L����0�܂���1�ȊO�̕������ݒ肳��Ă��܂��B";
            }
        }
        catch (NumberFormatException e) {
            result = false;
            reason += "�}�{�L����0�܂���1�ȊO�̕������ݒ肳��Ă��܂��B";
        }
    }

    /**
     * �N���X�̕�����\��
     */
    public String toString() {
        return "��ƃR�[�h = " + companyID + ", �]�ƈ��ԍ� = " + employeeID + ", �]�ƈ����� = " + employeeFName +
                ", ���ДN�� = " + hireYM + ", �̗p��� = " + adoptCode + ", �}�{�L�� = " + supportCode;
    }

    /**
     * �]�ƈ����̌^�ϊ�(EmployeeCSV��Employee)
     * @return �]�ƈ����(Employee)
     */
    public Employee toEmployee() {
        Employee employee = new Employee();

        employee.setCompanyIDFromString(getCompanyID());
        employee.setEmployeeID(getEmployeeID());
        employee.setEmployeeFName(getEmployeeFName());
        employee.setHireYM(getHireYM());
        employee.setAdoptCodeFromString(getAdoptCode());
        employee.setSupportCodeFromString(getSupportCode());

        return employee;
    }
}