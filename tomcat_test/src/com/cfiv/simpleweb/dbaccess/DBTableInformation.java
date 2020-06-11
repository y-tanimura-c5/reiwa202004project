/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.cfiv.simpleweb.common.Util;

/**
 * <p>タイトル: テーブル情報クラス</p>
 * <p>説明: テーブル情報(テーブル名、カラム情報等)を格納するクラス</p>
 * <p>著作権: Copyright (c) 2012</p>
 * <p>会社名: </p>
 * @author Tanimura
 * @version 1.0
 */
public class DBTableInformation {
    /**
     * テーブル名
     */
    protected String TableName = "";

    /**
     * プライマリキー名
     */
    protected String PKName = "";

    /**
     * カラム情報リスト
     */
    protected ArrayList<DBColumnInformation> ColumnList = new ArrayList<>();

    /**
     * コンストラクタ
     */
    public DBTableInformation() {
    }

    /**
     * コンストラクタ
     * @param metadata テーブルメタデータ
     * @param table テーブル名
     * @param pk プライマリキー名
     * @throws Exception テーブルアクセスに失敗した、またはメタデータ取得に失敗した場合
     */
    public DBTableInformation(ResultSetMetaData metadata, String table, String pk) throws Exception {
        DBColumnInformation pk_column = null;

        TableName = table.trim();
        PKName = pk.trim();

        // テーブル名、プライマリキー名のいずれかが空白の場合はExceptionを発生させる
        if (Util.isEmpty(TableName)) {
            throw new IllegalArgumentException("Table Name Is Wrong");
        }

        // カラム情報リストを作成する
        // リストの設定順序は以下の通りとする
        // ・カラムの追加順序はプライマリキーを除いてテーブル上の並び順とする
        // ・プライマリキーカラムが最後とする

        // 結果セットのメタデータから、カラム情報を生成する
        for (int i = 1; i <= metadata.getColumnCount(); i++) {
            DBColumnInformation column = new DBColumnInformation(metadata.getColumnType(i), i,
                    metadata.getColumnName(i));

            // プライマリキーカラムは最後にしたいので、この時点ではリストには追加しない
            if (column.Name.equals(PKName)) {
                pk_column = column;
            }
            else {
                ColumnList.add(column);
            }
        }

        // プライマリキーカラムが存在する場合、リストの最後に追加
        if (pk_column != null) {
            ColumnList.add(pk_column);
        }
    }

    /**
     * カラム情報の取得
     * @param index インデックス
     * @return DBColumnInformation カラム情報
     */
    public DBColumnInformation getColumn(int index) {
        return (DBColumnInformation) ColumnList.get(index);
    }

    /**
     * カラム数の取得
     * @return int カラム数
     */
    public int getColumnNum() {
        return ColumnList.size();
    }

    /**
     * カラム情報の追加
     * @param column カラム情報
     */
    public void addColumn(DBColumnInformation column) {
        ColumnList.add(column);
    }

    /**
     * insertSQL文の取得
     * @return String insertSQL文(PreparedStatment用)
     */
    public String getPreparedStatmentInsertSQL() {
        StringBuilder column_names = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (int i = 0; i < getColumnNum(); i++) {
            DBColumnInformation column = getColumn(i);

            if (i != 0) {
                column_names.append(",");
                values.append(",");
            }

            // カラム名をカラム名リストに追加する
            column_names.append("\"").append(column.Name).append("\"");

            // 設定値を設定値リストに追加する
            values.append("?");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("insert into \"").append(TableName).append("\" (");
        sql.append(column_names);
        sql.append(") values (");
        sql.append(values);
        sql.append(")");

        return sql.toString();
    }

    /**
     * updateSQL文の取得
     * @return String updateSQL文(PreparedStatment用)
     */
    public String getPreparedStatmentUpdateSQL() {
        StringBuilder sql = new StringBuilder();

        sql.append("update \"").append(TableName).append("\" set ");

        // 最後のプライマリキーを除いて、更新対象データとする
        for (int i = 0; i < getColumnNum() - 1; i++) {
            DBColumnInformation column = getColumn(i);

            if (i != 0) {
                sql.append(",");
            }

            sql.append("\"").append(column.Name).append("\" = ?");
        }

        // 更新対象のプライマリキーを設定する
        sql.append(" where \"").append(PKName).append("\" = ?");

        return sql.toString();
    }

    /**
     * deleteSQL文の取得
     * @return String deleteSQL文(PreparedStatment用)
     */
    public String getPreparedStatmentDeleteSQL() {
        return "delete from \"" + TableName + "\" where \"" + PKName + "\" = ?;";
    }

    /**
     * @return tableName
     */
    public String getTableName() {
        return TableName;
    }

    /**
     * @return pKName
     */
    public String getPKName() {
        return PKName;
    }
}
