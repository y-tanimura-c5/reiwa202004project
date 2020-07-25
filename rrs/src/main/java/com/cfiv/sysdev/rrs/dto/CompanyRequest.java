package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cfiv.sysdev.rrs.Const;

import lombok.Data;

@Data
public class CompanyRequest implements Serializable {

    public CompanyRequest() {
        enabledItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < Const.ENABLED_CODES.length; i ++) {
            enabledItems.put(Const.ENABLED_CODES[i], Const.ENABLED_NAMES[i]);
        }
    }

    public CompanyRequest(String _id, String _name, boolean _enabled) {
        this();

        setId(_id);
        setName(_name);
        setEnabled(_enabled ? 1 : 0);
        setInitialID("自動付与されます");
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
