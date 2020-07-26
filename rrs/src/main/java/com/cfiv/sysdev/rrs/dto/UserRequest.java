package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.annotation.PasswordConfirm;
import com.cfiv.sysdev.rrs.annotation.Unused;

import lombok.Data;

/**
 * ユーザー情報 リクエストデータ
 */
@Data
@PasswordConfirm
public class UserRequest implements Serializable {
    public UserRequest() {
        userRoleItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < Consts.USERROLE_CODES.length; i ++) {
            userRoleItems.put(Consts.USERROLE_CODES[i], Consts.USERROLE_NAMES[i]);
        }
        enabledItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < Consts.ENABLED_CODES.length; i ++) {
            enabledItems.put(Consts.ENABLED_CODES[i], Consts.ENABLED_NAMES[i]);
        }
    }

    public UserRequest(String _id, String _username, String _password, String _displayName,
            int _userRoleCode, String _company, boolean _enabled) {
        this();

        setId(_id);
        setUsername(_username);
        setPassword(_password);
        setPasswordCheck(_password);
        setDisplayName(_displayName);
        setUserRoleCode(_userRoleCode);
        setCompany(_company);
        setEnabled(_enabled ? 1 : 0);
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
     * ユーザー権限コード
     */
    private int userRoleCode;

    /**
     * 企業名称(リスト選択)
     */
    private String company;

    /**
     * 有効／無効
     */
    private int enabled;

    /**
     * ユーザー権限ラジオボタン表示内容リスト
     */
    private Map<Integer, String> userRoleItems;

    /**
     * 有効／無効ラジオボタン表示内容リスト
     */
    private Map<Integer, String> enabledItems;

    /**
     * ユーザー権限文字列
     * @return ユーザー権限文字列
     */
    public String getUserRole() {
        String result = userRoleItems.get(userRoleCode);

        if (result != null) {
            return result;
        }
        else {
            return "";
        }
    }

    /**
     * 有効／無効文字列
     * @return 有効／無効文字列
     */
    public String getEnabledName() {
        String result = enabledItems.get(enabled);

        if (result != null) {
            return result;
        }
        else {
            return "";
        }
    }
}