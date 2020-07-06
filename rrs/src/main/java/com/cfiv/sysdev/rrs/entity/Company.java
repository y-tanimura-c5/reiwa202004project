package com.cfiv.sysdev.rrs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 企業情報 Entity
 */
@Entity
@Data
@Table(name="M_COMPANY")
public class Company implements Serializable {

    /**
     * ID
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * 名前
     */
    @Column(name="NAME")
    private String name;

    /**
     * 有効／無効
     */
    @Column(name="ENABLED")
    private boolean enabled;

    /**
     * 削除
     */
    @Column(name="DELETED")
    private boolean deleted;

    /**
     * 登録日
     */
    @Column(name="REGIST_TIME")
    private Date registTime;

    /**
     * 登録者
     */
    @Column(name="REGIST_USER")
    private String registUser;

    /**
     * 更新日
     */
    @Column(name="UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name="UPDATE_USER")
    private String updateUser;

    /**
     * 更新回数
     */
    @Column(name="UPDATE_COUNT")
    private int updateCount;

    /**
     * 文字列形式のID
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public String getIdString(int nDigits) {
        return String.format("%0" + nDigits + "d", id);
    }

    /**
     * 文字列形式の有効／無効
     * @return 「有効」または「無効」文字列
     */
    public String getEnabledString() {
        if (enabled) {
            return "有効";
        }
        else {
            return "無効";
        }
    }

    /**
     * 文字列からの有効／無効設定
     * @param es 「有効」／「無効」文字列
     */
    public void setEnabledFromString(String es) {
        if (es.equals("有効")) {
            setEnabled(true);
        }
        else {
            setEnabled(false);
        }
    }
}
