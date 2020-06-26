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
     * ���O
     */
    private String name;

    /**
     * �L���^����
     */
    private String enabled;
}
