/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.task;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.common.Util;
import com.cfiv.simpleweb.dbaccess.DBAccess;
import com.cfiv.simpleweb.dbaccess.DBController;

/**
 *
 * @author tanimura
 */
public class AuthTask extends AbstractTask {
    private DBController customerDetail;
    private String userName;
    private String password;
    private boolean isAuth = false;

    public AuthTask() {
        customerDetail = new DBController();
    }

    public AuthTask(String name, String pass) {
        this();

        userName = name;
        password = pass;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public String getCustomerId() {
        return (String) customerDetail.getByName("CUSTOMER_ID");
    }

    public int getUserKind() {
        return (Integer) customerDetail.getByName("USER_KIND");
    }

    public String getCompanyName() {
        return (String) customerDetail.getByName("COMPANY_NAME");
    }

    public String getSectionName() {
        return (String) customerDetail.getByName("SECTION_NAME");
    }

    public String getChargeName() {
        return (String) customerDetail.getByName("CHARGE_NAME");
    }

    public String getZipCode() {
        return (String) customerDetail.getByName("ZIP_CODE");
    }

    public String getAddress() {
        return (String) customerDetail.getByName("ADDRESS");
    }

    public String getTelNo() {
        return (String) customerDetail.getByName("TEL_NO");
    }

    public String getMailAddress() {
        return (String) customerDetail.getByName("MAIL_ADDRESS");
    }

    public String getPassCode() {
        return (String) customerDetail.getByName("PASS_CODE");
    }

    @Override
    protected boolean doTask() throws Exception {
        DBAccess dbAccess = new DBAccess();

        try {
            dbAccess.open();

            // ユーザ情報を取得する
            customerDetail = dbAccess.getCustomerDetailController(userName);

            // ユーザ情報が有効の場合
            if (customerDetail != null) {
                // パスワードをハッシュ化する
                MessageDigest md;

                try {
                    md = MessageDigest.getInstance("SHA-512");
                }
                catch (NoSuchAlgorithmException e) {
                    Log.printStackTrace(e);
                    return false;
                }

                // パスワードハッシュコードを16進数の文字列に変換する
                String digest = Util.toHexString(md.digest(password.getBytes()));

                // ユーザ情報のパスワードメッセージダイジェストを取得する
                String passwordMD = (String) customerDetail.getByName("PASSWORD_HASH");

                // パスワードの一致結果を格納する
                isAuth = digest.equals(passwordMD);
            }
        }
        catch (Exception e) {
            Log.printStackTrace(e);

            // Exceptionをスローする
            throw e;
        }
        finally {
            try {
                dbAccess.close();
            }
            catch (SQLException e) {
                //無視
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "AuthTask{"
                + "CustomerID=" + getCustomerId() + ", "
                + "UserKind=" + getUserKind() + ", "
                + "CompaneName=" + getCompanyName() + ", "
                + "SectionName=" + getSectionName() + ", "
                + "ChargeName=" + getChargeName() + ", "
                + "ZipCode=" + getZipCode() + ", "
                + "Address=" + getAddress() + ", "
                + "TelNo=" + getTelNo() + ", "
                + "MailAddress=" + getMailAddress() + ", "
                + "PassCode=" + getPassCode()
                + '}';
    }
}
