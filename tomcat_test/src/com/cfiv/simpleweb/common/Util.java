/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @author tanimura
 */
public class Util {

    /**
     * 文字列が数値かどうか判定を行う
     *
     * @param str 判定元文字列
     * @return boolean 判定結果
     */
    public static boolean isNumeric(String str) {
        // 数値チェック
        try {
            Long.parseLong(str);
        }
        catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * 文字列が空白かどうかの判定を行う
     *
     * @param str 判定元文字列
     * @return boolean 判定結果
     */
    public static boolean isEmpty(String str) {
        if (str != null && !str.trim().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * 32桁のランダム数を生成する
     *
     * @return String ランダム値
     */
    public static String random32() {
        Random rnd = new Random();
        StringBuffer result = new StringBuffer();
        result.append(addZero(rnd.nextInt(100000000), 8));
        result.append(addZero(rnd.nextInt(100000000), 8));
        result.append(addZero(rnd.nextInt(100000000), 8));
        result.append(addZero(rnd.nextInt(100000000), 8));
        return result.toString();
    }

    /**
     * 左ゼロ埋め
     *
     * @param val 値
     * @param keta 桁
     * @return 左ゼロ埋めした文字列
     */
    public static String addZero(int val, int keta) {
        String v = String.valueOf(val);
        while (v.length() < keta) {
            v = "0" + v;
        }
        return v;
    }

    /**
     * 文字列をboolean値に変換
     *
     * @param str 判定元文字列
     * @return boolean 判定結果("1"または"true"文字列ならtrue／それ以外はfalseを返す)
     */
    public static boolean convertBoolean(String str) {
        if (str != null) {
            if (str.equals("1") || str.toLowerCase().equals("true")) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Integerをboolean値に変換
     *
     * @param value 判定値
     * @return 判定結果("1"または"true"文字列ならtrue／それ以外はfalseを返す)
     */
    public static boolean convertBoolean(Integer value) {
        return convertBoolean(value.toString());
    }

    /**
     * バイト列を16進文字列に変換
     *
     * @param arr バイト列
     * @return String 16進文字列
     */
    public static String toHexString(byte[] arr) {
        StringBuilder buff = new StringBuilder(arr.length * 2);

        for (int i = 0; i < arr.length; i++) {
            String b = Integer.toHexString(arr[i] & 0xff);

            if (b.length() == 1) {
                buff.append("0");
            }
            buff.append(b);
        }

        return buff.toString();
    }

    /**
     * where in 句で使用するパラメータ文字列((?,?,・・・))を返す
     *
     * @param paramNum パラメータ数
     * @return パラメータ文字列
     */
    public static String whereInParams(int paramNum) {
        StringBuffer result = new StringBuffer();

        result.append("(?");

        for (int i = 1; i < paramNum; i++) {
            result.append(",?");
        }

        result.append(")");

        return result.toString();
    }

    /**
     * 開始日～終了日の中の指定月の日付リストを返す(開始／終了が8/20～9/10で、指定月が9月の場合、1～10のリストが返る)
     *
     * @param month 月
     * @param beginDate 開始日
     * @param finishDate 終了日
     * @return 日付リスト
     */
    public static List<Integer> containsDayList(String month, FormatDate beginDate, FormatDate finishDate) {
        List<Integer> dayList = new ArrayList<>();

        // 月の開始日と最終日を取得
        FormatDate startDate = FormatDate.parseDate("yyyyMMdd", month + "01");
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) startDate);
        FormatDate endDate = FormatDate.parseDate("yyyyMMdd", month + cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        int startDay = 0;
        int endDay;
        if (beginDate.getTime() <= startDate.getTime()) {
            startDay = Integer.parseInt(startDate.toString("dd"));
        }
        else {
            startDay = Integer.parseInt(beginDate.toString("dd"));
        }

        if (finishDate.getTime() >= endDate.getTime()) {
            endDay = Integer.parseInt(endDate.toString("dd"));
        }
        else {
            endDay = Integer.parseInt(finishDate.toString("dd"));
        }

        for (int i = startDay; i <= endDay; i++) {
            dayList.add(i);
        }

        return dayList;
    }

    /**
     * 取得文字列を指定セパレータで分割してList形式で返す。
     *
     * @param str 文字列
     * @param sep セパレータ文字列
     * @return Listクラス
     */
    public static List<String> getStringList(String str, String sep) {
        if (isEmpty(str)) {
            return null;
        }

        List<String> list = new ArrayList<>();
        String[] array = str.split(sep);

        if (array.length == 1 && "".equals(array[0])) {
            return list;
        }

        for (String s : array) {
            list.add(s);
        }

        return list;
    }

    /**
     * 文字列配列をList形式に変換します。
     *
     * @param strs 文字列配列
     * @return Listクラス
     */
    public static List<String> convertStringList(String[] strs) {
        if (strs == null) {
            return null;
        }

        List<String> list = new ArrayList<>();
        for (String s : strs) {
            list.add(s);
        }

        return list;
    }

    /**
     * 取得文字列が数値またはハイフンのみで構成されているかチェックします。
     *
     * @param str チェック対象文字列
     * @return boolean チェック結果
     */
    public static boolean isDigitAndDash(String str) {
        if (isEmpty(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (!Character.isDigit(c) && c != '-') {
                return false;
            }
        }

        return true;
    }

    /**
     * 取得文字列がメールアドレス使用可能文字で構成されているかチェックします。
     *
     * @param str チェック対象文字列
     * @return boolean チェック結果
     */
    public static boolean canUseMailAddress(String str) {
        if (isEmpty(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (!Character.isDigit(c) &&
                    !Character.isAlphabetic(c) &&
                    c != '@' &&
                    c != '.' &&
                    c != '!' &&
                    c != '#' &&
                    c != '$' &&
                    c != '%' &&
                    c != '&' &&
                    c != '\'' &&
                    c != '*' &&
                    c != '+' &&
                    c != '-' &&
                    c != '/' &&
                    c != '=' &&
                    c != '?' &&
                    c != '^' &&
                    c != '_' &&
                    c != '`' &&
                    c != '{' &&
                    c != '|' &&
                    c != '}' &&
                    c != '~') {
                return false;
            }
        }

        return true;
    }

    /**
     * "YYYY年M月"フォーマット文字列から年と月の値を取得する
     * @param yyyym フォーマット文字列
     * @return FormatDate 日時
     */
    public static FormatDate getDateFromYYYYMString(String yyyym) {
        FormatDate result = null;
        String[] ySplited = yyyym.split("年");

        if (ySplited.length >= 2) {
            String yStr = ySplited[0];
            String[] mSplited = ySplited[1].split("月");

            if (mSplited.length >= 1) {
                String mStr = mSplited[0];

                try {
                    int year = Integer.parseInt(yStr);
                    int month = Integer.parseInt(mStr);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month - 1);
                    cal.set(Calendar.DATE, 1);
                    result = new FormatDate(cal.getTime());
                }
                catch (NumberFormatException e) {
                    // 無視
                }
            }
        }

        return result;
    }

    /**
     * 金額フォーマット文字列を数値に変換する
     * @param yen 金額フォーマット文字列
     * @return int 金額(エラーの場合は-1を返す)
     */
    public static int getPriceFromString(String yen) {
        int result = -1;

        if (isEmpty(yen)) {
            return result;
        }

        // 先頭が"\"の場合、"\"を削除する
        char c = yen.charAt(0);
        if (c == '\\' || c == '￥') {
            yen = yen.substring(1);
        }

        // 含まれているカンマを削除する
        yen = yen.replaceAll(",", "");

        try {
            result = Integer.parseInt(yen);
        }
        catch (NumberFormatException e) {
            result = -1;
        }

        return result;
    }
}
