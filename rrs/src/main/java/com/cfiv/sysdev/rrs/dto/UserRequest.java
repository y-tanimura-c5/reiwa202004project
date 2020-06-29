package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * ���[�U�[��� ���N�G�X�g�f�[�^
 */
@Data
public class UserRequest implements Serializable {
    public UserRequest() {
    }

    public UserRequest(String _id, String _username, String _password, String _displayName,
            String _userRole, String _company, String _enabled) {
        setId(_id);
        setUsername(_username);
        setPassword(_password);
        setPasswordConf(_password);
        setDisplayName(_displayName);
        setUserRole(_userRole);
        setCompany(_company);
        setEnabled(_enabled);
    }

    /**
     * ID
     */
    private String id;

    /**
     * ���[�U�[ID
     */
    private String username;

    /**
     * �p�X���[�h
     */
    private String password;

    /**
     * �p�X���[�h(�m�F�p)
     */
    private String passwordConf;

    /**
     * �\���p����
     */
    private String displayName;

    /**
     * ���[�U�[����
     */
    private String userRole;

    /**
     * ��Ɩ���(���X�g�I��)
     */
    private String company;

    /**
     * �L���^����
     */
    private String enabled;
}