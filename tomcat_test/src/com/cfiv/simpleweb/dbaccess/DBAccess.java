/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cfiv.simpleweb.common.FormatDate;
import com.cfiv.simpleweb.common.Util;

/**
 * <p>
 * タイトル: DBアクセスクラス</p>
 * <p>
 * 説明: DBへのアクセスを行う抽象クラス</p>
 * <p>
 * 著作権: Copyright (c) 2012</p>
 * <p>
 * 会社名: </p>
 *
 * @author Tanimura
 * @version 1.0
 */
public class DBAccess {

    /**
     * DBアクセスクラス
     */
    private final DBAccessCore dbAccessCore = new DBAccessCore();

    /**
     * DBへの接続
     *
     * @throws Exception DB接続失敗
     */
    public void open() throws Exception {
        // DB未接続の場合
        if (!dbAccessCore.isOpened()) {
            // DBへの接続を行う
            DBParameter param = new DBParameter();
            dbAccessCore.open(param.getDriver(), param.getURL(), param.getUser(), param.getPassword());
        }
    }

    /**
     * DBとの切断
     *
     * @throws SQLException 切断失敗
     */
    public void close() throws SQLException {
        dbAccessCore.close();
    }

    /**
     * DBのコミット
     *
     * @throws SQLException 切断失敗
     */
    public void commit() throws SQLException {
        dbAccessCore.commit();
    }

    /**
     * DBのロールバック
     *
     * @throws SQLException 切断失敗
     */
    public void rollback() throws SQLException {
        dbAccessCore.rollback();
    }

    /**
     * ユーザ情報取得
     *
     * @param userName ユーザ名
     * @return DBController ユーザ情報
     * @throws SQLException
     */
    public DBController getCustomerDetailController(String userName) throws Exception {
        DBController result = null;
        String tableName = "MT_CUSTOMER";
        String pkName = "CUSTOMER_ID";
        String selectSQL = "select * from " + tableName + " where " + pkName + " = ?;";

        if (dbAccessCore.isOpened()) {
            // ユーザ管理テーブル情報作成
            ResultSetMetaData meta = dbAccessCore.getTableMetaData(tableName);
            DBTableInformation info = new DBTableInformation(meta, tableName, pkName);

            // ユーザ情報を取得する
            DBController control = new DBController(info, userName);
            control.setPreparedStatementSQL(selectSQL);
            DBControllerList list = dbAccessCore.executeQuery(control);

            // ユーザが存在する場合、当該ユーザ情報を保持する
            if (list.size() != 0) {
                result = list.get(0);
            }
        }

        return result;
    }

    /**
     * 登録可能機種取得
     *
     * @param customerID お客様ID
     * @return DBControllerList 登録可能機種一覧
     * @throws SQLException
     */
    public DBControllerList getEntryDeviceControllerList(String customerID) throws Exception {
        DBControllerList result = null;
        String tableName = "MT_ENTRYDEVICE";
        String pkName = "CUSTOMER_ID";
        String selectSQL = "select CUSTOMER_ID, DEVICE_NAME, STORAGE_SIZE, COLOR_NAME, CONTRACT_KIND, CONTRACT_SPAN, PRICE_PLAN "
                +
                "from " +
                "  ((((MT_ENTRYDEVICE E left join MT_DEVICE D on E.DEVICE_CODE = D.DEVICE_CODE) " +
                "    left join MT_COLORNAME C on D.COLORNAME_CODE = C.COLORNAME_CODE) " +
                "      left join MT_CONTRACTKIND K on D.CONTRACTKIND_CODE = K.CONTRACTKIND_CODE) " +
                "        left join MT_PRICEPLAN P on D.PRICEPLAN_CODE = P.PRICEPLAN_CODE) " +
                "where " + pkName + " =  ";

        if (dbAccessCore.isOpened()) {
            // ユーザ管理テーブル情報作成
            ResultSetMetaData meta = dbAccessCore.getTableMetaDataFromSQL(selectSQL + "'" + customerID + "';");
            DBTableInformation info = new DBTableInformation(meta, tableName, pkName);

            // 登録可能機種情報を取得する
            DBController control = new DBController(info, customerID);
            control.setPreparedStatementSQL(selectSQL + "?;");
            result = dbAccessCore.executeQuery(control);
        }

        return result;
    }

