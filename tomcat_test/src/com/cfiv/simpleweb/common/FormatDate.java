/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * タイトル: 時刻クラス</p>
 * <p>
 * 説明: 時刻の取得および管理を行うクラス</p>
 * <p>
 * 著作権: Copyright (c) 2013</p>
 * <p>
 * 会社名: </p>
 *
 * @author Tanimura
 * @version 1.0
 */
public class FormatDate extends Date {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = -5591153211160842688L;

    /**
     * 時刻フォーマット
     */
    private static final String FORMAT_YYYYMMDDHHMMSS = "yyyy/MM/dd HH:mm:ss";

    /**
     * 年月フォーマット
     */
    private static final String FORMAT_JPYYYYM = "yyyy年M月";

    /**
     * 年月日フォーマット
     */
    private static final String FORMAT_JPYYYYMD = "yyyy年M月d日";

    /**
     * コンストラクタ
     */
    public FormatDate() {
        super();
    }

    /**
     * コンストラクタ
     *
     * @param date ミリ秒
     */
    public FormatDate(long date) {
        super(date);
    }

    /**
     * コンストラクタ
     *
     * @param date Dateクラス
     */
    public FormatDate(Date date) {
        this(date.getTime());
    }

    /**
     * フォーマット指定での時刻文字列取得
     *
     * @param format フォーマット
     * @return String 時刻文字列
     */
    public String toString(String format) {
        return toString(this, format);
    }

    /**
     * フォーマット指定での時刻文字列取得
     *
     * @param format フォーマット
     * @return String 時刻文字列
     */
    public String toString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setLenient(true);
        sdf.applyPattern(format);

        return sdf.format(date);
    }

    /**
     * 「yyyy/MM/dd HH:mm:ss」形式での時刻文字列取得
     *
     * @return 時刻文字列
     */
    public String toDateString() {
        return toString(FORMAT_YYYYMMDDHHMMSS);
    }

    /**
     * 「yyyy年M月」形式での時刻文字列取得
     *
     * @return 時刻文字列
     */
    public String toJPMonthString() {
        return toString(FORMAT_JPYYYYM);
    }

    /**
     * 時刻文字列からのBSDateインスタンス生成
     *
     * @param str 時刻文字列
     * @return JADate JADateインスタンス
     */
    public static FormatDate parseDate(String str) {
        return parseDate(FORMAT_YYYYMMDDHHMMSS, str);
    }

    /**
     * 時刻文字列からのBSDateインスタンス生成
     *
     * @param format フォーマット文字列
     * @param str 時刻文字列
     * @return JADate JADateインスタンス
     */
    public static FormatDate parseDate(String format, String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            return new FormatDate(sdf.parse(str));
        }
        catch (Exception e) {
            // 時刻文字列の読み込みに失敗した場合、現在時刻を返す
            return new FormatDate();
        }
    }

    /**
     * 前月末日の「yyyy年M月D日」形式での時刻文字列取得
     *
     * @return 時刻文字列
     */
    public String getLastMonthLastDayString() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

        return toString(cal.getTime(), FORMAT_JPYYYYMD);
    }
}
