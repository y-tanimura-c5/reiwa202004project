/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.task;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfiv.simpleweb.common.FormatDate;
import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.dbaccess.DBAccess;
import com.cfiv.simpleweb.dbaccess.DBControllerList;

/**
 *
 * @author tanimura
 */
public class ContractDataListTask extends AbstractTask {
    private List<Map<String, String>> contractDataList = new ArrayList<>();
    private FormatDate lastUpdateDate = new FormatDate(0);
    private String customerID;

    public ContractDataListTask(String id) {
        customerID = id;
    }

    public List<Map<String, String>> getContractDataList() {
        return contractDataList;
    }

    public String getUpdateDateString() {
        return lastUpdateDate.getLastMonthLastDayString();
    }

    @Override
    protected boolean doTask() throws Exception {
        DBAccess dbAccess = new DBAccess();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        try {
            dbAccess.open();

            // 契約情報を取得する
            DBControllerList contractDetailList = dbAccess.getContractControllerList(customerID);

            // 契約情報が存在する場合
            if (contractDetailList != null) {
                for (int i = 0; i < contractDetailList.size(); i++) {
                    Map<String, String> data = new HashMap<>();

                    // お客様IDの設定
                    data.put("CUSTOMER_ID", (String) contractDetailList.get(i).getByName("CUSTOMER_ID"));

                    // 契約名義の設定
                    data.put("CONTRACT_NAME", (String) contractDetailList.get(i).getByName("CONTRACT_NAME"));

                    // 機器電話番号の設定
                    data.put("DEVICE_TELNO", (String) contractDetailList.get(i).getByName("DEVICE_TELNO"));

                    // 現行機種名称の設定
                    data.put("DEVICE_NAME", (String) contractDetailList.get(i).getByName("DEVICE_NAME"));

                    // 契約月の設定
                    data.put("START_MONTH",
                            ((FormatDate) contractDetailList.get(i).getByName("START_MONTH")).toJPMonthString());

                    // 満了月の設定
                    data.put("END_MONTH",
                            ((FormatDate) contractDetailList.get(i).getByName("END_MONTH")).toJPMonthString());

                    // 現行利用料金の設定
                    data.put("NOW_PRICE",
                            currencyFormat.format((Integer) contractDetailList.get(i).getByName("NOW_PRICE")));

                    // 継続時利用料金の設定
                    data.put("CONTINUATION_PRICE",
                            currencyFormat.format((Integer) contractDetailList.get(i).getByName("CONTINUATION_PRICE")));

                    // 機変後利用料金の設定
                    data.put("RENEWAL_PRICE",
                            currencyFormat.format((Integer) contractDetailList.get(i).getByName("RENEWAL_PRICE")));

                    // 更新日付を取得し、最新の更新日付を保持する
                    FormatDate updateDate = (FormatDate) contractDetailList.get(i).getByName("UPDATE_DATE");
                    if (lastUpdateDate.getTime() < updateDate.getTime()) {
                        lastUpdateDate = updateDate;
                    }

                    // リストに追加する
                    contractDataList.add(data);
                }
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
        return "ContractDetailListTask : Size = " + contractDataList.size();
    }
}