    /**
     * 契約情報一覧取得
     *
     * @param customerID お客様ID
     * @return DBControllerList 契約情報一覧
     * @throws SQLException
     */
    public DBControllerList getContractControllerList(String customerID) throws Exception {
        DBControllerList result = null;
        String tableName = "DT_CONTRACT";
        String pkName = "CUSTOMER_ID";
        String selectSQL = "select * from " + tableName + " where " + pkName + " = ? order by END_MONTH;";

        if (dbAccessCore.isOpened()) {
            // 契約情報テーブル情報作成
            ResultSetMetaData meta = dbAccessCore.getTableMetaData(tableName);
            DBTableInformation info = new DBTableInformation(meta, tableName, pkName);

            // 契約情報を取得する
            DBController control = new DBController(info, customerID);
            control.setPreparedStatementSQL(selectSQL);
            result = dbAccessCore.executeQuery(control);
        }

        return result;
    }

    /**
     * メニューコメント取得
     *
     * @param userKind お客様ID
     * @return DBController メニューコメント
     * @throws SQLException
     */
    public DBController getMenuCommentController(int userKind) throws Exception {
        DBController result = null;
        String tableName = "MT_MENUCOMMENT";
        String pkName = "MENUCOMMENT_KIND";
        String selectSQL = "select * from " + tableName + " where " + pkName + " = ?;";

        if (dbAccessCore.isOpened()) {
            // メニューコメントテーブル情報作成
            ResultSetMetaData meta = dbAccessCore.getTableMetaData(tableName);
            DBTableInformation info = new DBTableInformation(meta, tableName, pkName);

            // メニューコメントを取得する
            DBController control = new DBController(info, userKind);
            control.setPreparedStatementSQL(selectSQL);
            DBControllerList list = dbAccessCore.executeQuery(control);

            // ユーザが存在する場合、当該ユーザ情報を保持する
            if (list.size() != 0) {
                result = list.get(0);
            }
        }

        return result;
    }

    /**
     * 契約情報登録
     *
     * @param list 契約情報一覧
     * @throws SQLException
     */
    public void setContractDataList(List<Map<String, String>> list) throws Exception {
        String tableName = "DT_CONTRACT";
        String pkName = "CONTRACT_ID";
        String truncateSQL = "truncate table " + tableName + ";";
        String insertSQL = "insert into " + tableName + "(" +
                "CUSTOMER_ID, CONTRACT_NAME, DEVICE_TELNO, DEVICE_NAME, START_MONTH, END_MONTH, " +
                "NOW_PRICE, CONTINUATION_PRICE, RENEWAL_PRICE, UPDATE_DATE) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        FormatDate currentTime = new FormatDate();

        if (dbAccessCore.isOpened()) {
            // 契約情報テーブル情報作成
            ResultSetMetaData meta = dbAccessCore.getTableMetaData(tableName);
            DBTableInformation info = new DBTableInformation(meta, tableName, pkName);

            // 契約情報の全削除
            DBController truncateControl = new DBController(info, null);
            truncateControl.setPreparedStatementSQL(truncateSQL);
            dbAccessCore.execute(truncateControl);

            // 取得した契約情報をすべて投入する
            for (Map<String, String> map : list) {
                ArrayList<Object> dataList = new ArrayList<>();
                dataList.add((String) map.get("CUSTOMER_ID"));
                dataList.add((String) map.get("CONTRACT_NAME"));
                dataList.add((String) map.get("DEVICE_TELNO"));
                dataList.add((String) map.get("DEVICE_NAME"));
                dataList.add(Util.getDateFromYYYYMString((String) map.get("START_MONTH")));
                dataList.add(Util.getDateFromYYYYMString((String) map.get("END_MONTH")));
                dataList.add(Util.getPriceFromString((String) map.get("NOW_PRICE")));
                dataList.add(Util.getPriceFromString((String) map.get("CONTINUATION_PRICE")));
                dataList.add(Util.getPriceFromString((String) map.get("RENEWAL_PRICE")));
                dataList.add(currentTime);

                DBController insertControl = new DBController(info, dataList);
                insertControl.setPreparedStatementSQL(insertSQL);
                dbAccessCore.execute(insertControl);
            }

            dbAccessCore.commit();
        }
    }
}
