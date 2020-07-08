package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CompanyRequest implements Serializable {
    public CompanyRequest(String _id, String _name, String _enabled) {
        setId(_id);
        setName(_name);
        setEnabled(_enabled);
    }

    public CompanyRequest() {
    }

    /**
     * ID
     */
    private String id;

    /**
     * 名前
     */
    private String name;

    /**
     * 有効／無効
     */
    private String enabled;
}
