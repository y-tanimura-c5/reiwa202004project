package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cfiv.sysdev.rrs.Const;

import lombok.Data;

@Data
public class InterviewSearchRequest implements Serializable {


    public InterviewSearchRequest() {
        interviewDateLastItems = Arrays.asList(Const.INTERVIEWDATELAST_NAMES);
        hireLengthItems = Arrays.asList(Const.HIRELENGTH_NAMES);
        adoptCheckItems = Arrays.asList(Const.ADOPT_NAMES);
        supportCheckItems = Arrays.asList(Const.SUPPORT_NAMES);
        employCheckItems = Arrays.asList(Const.EMPLOY_NAMES);

        contentJobCheckItems = Arrays.asList(Const.JOB_NAMES);
        contentPrivateCheckItems = Arrays.asList(Const.PRIVATE_NAMES);
        interviewTimeItems = Arrays.asList(Const.INTERVIEWTIME_NAMES);
        discloseItems = Arrays.asList(Const.DISCLOSE_NAMES);
        interviewDateStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        interviewDateEnd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        interviewDateLastCode = 2;

        interviewerCommentMemos = Arrays.asList("", "", "");
        adminCommentMemos = Arrays.asList("", "", "");
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

    public boolean containsContentJobChecked(int c) {
        return containsContentChecked(contentJobCheckedList, c);
    }

    public boolean containsContentPrivateChecked(int c) {
        return containsContentChecked(contentPrivateCheckedList, c);
    }

    private boolean containsContentChecked(List<Integer> list, int c) {
        for (int value : list) {
            if (value == c) {
                return true;
            }
        }

        return false;
    }

    /**
     * 文字列形式のID保存
     * @param id id
     * @param nDigits 0埋め桁数
     */
    public void setIdFromNumeric(Long id, int nDigits) {
        setId(String.format("%0" + nDigits + "d", id));
    }

    /**
     * 文字列形式の企業コード保存
     * @param id id
     * @param nDigits 0埋め桁数
     */
    public void setCompanyIDFromNumeric(Long id, int nDigits) {
        setCompanyID(String.format("%0" + nDigits + "d", id));
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

    public List<String> getContentJobNames() {
        List<String> names = new ArrayList<String>();

        for (int code : contentJobCheckedList) {
            try {
                names.add(Const.JOB_NAMES[code]);
            }
            catch (Exception e) {
                names.add(Const.JOB_NAMES[0]);
            }
        }

        return names;
    }

    public Date getHireYMDateFromLengthStartCode() {
        return getHireYMDateFromCode(hireLengthStartCode);
    }

    public Date getHireYMDateFromLengthEndCode() {
        return getHireYMDateFromCode(hireLengthEndCode);
    }


    public Date getHireYMDateFromCode(int code) {
        Date hireYMMin = null;
        Calendar calendar = Calendar.getInstance();

        Date nowYM = new Date();
        try {
            DateFormat df = new SimpleDateFormat("yyyyMM");
            nowYM = df.parse(df.format(new Date()));
        }
        catch (ParseException e) {
            return null;
        }

        if (code != 0) {
            int hireYMLength = 0;
            try {
                hireYMLength = Integer.parseInt("-" + Const.HIRELENGTH_NAMES[code]);
            }
            catch (NumberFormatException e) {
                return null;
            }
            calendar.setTime(nowYM);
            calendar.add(Calendar.YEAR, hireYMLength);
            hireYMMin = calendar.getTime();
        }
        else {
            return null;
        }

        return hireYMMin;
    }
}