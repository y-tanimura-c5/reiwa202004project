package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * ユーザー情報 リクエストデータ
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
     * ユーザーID
     */
    private String username;

    /**
     * パスワード
     */
    private String password;

    /**
     * パスワード(確認用)
     */
    private String passwordConf;

    /**
     * 表示用名称
     */
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