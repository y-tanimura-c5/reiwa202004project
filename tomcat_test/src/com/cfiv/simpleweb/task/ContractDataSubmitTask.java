/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.dbaccess.DBAccess;

/**
 *
 * @author tanimura
 */
public class ContractDataSubmitTask extends AbstractTask {
    private List<Map<String, String>> contractDataList = new ArrayList<>();

    public ContractDataSubmitTask(List<Map<String, String>> list) {
        contractDataList = list;
    }

    @Override
    protected boolean doTask() throws Exception {
        DBAccess dbAccess = new DBAccess();

        try {
            dbAccess.open();

            // 契約情報を取得する
            dbAccess.setContractDataList(contractDataList);
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
}
