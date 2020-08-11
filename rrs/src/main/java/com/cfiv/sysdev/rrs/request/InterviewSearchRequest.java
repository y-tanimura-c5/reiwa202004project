package com.cfiv.sysdev.rrs.request;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.Utils;

import lombok.Data;

@Data
public class InterviewSearchRequest implements Serializable {
    public InterviewSearchRequest() {
        Date now = new Date();

        interviewDateLastItems = Arrays.asList(Consts.INTERVIEWDATELAST_NAMES);
        hireLengthItems = Arrays.asList(Consts.HIRELENGTH_NAMES);
        adoptCheckItems = Arrays.asList(Consts.ADOPT_NAMES);
        supportCheckItems = Arrays.asList(Consts.SUPPORT_NAMES);
        employCheckItems = Arrays.asList(Consts.EMPLOY_NAMES);

        contentJobCheckItems = Arrays.asList(Consts.JOB_NAMES);
        contentPrivateCheckItems = Arrays.asList(Consts.PRIVATE_NAMES);
        interviewTimeItems = Arrays.asList(Consts.INTERVIEWTIME_NAMES);
        discloseItems = Arrays.asList(Consts.DISCLOSE_NAMES);
        interviewDateStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        interviewDateEnd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        interviewDateLastCode = 0;

        interviewerCommentMemos = Arrays.asList("", "", "");
        adminCommentMemos = Arrays.asList("", "", "");

        companyID = "";
        employeeCode = "";
        interviewDateStart = Utils.getStringDashFromDate(now);
        interviewDateEnd = Utils.getStringDashFromDate(now);
        interviewTimeCheckedList = new ArrayList<Integer>();
        discloseCheckedList = new ArrayList<Integer>();
        contentJobCheckedList = new ArrayList<Integer>();
        contentJobMemos = new ArrayList<String>() ;
        contentPrivateCheckedList = new ArrayList<Integer>();
        contentPrivateMemos = new ArrayList<String>();
        adoptCheckedList =  new ArrayList<Integer>();
        supportCheckedList = new ArrayList<Integer>();
        employCheckedList = new ArrayList<Integer>();
    }

    /**
     * ID
     */
    private String id;

    /**
     * 企業コード
     */
    private String companyID;

    /**
     * 従業員番号
     */
    private String employeeCode;

    /**
     * 面談日(開始)
     */
    private String interviewDateStart;

    /**
     * 面談日(終了)
     */
    private String interviewDateEnd;

    /**
     * 面談日(nヶ月前)ドロップダウンコード
     */
    private int interviewDateLastCode;

    /**
     * 面談日(nヶ月前)ドロップダウン表示内容リスト
     */
    private List<String> interviewDateLastItems;

    /**
     * 面談日種別(期間指定／最終面談日指定)
     */
    private int interviewDateCode;

    /**
     * 面談時間チェック結果リスト
     */
    private List<Integer> interviewTimeCheckedList;

    /**
     * 面談時間チェックボックス表示内容リスト
     */
    private List<String> interviewTimeItems;

    /**
     * 情報開示チェック結果リスト
     */
    private List<Integer> discloseCheckedList;

    /**
     * 情報開示チェックボックス表示内容リスト
     */
    private List<String> discloseItems;

    /**
     * 面談内容(会社関連)チェックボックス表示内容リスト
     */
    private List<String> contentJobCheckItems;

    /**
     * 面談内容(会社関連)チェック結果リスト
     */
    private List<Integer> contentJobCheckedList;

    /**
     * 面談内容(会社関連)チェックメモリスト
     */
    private List<String> contentJobMemos;

    /**
     * 面談内容(プライベート)チェックボックス表示内容リスト(ダミー)
     */
    private List<String> contentPrivateCheckItems;

    /**
     * 面談内容(プライベート)チェック結果リスト
     */
    private List<Integer> contentPrivateCheckedList;

    /**
     * 面談内容(プライベート)チェックメモリスト
     */
    private List<String> contentPrivateMemos;

    /**
     * 相談内容キーワードリスト
     */
    private List<String> interviewerCommentMemos;

    /**
     * 管理者コメントキーワードリスト
     */
    private List<String> adminCommentMemos;

    /**
     * 勤続年数ドロップダウン表示内容リスト
     */
    private List<String> hireLengthItems;

    /**
     * 勤続年数(開始)ドロップダウンコード
     */
    private int hireLengthStartCode;
    /**
     * 勤続年数(終了)ドロップダウンコード
     */
    private int hireLengthEndCode;

    /**
     * 採用種別チェックボックス表示内容リスト
     */
    private List<String> adoptCheckItems;

    /**
     * 採用種別チェック結果リスト
     */
    private List<Integer> adoptCheckedList;

    /**
     * 扶養有無チェックボックス表示内容リスト
     */
    private List<String> supportCheckItems;

    /**
     * 扶養有無チェック結果リスト
     */
    private List<Integer> supportCheckedList;

