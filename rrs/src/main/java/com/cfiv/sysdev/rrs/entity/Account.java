package com.cfiv.sysdev.rrs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * ユーザー情報 Entity
 */
@Entity
@Table(name = "M_ACCOUNT")
@Data
public class Account {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    /**
     * ユーザー名
     */
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    /**
     * パスワード
     */
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    /**
     * 表示用ユーザー名
     */
    @Column(name = "DISPLAYNAME", nullable = false)
    private String displayName;

    /**
     * ユーザー権限
     */
    @Column(name = "USERROLE", nullable = false)
    private int userRole;

    /**
     * 企業コード
     */
    @Column(name = "COMPANY_ID", nullable = false)
    private Long companyID;

    /**
     * 有効／無効
     */
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    /**
     * 削除
     */
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    /**
     * 登録日
     */
    @Column(name = "REGIST_TIME", nullable = false)
    private Date registTime;

    /**
     * 登録者
     */
    @Column(name = "REGIST_USER", nullable = false)
    private String registUser;

    /**
     * 更新日
     */
    @Column(name = "UPDATE_TIME", nullable = false)
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name = "UPDATE_USER", nullable = false)
    private String updateUser;

    /**
     * 更新回数
     */
    @Column(name = "UPDATE_COUNT", nullable = false)
    private int updateCount;

    /**
     * 文字列形式のID
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public String idToString(int nDigits) {
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
     * 「有効」／「無効」文字列からの有効／無効設定
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public void setEnabledFromString(String es) {
        if (es.equals("有効")) {
            setEnabled(true);
        }
        else {
            setEnabled(false);
        }
    }

    /**
     * 文字列形式のユーザー権限
     * @return ユーザー権限文字列
     */
    public String getUserRoleString() {
        if (userRole == 0) {
            return "全体管理者";
        }
        else if (userRole == 1){
            return "企業管理者";
        }
        else {
            return "リファイナー";
        }
    }

    /**
     * 文字列からのユーザー権限設定
     * @param rs ユーザー権限文字列
     */
    public void setUserRoleFromString(String rs) {
        if (rs.equals("全体管理者")) {
            setUserRole(0);
        }
        else if (rs.equals("企業管理者")) {
            setUserRole(1);
        }
        else {
            setUserRole(10);
        }
    }

    /**
     * 企業名称文字列からの企業コード設定
     * @param cs 企業名称文字列
     */
    public void setCompanyIDFromName(String cs) {
        String[] cs_list = cs.split(":");

        if (cs_list.length >= 1) {

            try {
                setCompanyID(Long.parseLong(cs_list[0]));
            }
            catch (NumberFormatException e) {
                // 何もしない
            }
        }
    }
}