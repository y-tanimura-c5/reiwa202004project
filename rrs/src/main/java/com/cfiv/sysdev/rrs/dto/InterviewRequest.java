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

import com.cfiv.sysdev.rrs.Const;
import com.cfiv.sysdev.rrs.annotation.AttachedFile;
import com.cfiv.sysdev.rrs.entity.InterviewContent;
import com.cfiv.sysdev.rrs.entity.InterviewResult;

import lombok.Data;

@Data
public class InterviewRequest implements Serializable {

    public InterviewRequest() {
        contentJobCheckItems = Arrays.asList(Const.JOB_NAMES);
        contentPrivateCheckItems = Arrays.asList(Const.PRIVATE_NAMES);
        interviewTimeItems = Arrays.asList(Const.INTERVIEWTIME_NAMES);
        discloseItems = Arrays.asList(Const.DISCLOSE_NAMES);
        interviewDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public InterviewRequest(InterviewResult result, EmployeeRequest employee) {
        this();

        setIdFromNumeric(result.getId(), 1);
        setCompanyIDFromNumeric(result.getCompanyID(), 4);
        setEmployeeCode(result.getEmployeeCode());
        setEmployee(employee);
        setInterviewDateFromDate(result.getInterviewDateTime());
        setInterviewTimeCode(result.getInterviewTimeCode());
        setDiscloseCode(result.getDiscloseCode());
        setInterviewerComment(result.getInterviewerComment());
        setAdminComment(result.getAdminComment());
        setAttachedFilename(result.getFilename());

        List<Integer> job_checked = new ArrayList<Integer>();
        List<String> job_memos = new ArrayList<String>();
        List<String> pri_memos = new ArrayList<String>();
        for (InterviewContent content : result.getInterviewContents()) {
            if (content.getContentKind() == Const.CONTENTKIND_JOB) {
                job_checked.add(content.getContentCode());
                job_memos.add(content.getContentComment());
            }
            else {
                pri_memos.add(content.getContentComment());
            }
        }
        setContentJobCheckedList(job_checked);
        setContentJobMemos(job_memos);
        setContentPrivateMemos(pri_memos);
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
     * 従業員番号
     */
    @NotBlank
    private String employeeCode;

    /**
     * 従業員情報
     */
    private EmployeeRequest employee;

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
        setInterviewDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    /**
     * 文字列形式の面談時間
     * @return 面談時間文字列
     */
    public String getInterviewTime() {
        try {
            return Const.INTERVIEWTIME_NAMES[interviewTimeCode];
        }
        catch (Exception e) {
            return Const.INTERVIEWTIME_NAMES[0];
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列
     */
    public String getDisclose() {
        try {
            return Const.DISCLOSE_NAMES[discloseCode];
        }
        catch (Exception e) {
            return Const.DISCLOSE_NAMES[0];
        }
    }

    /**
     * 文字列形式の面談時間
     * @return 面談時間文字列(短縮版)
     */
    public String getInterviewTimeShort() {
        try {
            return Const.INTERVIEWTIME_SHORTNAMES[interviewTimeCode];
        }
        catch (Exception e) {
            return Const.INTERVIEWTIME_SHORTNAMES[0];
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列(短縮版)
     */
    public String getDiscloseShort() {
        try {
            return Const.DISCLOSE_SHORTNAMES[discloseCode];
        }
        catch (Exception e) {
            return Const.DISCLOSE_SHORTNAMES[0];
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
                names.add(Const.JOB_NAMES[code]);
            }
            catch (Exception e) {
                names.add(Const.JOB_NAMES[0]);
            }
        }

        return names;
    }

    /**
     * 面談内容・会社関連アイテム名文字列(短縮版)
     * @return 面談内容・会社関連アイテム名文字列(短縮版)
     */
    public String getContentJobNamesShort() {
        StringBuilder names = new StringBuilder();

        for (int i = 0; i < contentJobCheckedList.size(); i ++) {
            names.append(Const.JOB_SHORTNAMES[i]);

            if (i != contentJobCheckedList.size() - 1) {
                names.append("、");
            }
        }

        return names.toString();
    }

    /**
     * 文字列形式の面談日("/"区切り)
     * @param 面談日
     */
    public String getInterviewDateSlash() {
        return interviewDate.replace("-", "/");
    }

    /**
     * 文字列形式の面談日(面談結果日表示用)
     * @param 面談日
     */
    public String getInterviewDateResult() {
        return getInterviewDateSlash() + " 面談結果";
    }
}
