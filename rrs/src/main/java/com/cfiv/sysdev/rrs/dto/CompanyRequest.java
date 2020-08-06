package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.Size;

import com.cfiv.sysdev.rrs.Consts;

import lombok.Data;

@Data
public class CompanyRequest implements Serializable {

    public CompanyRequest() {
        enabledItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < Consts.ENABLED_CODES.length; i ++) {
            enabledItems.put(Consts.ENABLED_CODES[i], Consts.ENABLED_NAMES[i]);
        }
    }

    public CompanyRequest(String _id, String _name, int _enabled, Date _lastLogin, Date _lastInter) {
        this();

        setId(_id);
        setName(_name);
        setEnabled(_enabled);
        setInitialID("自動付与されます");
        setLastLoginDate(_lastLogin);
        setLastInterviewDate(_lastInter);
    }

    /**
     * ID
     */
    private String id;

    /**
     * ID
     */
    private String initialID;

    /**
     * 名前
     */
    @Size(max=50)
    private String name;

    /**
     * 有効／無効
     */
    private int enabled;

    /**
     * 有効／無効ラジオボタン表示内容リスト
     */
    private Map<Integer, String> enabledItems;

    /**
     * 最終ログイン日
     */
    private Date lastLoginDate;

    /**
     * 最終面談日
     */
    private Date lastInterviewDate;

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

    /**
     * 最終ログイン日文字列
     * @return 最終ログイン日文字列
     */
    public String getLastLoginDateString() {
        return getDateString(lastLoginDate);
    }

    /**
     * 最終ログイン日文字列
     * @return 最終ログイン日文字列
     */
    public String getLastInterviewDateString() {
        return getDateString(lastInterviewDate);
    }

    /**
     * 日付文字列
     * @return 日付文字列文字列
     */
    public String getDateString(Date date) {
        if (date != null) {
            return new SimpleDateFormat("yyyy/MM/dd").format(date);
        }
        else {
            return "－";
        }
    }
}
