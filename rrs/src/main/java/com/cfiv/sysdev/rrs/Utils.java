package com.cfiv.sysdev.rrs;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    /**
     * Long→指定文字数文字列形式
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public static String getStringFromLong(Long value, int nDigits) {
        return String.format("%0" + nDigits + "d", value);
    }

    /**
     * Integer→指定文字数文字列形式
     * @param nDigits 0埋め桁数
     * @return 指定桁で0埋め後のID文字列
     */
    public static String getStringFromInteger(Integer value, int nDigits) {
        return String.format("%0" + nDigits + "d", value);
    }

    /**
     * 文字列→Long形式
     * @param str 数値文字列
     * @return 数値(Long)
     */
    public static Long getLongFromString(String str) {
        try {
            return Long.parseLong(str);
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 文字列→Integer形式
     * @param str 数値文字列
     * @return 数値(Integer)
     */
    public static Integer getIntegerFromString(String str) {
        try {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Date→文字列(YYYY-MM-DD)形式
     * @param date 日時
     * @return YYYY-MM-DD形式文字列
     */
    public static String getStringDashFromDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * Date→文字列(YYYY/MM/DD)形式
     * @param date 日時
     * @return YYYY/MM/DD形式文字列
     */
    public static String getStringSlashFromDate(Date date) {
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    /**
     * Date→文字列(YYYYMMDDHHmmss)形式
     * @param date 日時
     * @return YYYYMMDDHHmmss形式文字列
     */
    public static String getYYYYMMDDHHmmssFromDate(Date date) {
        return new SimpleDateFormat("YYYYMMDDHHmmss").format(date);
    }

    /**
     * 現在年取得
     * @return 年
     */
    public static int getNowYear() {
        return getIntegerFromString(new SimpleDateFormat("YYYY").format(new Date()));
    }

    /**
     * 画像データ(JPG)のサイズ変更
     * @param input 変換元ファイル
     * @return 変換後ファイル
     */
    public static byte[] imageSizeConvert(byte[] input){
        try {
            InputStream is = new ByteArrayInputStream(input);
            BufferedImage org = ImageIO.read(is);

            int width = org.getWidth();
            int height = org.getHeight();

            int w = Consts.CONVERTEDIMAGE_WIDTH;

            // 元ファイルの幅が変更後の幅よりも小さい場合は何もしない
            if (width <= w) {
                return input;
            }

            int new_height = w * height / width;
            int new_width = w;

            AffineTransformOp xform = new AffineTransformOp(AffineTransform.getScaleInstance(
                    (double) new_width / width, (double) new_height / height ), AffineTransformOp.TYPE_BILINEAR);
            BufferedImage dst = new BufferedImage(new_width, new_height, org.getType());
            xform.filter(org, dst);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(dst, "jpeg", baos);

            return baos.toByteArray();
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    /**
     * 画像データ(JPG)のサイズ変更
     * @param input 変換元ファイル
     * @return 変換後ファイル
     */
    public static String loginUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * メモリストの空文字列削除
     * @param memos
     * @return
     */
    public static List<String> trimMemos(List<String> memos) {
        List<String> result = new ArrayList<String>();

        for (String memo : memos) {
            if (!memo.isEmpty()) {
                result.add(memo);
            }
        }

        return result;
    }

    /**
     * 翌日設定
     * @param date 元日付
     * @return 翌日日付
     */
    public static Date tommorow(Date date) {
        return dayAfter(date, 1);
    }

    /**
     * n日後設定
     * @param date 元日付
     * @return n日後日付
     */
    public static Date dayAfter(Date date, int after) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, after);

        return calendar.getTime();
    }

    public static String interviewCSVHeaderString() {
        StringBuffer header = new StringBuffer();

        for (int i = 0; i < Consts.INTERVIEWCSV_HEADER.length; i ++) {
            header.append("\"" + Consts.INTERVIEWCSV_HEADER[i] + "\"");

            if (i != Consts.INTERVIEWCSV_HEADER.length - 1) {
                header.append(",");
            }
        }
        header.append("\n");

        return header.toString();
    }
}