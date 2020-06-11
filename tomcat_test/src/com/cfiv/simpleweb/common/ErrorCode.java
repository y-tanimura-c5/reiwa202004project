package com.cfiv.simpleweb.common;

/**
 * エラーコード列挙型クラス
 *
 * @author katom
 */
public enum ErrorCode {
    /** 1xx : データベース関連エラー */
    DB_CONNECT(101, "データベース接続に失敗しました。"),
    DB_ACCESS(102, "データベースアクセスに失敗しました。"),

    /** 2xx : ファイルIO関連エラー */
    FILE_UPLOAD(201, "ファイルアップロードに失敗しました。"),
    FILE_DOWNLOAD(202, "ファイルダウンロードに失敗しました。"),
    FILE_ACCESS(203, "ファイルアクセスに失敗しました。"),

    /** 3xx : 不正値検出関連エラー */
    PARAMETER(301, "パラメータが不正です。"),

    /** 4xx : 認証関連エラー */
    AUTH_ERROR(401, "ページの参照権限がありません。"),

    /** 5xx : セッションエラー */
    SESSION_ERROR(501, "セッションエラー。"),

    /** 9xx : システム内部の問題に起因するエラー */
    UNKNOWN(901, "システムエラーが発生しました。"),
    ;

    private final int code;
    private final String msg;

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * エラーコードを取得します。
     *
     * @return エラーコード
     */
    public int getCode() {
        return code;
    }

    /**
     * エラーメッセージを取得します。
     *
     * @return エラーメッセージ
     */
    public String getMsg() {
        return msg;
    }
}
