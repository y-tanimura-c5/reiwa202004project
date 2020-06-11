/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import com.cfiv.simpleweb.common.FormatDate;
import com.cfiv.simpleweb.common.Log;

/**
 *
 * @author tanimura
 */
public class DBAccessCore {

    /**
     * 接続オブジェクト
     */
    private Connection DBConnection = null;

    /**
     * バッファ
     */
    private Statement DBStatement = null;

    /**
     * タイムアウト値(秒)
     */
    private static final int TIMEOUT_LOGIN = 10;
    private static final int TIMEOUT_QUERY = 100;

    /**
     * DBとの接続
     *
     * @param driver データベースドライバ
     * @param url データベース接続先
     * @param user データベース接続ユーザ
     * @param pass データベース接続パスワード
     * @throws Exception コネクション生成失敗
     */
    public void open(String driver, String url, String user, String pass) throws Exception {
        // データベースの指定
        Class.forName(driver);

        // データベースとの接続
        DriverManager.setLoginTimeout(TIMEOUT_LOGIN);
        Log.log(Log.INFO, "url = " + url + ", user = " + user + ", pass = " + pass);
        DBConnection = DriverManager.getConnection(url, user, pass);
        DBConnection.setAutoCommit(false);
        DBStatement = DBConnection.createStatement();
        DBStatement.setQueryTimeout(TIMEOUT_QUERY);

        if (DBStatement == null) {
            throw new Exception("Statement Create Failed : " + url);
        }
    }

    /**
     * DB接続中かどうかの判定
     *
     * @return boolean 判定結果(true:接続中／false:切断中)
     * @throws SQLException 接続確認失敗
     */
    public boolean isOpened() throws SQLException {
        if (DBConnection == null) {
            return false;
        }

        if (DBStatement == null) {
            return false;
        }

        if (DBConnection.isClosed()) {
            return false;
        }

        return true;
    }

    /**
     * SQL文の実行
     *
     * @param sql SQL文
     * @return int insert／update／deleteの場合0を返す
     * @throws SQLException SQL文実行失敗
     */
    public int executeUpdate(String sql) throws SQLException {
        return DBStatement.executeUpdate(sql);
    }

    /**
     * selectSQL文の実行
     *
     * @param sql SQL文
     * @return ResultSet SQL文実行結果
     * @throws SQLException SQL文実行失敗
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return DBStatement.executeQuery(sql);
    }

    /**
     * SQL文の実行
     *
     * @param sql SQL文
     * @return ResultSet SQL文実行結果(実行結果がない場合はnull)
     * @throws SQLException SQL文実行失敗
     */
    public ResultSet execute(String sql) throws SQLException {
        if (DBStatement.execute(sql)) {
            return DBStatement.getResultSet();
        }
        else {
            return null;
        }
    }

    /**
     * selectSQL文の実行
     *
     * @param control DBコントローラ
     * @return DBControllerList SQL文実行結果
     * @throws SQLException SQL文実行失敗
     */
    public DBControllerList executeQuery(DBController control) throws Exception {
        DBControllerList result = new DBControllerList();

        PreparedStatement prepared = createPreparedStatement(control);

        ResultSet rs = prepared.executeQuery();

        while (rs.next()) {
            // SQL文実行結果をレコードデータに変換し、実行結果リストに追加する
            result.add(createController(rs));
        }

        rs.close();
        prepared.close();

        return result;
    }

    /**
     * SQL文の実行
     *
     * @param controls DB操作データリスト
     * @return int 更新件数
     * @throws Exception SQL文実行失敗
     */
    public int execute(DBControllerList controls) throws Exception {
        int count = 0;

        // 取得したレコードデータ数分繰り返す
        for (int i = 0; i < controls.size(); i++) {
            execute(controls.get(i));
            count++;
        }

        return count;
    }

    /**
     * PreparedStatementSQL文の実行
     *
     * @param control DB操作データ
     * @throws Exception SQL文実行失敗
     */
    public void execute(DBController control) throws Exception {
        PreparedStatement prepared;
        prepared = createPreparedStatement(control);

        // レコード追加
        prepared.executeUpdate();
        prepared.close();
    }

