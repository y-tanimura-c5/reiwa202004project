package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cfiv.sysdev.rrs.annotation.PasswordConfirm;

import lombok.Data;

/**
 * ���[�U�[��� ���N�G�X�g�f�[�^
 */
@Data
@PasswordConfirm
public class UserEditRequest implements Serializable {
    public UserEditRequest() {
    }

    public UserEditRequest(String _id, String _username, String _password, String _displayName,
            String _userRole, String _company, String _enabled) {
        setId(_id);
        setUsername(_username);
        setPassword(_password);
        setPasswordCheck(_password);
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
    @NotBlank
    @Size(min=1, max=30)
    private String username;

    /**
     * �p�X���[�h
     */
    @Size(min=8, max=50)
    private String password;

    /**
     * �p�X���[�h(�m�F�p)
     */
    @Size(min=8, max=50)
    private String passwordCheck;

    /**
     * �\���p����
     */
    @Size(max=100)
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