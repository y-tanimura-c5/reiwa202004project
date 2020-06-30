package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cfiv.sysdev.rrs.annotation.PasswordConfirm;
import com.cfiv.sysdev.rrs.annotation.Unused;

import lombok.Data;

/**
 * ユーザー情報 リクエストデータ
 */
@Data
@PasswordConfirm
public class UserAddRequest implements Serializable {
    public UserAddRequest() {
    }

    public UserAddRequest(String _id, String _username, String _password, String _displayName,
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
     * ユーザーID
     */
    @NotBlank
    @Size(min=1, max=30)
    @Unused
    private String username;

    /**
     * パスワード
     */
    @NotBlank
    @Size(min=8, max=50)
    private String password;

    /**
     * パスワード(確認用)
     */
    @NotBlank
    @Size(min=8, max=50)
    private String passwordCheck;

    /**
     * 表示用名称
     */
    @Size(max=100)
    private String displayName;

    /**
     * ユーザー権限
     */
    private String userRole;

    /**
     * 企業名称(リスト選択)
     */
    private String company;

    /**
     * 有効／無効
     */
    private String enabled;
}