    /**
     * テーブルメタデータの取得
     *
     * @param table テーブル名
     * @return ResultSetMetaData テーブルメタデータ
     * @throws Exception SQL文実行失敗
     */
    public ResultSetMetaData getTableMetaData(String table) throws Exception {
        ResultSetMetaData data = null;

        try {
            // SQL文の作成
            String sql = "select * from MT_CUSTOMER;";
            //      String sql = "select first 1 * from \"" + table + "\";";

            ResultSet rs = executeQuery(sql);

            // ResultSetにアクセスしないとStatementを解放しないため、
            // このタイミングでダミーのアクセスを行う。
            rs.next();

            // メタデータを取得する
            data = rs.getMetaData();

            // 結果をクローズする
            rs.close();
        }
        catch (Exception e) {
            // DBアクセス時にExceptionが発生した場合、DBをいったん切断しExceptionをスローする
            close();

            throw e;
        }

        return data;
    }

    /**
     * テーブルメタデータの取得
     *
     * @param sql SQL文
     * @return ResultSetMetaData テーブルメタデータ
     * @throws Exception SQL文実行失敗
     */
    public ResultSetMetaData getTableMetaDataFromSQL(String sql) throws Exception {
        ResultSetMetaData data = null;

        try {
            ResultSet rs = executeQuery(sql);

            // ResultSetにアクセスしないとStatementを解放しないため、
            // このタイミングでダミーのアクセスを行う。
            rs.next();

            // メタデータを取得する
            data = rs.getMetaData();

            // 結果をクローズする
            rs.close();
        }
        catch (Exception e) {
            // DBアクセス時にExceptionが発生した場合、DBをいったん切断しExceptionをスローする
            close();

            throw e;
        }

        return data;
    }

    /**
     * DBの切断
     */
    public void close() {
        try {
            if (DBStatement != null) {
                DBStatement.close();
            }
        }
        catch (Exception e) {
            System.out.println("DBStatement Close Failed [ErrorMessage : " + e.getMessage() + "]");
        }

        try {
            if (DBConnection != null) {
                DBConnection.close();
            }
        }
        catch (Exception e) {
            System.out.println("DBConnection Close Failed [ErrorMessage : " + e.getMessage() + "]");
        }

        DBStatement = null;
        DBConnection = null;
    }

    /**
     * DBのコミット
     *
     * @throws SQLException コミット失敗
     */
    public void commit() throws SQLException {
        DBConnection.commit();
    }

    /**
     * DBのロールバック
     */
    public void rollback() {
        try {
            DBConnection.rollback();
        }
        catch (Exception e) {
            System.out.println("DB Rollback Failed [ErrorMessage : " + e.getMessage() + "]");
        }
    }

    /**
     * PreparedStatementの作成
     *
     * @param DBController control DB操作データ
     * @return PreparedStatement ステートメント
     * @throws Exception SQL文実行失敗
     */
    private PreparedStatement createPreparedStatement(DBController control) throws Exception {
        Object obj;
        ArrayList<Object> list = control.getDataList();

        // SQL文を取得する
        String sql = control.getPreparedStatementSQL();

        // PreparedStatement構築
        PreparedStatement prepared = DBConnection.prepareStatement(sql);
        prepared.setQueryTimeout(TIMEOUT_QUERY);

        // カラム数ループ
        for (int i = 0; i < list.size(); i++) {
            obj = list.get(i);

            // 取得したデータが文字列の場合
            if (obj instanceof String) {
                // 文字列としてデータ設定する
                String str = (String) obj;
                prepared.setString(i + 1, str);
            } // 取得したデータがIntegerの場合
            else if (obj instanceof Integer) {
                // Integerとしてデータ設定する
                Integer integerData = (Integer) obj;
                prepared.setInt(i + 1, integerData);
            } // 取得したデータがLongの場合
            else if (obj instanceof Long) {
                // Longとしてデータ設定する
                Long longData = (Long) obj;
                prepared.setLong(i + 1, longData);
            } // 取得したデータがBSDateの場合
            else if (obj instanceof FormatDate) {
                // Timestampとしてデータ設定する
                Date dateData = (Date) obj;
                Timestamp timestampData = new Timestamp(dateData.getTime());
                prepared.setTimestamp(i + 1, timestampData);
            }
        }

        return prepared;
    }

