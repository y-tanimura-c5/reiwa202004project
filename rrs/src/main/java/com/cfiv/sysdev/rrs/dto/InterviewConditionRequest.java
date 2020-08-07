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

import javax.validation.constraints.Size;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.Utils;

import lombok.Data;

@Data
public class InterviewConditionRequest implements Serializable {
    public InterviewConditionRequest() {
        interviewDateLastItems = Arrays.asList(Consts.INTERVIEWDATELAST_NAMES);
        hireLengthItems = Arrays.asList(Consts.HIRELENGTH_NAMES);
        adoptCheckItems = Arrays.asList(Consts.ADOPT_NAMES);
        supportCheckItems = Arrays.asList(Consts.SUPPORT_NAMES);
        employCheckItems = Arrays.asList(Consts.EMPLOY_NAMES);

        contentJobCheckItems = Arrays.asList(Consts.JOB_NAMES);
        contentPrivateCheckItems = Arrays.asList(Consts.PRIVATE_NAMES);
        interviewTimeItems = Arrays.asList(Consts.INTERVIEWTIME_NAMES);
        discloseItems = Arrays.asList(Consts.DISCLOSE_NAMES);
        interviewDateLastCode = 0;

        interviewerCommentMemos = new ArrayList<String>() ;
        for (int i = 0;  i < Consts.INTERVIEWERCOMMENT_CONDNUM; i ++) {
            interviewerCommentMemos.add("");
        }
        adminCommentMemos = new ArrayList<String>() ;
        for (int i = 0;  i < Consts.ADMINCOMMENT_CONDNUM; i ++) {
            adminCommentMemos.add("");
        }

        useInterviewSearch = "0";
        interviewDateMin = 0;
        interviewDateMax = 0;
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
     * 検索画面利用
     */
    private String useInterviewSearch;

    /**
     * 面談日(最小)
     */
    private int interviewDateMin;

    /**
     * 面談日(最大)
     */
    private int interviewDateMax;

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
    private List<@Size(max=200) String> contentJobMemos;

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
    private List<@Size(max=200) String> contentPrivateMemos;

    /**
     * 相談内容キーワードリスト
     */
    private List<@Size(max=200) String> interviewerCommentMemos;

    /**
     * 管理者コメントキーワードリスト
     */
    private List<@Size(max=200) String> adminCommentMemos;

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
                names.add(Consts.JOB_NAMES[code]);
            }
            catch (Exception e) {
                names.add(Consts.JOB_NAMES[0]);
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

    public String getInterviewDateStart() {
        return Utils.getStringDashFromDate(getInterviewDateStartDate());
    }

    public String getInterviewDateEnd() {
        return Utils.getStringDashFromDate(getInterviewDateEndDate());
    }

    public Date getInterviewDateStartDate() {
        if (interviewDateMin < interviewDateMax) {
            return getInterviewDateBeforeDay(interviewDateMax);
        }
        else {
            return getInterviewDateBeforeDay(interviewDateMin);
        }
    }

    public Date getInterviewDateEndDate() {
        if (interviewDateMin < interviewDateMax) {
            return getInterviewDateBeforeDay(interviewDateMin);
        }
        else {
            return getInterviewDateBeforeDay(interviewDateMax);
        }
    }

    public Date getInterviewDateBeforeDay(int before) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        try {
            now = df.parse(df.format(now));
        }
        catch (ParseException e) {
        }

        return Utils.dayAfter(now, (-1) * before);
    }

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