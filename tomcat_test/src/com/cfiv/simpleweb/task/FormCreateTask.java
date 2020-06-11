/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.task;

import java.sql.SQLException;

import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.dbaccess.DBAccess;
import com.cfiv.simpleweb.dbaccess.DBController;
import com.cfiv.simpleweb.dbaccess.DBControllerList;

/**
 *
 * @author tanimura
 */
public class FormCreateTask extends AbstractTask {
    private DBController customerDetail;
    private DBControllerList entryDeviceList;
    private String customerID;

    public FormCreateTask() {
        customerDetail = new DBController();
        entryDeviceList = new DBControllerList();
        entryDeviceList.add(new DBController());
    }

    public FormCreateTask(String id) {
        this();

        customerID = id;
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

    public String getDeviceName() {
        return (String) entryDeviceList.get(0).getByName("DEVICE_NAME");
    }

    public int getStorageSize() {
        //    Log.log(Log.INFO, Defines.STORAGE_SIZE + " = " + entryDeviceList.get(0).getByName("STORAGE_SIZE"));
        return (Integer) entryDeviceList.get(0).getByName("STORAGE_SIZE");
    }

    public String getColorName() {
        return (String) entryDeviceList.get(0).getByName("COLOR_NAME");
    }

    public String getContractKind() {
        return (String) entryDeviceList.get(0).getByName("CONTRACT_KIND");
    }

    public int getContractSpan() {
        return (Integer) entryDeviceList.get(0).getByName("CONTRACT_SPAN");
    }

    public String getPrisePlan() {
        return (String) entryDeviceList.get(0).getByName("PRICE_PLAN");
    }

    @Override
    protected boolean doTask() throws Exception {
        DBAccess dbAccess = new DBAccess();

        try {
            dbAccess.open();

            // お客様情報を取得する
            customerDetail = dbAccess.getCustomerDetailController(customerID);

            // お客様情報が有効の場合
            if (customerDetail != null) {
                // お客様が登録可能な機器一覧を取得する
                entryDeviceList = dbAccess.getEntryDeviceControllerList(customerID);
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