    /**
     * 結果セットからデータマップを作成する
     *
     * @param ResultSet rs 結果セット
     * @return JADBSelectController レコードデータ
     * @throws Exception レコードデータ作成失敗
     */
    private DBController createController(ResultSet rs) throws Exception {
        DBTableInformation info = new DBTableInformation();
        ArrayList<Object> list = new ArrayList<>();

        StringBuilder log = new StringBuilder();

        try {
            // 取得カラム数を取得する
            int count = rs.getMetaData().getColumnCount();

            // 1レコード分のデータからカラムごとのデータを取得する
            for (int i = 1; i <= count; i++) {
                int type = rs.getMetaData().getColumnType(i);
                String name = rs.getMetaData().getColumnName(i);
                DBColumnInformation column = new DBColumnInformation(type, i, name);

                // テーブル情報およびデータリストにカラム情報とデータを追加する
                info.addColumn(column);
                list.add(convertColumnData(rs, column));
                log.append(convertColumnData(rs, column)).append(", ");
            }
        }
        catch (Exception e) {
            throw e;
        }

        return new DBController(info, list);
    }

    /**
     * シングルクオートの追加
     *
     * @param byte bytedata 変換元データ
     * @param int figure 桁数
     * @return String 変換後データ()
     */
    private String addQuote(String str) {
        if (str != null) {
            StringBuilder s = new StringBuilder(str);

            // シングルクオートがある場合、その文字の後ろにシングルクオートを追加する
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '\'') {
                    s.insert(i++, '\'');
                }
            }

            return s.toString();
        }
        else {
            return null;
        }
    }

    /**
     * DB検索結果のカラムデータをクラスに変換
     *
     * @param ResultSet rs 結果セット
     * @param BSColumnInformation column カラム情報
     * @return Object 変換後データ
     */
    private Object convertColumnData(ResultSet rs, DBColumnInformation column) {
        Object result = null;

        try {
            if (column.Type == Types.INTEGER) {
                result = rs.getInt(column.Index);
            }
            else if (column.Type == Types.NUMERIC) {
                result = rs.getInt(column.Index);
            }
            else if (column.Type == Types.SMALLINT) {
                result = rs.getInt(column.Index);
            }
            else if (column.Type == Types.VARCHAR) {
                result = addQuote(rs.getString(column.Index));
            }
            else if (column.Type == Types.LONGVARCHAR) {
                result = addQuote(rs.getString(column.Index));
            }
            else if (column.Type == Types.TIMESTAMP) {
                Timestamp time = rs.getTimestamp(column.Index);

                if (time != null) {
                    result = new FormatDate(time.getTime());
                }
            }
            else if (column.Type == Types.DATE) {
                Date date = rs.getDate(column.Index);

                if (date != null) {
                    result = new FormatDate(date);
                }
            }
            else if (column.Type == Types.TIME) {
                Time time = rs.getTime(column.Index);

                if (time != null) {
                    result = new FormatDate(time.getTime());
                }
            } // LongBinaryはbyte列に変換
            else if (column.Type == Types.LONGVARBINARY) {
                byte[] bytes = null;

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                BufferedInputStream input = new BufferedInputStream(rs.getBinaryStream(column.Index));

                byte[] buff = new byte[1024];
                int read_length = 0;
                if (!rs.wasNull()) {
                    while (true) {
                        if ((read_length = input.read(buff, 0, buff.length)) != -1) {
                            output.write(buff, 0, read_length);
                        }
                        else {
                            bytes = output.toByteArray();
                            break;
                        }
                    }

                    result = bytes;
                }
            }
        }
        catch (SQLException | IOException e) {
            result = null;
        }

        return result;
    }
}
