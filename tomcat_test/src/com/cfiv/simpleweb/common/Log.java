package com.cfiv.simpleweb.common;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * <p>
 * タイトル: ログ出力クラス</p>
 * <p>
 * 説明: log4jを使用してログ出力を行うクラス</p>
 * <p>
 * 著作権: Copyright (c) 2012</p>
 * <p>
 * 会社名: </p>
 *
 * @author Tanimura
 * @version 1.0
 */
public class Log {

    /**
     * Log4jカテゴリ
     */
    private static final Logger Log = Logger.getLogger(Log.class.getName());

    /**
     * ログレベル：FATAL
     */
    public static final int FATAL = 0;

    /**
     * ログレベル：ERROR
     */
    public static final int ERROR = 1;

    /**
     * ログレベル：WARN
     */
    public static final int WARN = 2;

    /**
     * ログレベル：INFO
     */
    public static final int INFO = 3;

    /**
     * ログレベル：DEBUG
     */
    public static final int DEBUG = 4;

    /**
     * コンストラクタ
     */
    public Log() {
    }

    /**
     * ログ出力の初期化
     */
    public static void initializeLog() {
        // 設定ファイルを読み込む
        DOMConfigurator.configure("log4j.xml");
//        ClassLoader loader = Thread.currentThread().getContextClassLoader();
//        DOMConfigurator.configure(loader.getResource("log4j.properties"));
    }

    /**
     * ログ出力
     *
     * @param level ログ出力レベル
     * @param message ログ出力文字列
     */
    public static void log(int level, String message) {
        switch (level) {
        case FATAL:
            Log.fatal(message);
            break;

        case ERROR:
            Log.error(message);
            break;

        case WARN:
            Log.warn(message);
            break;

        case INFO:
            Log.info(message);
            break;

        case DEBUG:
            Log.debug(message);
            break;

        default:
            break;
        }
    }

    /**
     * 例外クラスのスタックトレースログ出力(Errorレベル出力)
     *
     * @param e 例外クラス
     */
    public static void printStackTrace(Exception e) {
        printStackTrace(ERROR, e);
    }

    /**
     * 例外クラスのスタックトレースログ出力(指定レベル)
     *
     * @param level ログ出力レベル
     * @param e 例外クラス
     */
    public static void printStackTrace(int level, Exception e) {
        log(level, e.toString());

        StackTraceElement[] list = e.getStackTrace();
        for (StackTraceElement list1 : list) {
            log(level, "  " + list1.toString());
        }
    }
}
