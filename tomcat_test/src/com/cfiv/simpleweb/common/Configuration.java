/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.common;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author tanimura
 */
public class Configuration {
    /**
     * コンフィグクラス
     */
    private static final Properties Config = new Properties();

    /**
     * コンフィグ設定／取得キー
     */
    public static final String KEY_DBURL = "DB_URL";
    public static final String KEY_DBUSER = "DB_USER";
    public static final String KEY_DBPASSWORD = "DB_PASSWORD";
    public static final String KEY_HTTPPROTOCOL = "HTTP_PROTOCOL";

    static {
        loadConfig();
    }

    /**
     * コンフィグファイル読み込み
     */
    public static void loadConfig() {
        try {
            Config.load(new FileInputStream(Defines.CONFIG_FILENAME));
        }
        catch (Exception e) {
            Log.log(Log.ERROR, "コンフィグファイルの読み込みに失敗しました：コンフィグファイル名＝" + Defines.CONFIG_FILENAME);
            Log.printStackTrace(e);
        }
    }

    /**
     * 設定値通知
     * @param key キー文字列
     * @return String 設定値
     */
    public static String getProperty(String key) {
        if (Config.containsKey(key)) {
            return Config.getProperty(key);
        }
        else {
            Log.log(Log.ERROR, "キーがありません：キー＝" + key);
            return "";
        }
    }

    public static String getDBURL() {
        return getProperty(KEY_DBURL);
    }

    public static String getDBUser() {
        return getProperty(KEY_DBUSER);
    }

    public static String getDBPassword() {
        return getProperty(KEY_DBPASSWORD);
    }

    public static String getHTTPProtocol() {
        return getProperty(KEY_HTTPPROTOCOL);
    }
}