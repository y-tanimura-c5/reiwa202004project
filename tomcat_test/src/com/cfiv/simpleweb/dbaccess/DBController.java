/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

import java.util.ArrayList;

/**
 * <p>タイトル: DB操作クラス</p>
 * <p>説明: 指定テーブルの操作データを格納するクラス</p>
 * <p>著作権: Copyright (c) 2012</p>
 * <p>会社名: </p>
 * @author Tanimura
 * @version 1.0
 */
public class DBController {
    /**
    * テーブル情報
    */
    private DBTableInformation TableInformation = null;

    /**
     * データリスト
     */
    private ArrayList<Object> DataList = new ArrayList<>();

    /**
     * SQL文
     */
    private String PreparedSQL = "";

    /**
     * コンストラクタ
     */
    public DBController() {
    }

    /**
     * コンストラクタ
     * @param info テーブル情報
     * @throws Exception テーブル情報がnull
     */
    public DBController(DBTableInformation info) throws Exception {
        TableInformation = info;

        // テーブル情報がnullの場合、Exceptionをスロー
        if (TableInformation == null) {
            throw new IllegalArgumentException("TableInformation Is Null");
        }
    }

    /**
     * コンストラクタ
     * @param info テーブル情報
     * @param obj データ
     * @throws Exception テーブル情報がnull
     */
    public DBController(DBTableInformation info, Object obj) throws Exception {
        TableInformation = info;

        if (obj != null) {
            DataList = new ArrayList<>();
            DataList.add(obj);
        }

        // テーブル情報がnullの場合、Exceptionをスロー
        if (TableInformation == null) {
            throw new IllegalArgumentException("TableInformation Is Null");
        }
    }

    /**
     * コンストラクタ
     * @param info テーブル情報
     * @param list データリスト
     * @throws Exception テーブル情報がnull
     */
    public DBController(DBTableInformation info, ArrayList<Object> list) throws Exception {
        TableInformation = info;

        if (list != null) {
            DataList = list;
        }

        // テーブル情報がnullの場合、Exceptionをスロー
        if (TableInformation == null) {
            throw new IllegalArgumentException("TableInformation Is Null");
        }
    }

    /**
     * SQL文の設定
     * @param sql SQL文(PreparedStatement用)
     */
    public void setPreparedStatementSQL(String sql) {
        PreparedSQL = sql;
    }

    /**
     * SQL文の取得
     * @return String SQL文(PreparedStatement用)
     */
    public String getPreparedStatementSQL() {
        return PreparedSQL;
    }

    /**
     * カラム名指定でのデータ取得
     * @param name カラム名
     * @return Object データ
     */
    public Object getByName(String name) {
        Object result = null;
        int num = -1;

        for (int i = 0; i < TableInformation.getColumnNum(); i++) {
            // カラム名に対応する番号を取得する
            if (TableInformation.getColumn(i).Name.toUpperCase().equals(name.toUpperCase())) {
                num = i;
                break;
            }
        }

        // カラム名に一致するデータが存在する場合
        if (num >= 0) {
            // データを取得する
            result = DataList.get(num);
        }

        return result;
    }

    /**
     * インデックス指定でのデータ取得
     * @param index インデックス
     * @return Object データ
     */
    public Object getByIndex(int index) {
        Object result = null;

        // データリストの範囲内の場合
        if (0 <= index && index < DataList.size()) {
            // データを取得する
            result = DataList.get(index);
        }

        return result;
    }

    /**
     * データ数の通知
     * @return int データ数
     */
    public int size() {
        return DataList.size();
    }

    /**
     * @return tableInformation
     */
    public DBTableInformation getTableInformation() {
        return TableInformation;
    }

    /**
     * @return dataList
     */
    public ArrayList<Object> getDataList() {
        return DataList;
    }
}
