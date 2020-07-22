package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.annotation.AttachedFile;

import lombok.Data;

@Data
public class InterviewRequest implements Serializable {
    public static final String[] JOB_NAMES = {
            "人間関係の相談",
            "給与面の相談",
            "経済面の相談",
            "業務関連の相談",
            "職場環境の相談",
            "社内への要望"
            };

    public static final String[] JOB_SHORTNAMES = {
            "人間関係",
            "給与面",
            "経済面",
            "業務関連",
            "職場環境",
            "社内要望"
            };

    public static final String[] PRIVATE_NAMES = {
            "ダミー",
            "ダミー",
            "ダミー"
            };

    public static final String[] INTERVIEWTIME_NAMES = {
            "1時間未満",
            "1時間から2時間",
            "2時間超"
            };

    public static final String[] INTERVIEWTIME_SHORTNAMES= {
            "1H未満",
            "1H-2H",
            "2H超"
            };

    public static final String[] DISCLOSE_NAMES = {
            "勤務先への情報開示を認める",
            "勤務先へ一部情報については開示して欲しくない",
            "勤務先へ全ての情報を開示して欲しくない"};

    public static final String[] DISCLOSE_SHORTNAMES = {
            "OK",
            "一部NG",
            "全てNG"
            };

    public InterviewRequest() {
        contentJobCheckItems = Arrays.asList(JOB_NAMES);
        contentPrivateCheckItems = Arrays.asList(PRIVATE_NAMES);
        interviewTimeItems = Arrays.asList(INTERVIEWTIME_NAMES);
        discloseItems = Arrays.asList(DISCLOSE_NAMES);
        interviewDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * ID
     */
    private String id;

    /**
     * 企業コード
     */
    @NotBlank
    private String companyID;

    /**
     * 企業名称
     */
    private String companyName;

    /**
     * 社員番号
     */
    @NotBlank
    private String employeeID;

    /**
     * 従業員名字
     */
    private String employeeFName;

    /**
     * 入社年月
     */
    private String hireYM;

    /**
     * 採用種別
     */
    private String adoptCode;

    /**
     * 扶養有無
     */
    private String supportCode;

    /**
     * 就業種別
     */
    private String employCode;

    /**
     * 面談日
     */
    @NotBlank
    private String interviewDate;

    /**
     * 面談時間コード
     */
    private int interviewTimeCode;

    /**
     * 情報開示コード
     */
    private int discloseCode;

    /**
     * 相談内容
     */
    @Size(max=2000)
    private String interviewerComment;

    /**
     * 管理者コメント
     */
    @Size(max=2000)
    private String adminComment;

    /**
     * 面談時間ラジオボタン表示内容リスト
     */
    private List<String> interviewTimeItems;

    /**
     * 情報開示ラジオボタン表示内容リスト
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
     * 添付ファイル
     */
    @AttachedFile
    private MultipartFile attachedFile;

    /**
     * 添付ファイル名
     */
    private String attachedFilename;

    /**
     * 過去面談結果リスト
     */
    private List<InterviewRequest> pastInterviews;

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

    public Date getInterviewDateDate() {
        Date result;
        try {
            result = new SimpleDateFormat("yyyy-MM-dd").parse(interviewDate);
        }
        catch (ParseException e) {
            result = new Date(0);
        }

        return result;
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
     * 文字列形式の面談日保存
     * @param 面談日
     */
    public void setInterviewDateFromDate(Date date) {
        setInterviewDate(new SimpleDateFormat("yyyy/MM/dd").format(date));
    }

    /**
     * 文字列形式の面談時間
     * @return 面談時間文字列
     */
    public String getInterviewTime() {
        try {
            return INTERVIEWTIME_NAMES[interviewTimeCode];
        }
        catch (Exception e) {
            return INTERVIEWTIME_NAMES[0];
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列
     */
    public String getDisclose() {
        try {
            return DISCLOSE_NAMES[discloseCode];
        }
        catch (Exception e) {
            return DISCLOSE_NAMES[0];
        }
    }

    /**
     * 文字列形式の面談時間
     * @return 面談時間文字列(短縮版)
     */
    public String getInterviewTimeShort() {
        try {
            return INTERVIEWTIME_SHORTNAMES[interviewTimeCode];
        }
        catch (Exception e) {
            return INTERVIEWTIME_SHORTNAMES[0];
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列(短縮版)
     */
    public String getDiscloseShort() {
        try {
            return DISCLOSE_SHORTNAMES[discloseCode];
        }
        catch (Exception e) {
            return DISCLOSE_SHORTNAMES[0];
        }
    }

    public List<String> getContentJobNames() {
        List<String> names = new ArrayList<String>();

        for (int code : contentJobCheckedList) {
            try {
                names.add(JOB_NAMES[code]);
            }
            catch (Exception e) {
                names.add(JOB_NAMES[0]);
            }
        }

        return names;
    }

    public String getContentJobNamesShort() {
        StringBuilder names = new StringBuilder();

        for (int i = 0; i < contentJobCheckedList.size(); i ++) {
            names.append(JOB_SHORTNAMES[i]);

            if (i != contentJobCheckedList.size() - 1) {
                names.append("、");
            }
        }

        return names.toString();
    }
}
