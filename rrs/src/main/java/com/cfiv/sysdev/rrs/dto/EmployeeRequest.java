package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmployeeRequest implements Serializable {
    public EmployeeRequest(String _id, String _companyID, String _employeeID, String _employeeFName,
            String _hireYM, String _adoptCode, String _supportCode, String _employCode) {
        setId(_id);
        setCompanyID(_companyID);
        setEmployeeID(_employeeID);
        setEmployeeFName(_employeeFName);
        setHireYM(_hireYM);
        setAdoptCode(_adoptCode);
        setSupportCode(_supportCode);
        setEmployCode(_employCode);
    }

    public EmployeeRequest() {
    }

    /**
     * ID
     */
    private String id;

    /**
     * ��ƃR�[�h
     */
    private String companyID;

    /**
     * �Ј��ԍ�
     */
    private String employeeID;

    /**
     * �]�ƈ�����
     */
    private String employeeFName;

    /**
     * ���ДN��
     */
    private String hireYM;

    /**
     * �̗p���
     */
    private String adoptCode;

    /**
     * �}�{�L��
     */
    private String supportCode;

    /**
     * �A�Ǝ��
     */
    private String employCode;
}