    /**
     * 就業種別チェックボックス表示内容リスト
     */
    private List<String> employCheckItems;

    /**
     * 就業種別チェック結果リスト
     */
    private List<Integer> employCheckedList;

    /**
     * 企業コードLong値
     * @param cs 企業コード文字列
     */
    public Long getCompanyIDLong() {
        try {
            return Long.parseLong(companyID);
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 面談内容選択コードチェック(会社関連)
     * @param c チェック対象コード
     * @return true＝含まれる／false＝含まれない
     */
    public boolean containsContentJobChecked(int c) {
        return containsContentChecked(contentJobCheckedList, c);
    }

    /**
     * 面談内容選択コードチェック(プライベート)
     * @param c チェック対象コード
     * @return true＝含まれる／false＝含まれない
     */
    public boolean containsContentPrivateChecked(int c) {
        return containsContentChecked(contentPrivateCheckedList, c);
    }

    /**
     * 選択コードチェック
     * @param list 選択済コードリスト
     * @param c チェック対象コード
     * @return true＝含まれる／false＝含まれない
     */
    private boolean containsContentChecked(List<Integer> list, int c) {
        for (int value : list) {
            if (value == c) {
                return true;
            }
        }

        return false;
    }

    /**
     * Date形式の面談日(開始)
     * @param id id
     * @param nDigits 0埋め桁数
     */
    public Date getInterviewDateStartFromString() {
        return getDateFromString(interviewDateStart);
    }

    /**
     * Date形式の面談日(終了)
     * @param id id
     * @param nDigits 0埋め桁数
     */
    public Date getInterviewDateEndFromString() {
        return getDateFromString(interviewDateEnd);
    }

    /**
     * Date形式の日付
     * @param dateStr "yyyy-mm-dd"形式の日付文字列
     */
    public Date getDateFromString(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        }
        catch (ParseException e) {
            return new Date(0);
        }
    }

    /**
     * 面談内容・会社関連アイテム名文字列
     * @return 面談内容・会社関連アイテム名文字列
     */
    public List<String> getContentJobNames() {
        List<String> names = new ArrayList<String>();

        for (int code : contentJobCheckedList) {
            try {
                names.add(Consts.JOB_NAMES[code]);
            }
            catch (Exception e) {
                names.add(Consts.JOB_NAMES[0]);
            }
        }

        return names;
    }

    /**
     * 入社年月範囲開始値ドロップダウンメニューコード
     * @return ドロップダウンメニューコード
     */
    public Date getHireYMDateFromLengthStartCode() {
        return getHireYMDateFromCode(hireLengthStartCode);
    }

    /**
     * 入社年月範囲終了値ドロップダウンメニューコード
     * @return ドロップダウンメニューコード
     */
    public Date getHireYMDateFromLengthEndCode() {
        return getHireYMDateFromCode(hireLengthEndCode);
    }

    /**
     * 入社年月(Date形式)
     * @param code ドロップダウンメニューコード
     * @return
     */
    public Date getHireYMDateFromCode(int code) {
        Date result = null;
        Calendar calendar = Calendar.getInstance();

        Date now = new Date();
        try {
            DateFormat df = new SimpleDateFormat("yyyyMM");
            now = df.parse(df.format(new Date()));
        }
        catch (ParseException e) {
            return null;
        }

        if (code != 0) {
            int length = 0;
            try {
                length = Integer.parseInt("-" + Consts.HIRELENGTH_NAMES[code]);
            }
            catch (NumberFormatException e) {
                return null;
            }
            calendar.setTime(now);
            calendar.add(Calendar.YEAR, length);
            result = calendar.getTime();
        }
        else {
            return null;
        }

        return result;
    }

    /**
     * 面談日範囲開始値
     * @return 日付(Date形式)
     */
    public Date getInterviewDateStartDate() {
        return getInterviewDateFromString(interviewDateStart);
    }

    /**
     * 面談日範囲終了値
     * @return 日付(Date形式)
     */
    public Date getInterviewDateEndDate() {
        return getInterviewDateFromString(interviewDateEnd);
    }

    /**
     * 日付文字列→日付変換
     * @param str 日付文字列(YYYY-MM-DD形式)
     * @return 日付
     */
    public Date getInterviewDateFromString(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(str);
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * 現在から最終面談月数前の日付
     * @return 現在から最終面談月数前の日付
     */
    public Date getInterviewDateLastDate() {
        Date result = null;
        Calendar calendar = Calendar.getInstance();

        Date now = new Date();
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            now = df.parse(df.format(new Date()));
        }
        catch (ParseException e) {
            return null;
        }

        int length = 0;
        try {
            length = Integer.parseInt("-" + Consts.INTERVIEWDATELAST_NAMES[interviewDateLastCode]);
        }
        catch (NumberFormatException e) {
            return null;
        }
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, length);
        result = calendar.getTime();

        return result;
    }
}