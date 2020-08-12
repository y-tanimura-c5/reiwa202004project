package com.cfiv.sysdev.rrs.request;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.annotation.AttachedFile;
import com.cfiv.sysdev.rrs.annotation.EmployeeCodeUsed;
import com.cfiv.sysdev.rrs.csv.InterviewCSV;
import com.cfiv.sysdev.rrs.entity.InterviewAttach;
import com.cfiv.sysdev.rrs.entity.InterviewContent;
import com.cfiv.sysdev.rrs.entity.InterviewResult;

import lombok.Data;

@Data
@EmployeeCodeUsed
public class InterviewRequest implements Serializable {

    public InterviewRequest() {
        contentJobCheckItems = Arrays.asList(Consts.JOB_NAMES);
        contentPrivateCheckItems = Arrays.asList(Consts.PRIVATE_NAMES);
        interviewTimeItems = Arrays.asList(Consts.INTERVIEWTIME_NAMES);
        discloseItems = Arrays.asList(Consts.DISCLOSE_NAMES);
        interviewDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public InterviewRequest(InterviewResult _result, EmployeeRequest _employee, String _companyName, String _displayName) {
        this();

        setId(_result.getIdString(1));
        setCompanyID(_result.getCompanyIDString(4));
        setEmployeeCode(_result.getEmployeeCode());
        setEmployee(_employee);
        setEmployeeFName(_employee.getEmployeeFName());
        setEmployeeHireYM(_employee.getHireYM());
        setEmployeeAdopt(_employee.getAdopt());
        setEmployeeSupport(_employee.getSupport());
        setEmployeeEmploy(_employee.getEmploy());
        setCompanyName(_companyName);
        setRefinerUsername(_result.getRefinerUserID());
        setRefinerDisplayName(_displayName);
        setInterviewDate(_result.getInteviewDateStringDash());
        setInterviewTimeCode(_result.getInterviewTimeCode());
        setDiscloseCode(_result.getDiscloseCode());
        setInterviewerComment(_result.getInterviewerComment());
        setAdminComment(_result.getAdminComment());

        Map<Integer, String> jobMap = new HashMap<>();
        Map<Integer, String> priMap = new HashMap<>();

        for (InterviewContent content : _result.getInterviewContents()) {
            if (content.getDeleted() == Consts.EXIST) {
                if (content.getContentKind() == Consts.CONTENTKIND_JOB) {
                    jobMap.put(content.getContentCode(), content.getContentComment());
                }
                else {
                    priMap.put(content.getContentCode(), content.getContentComment());
                }
            }
        }

        List<Integer> jobChecked = new ArrayList<Integer>();
        List<String> jobMemos = new ArrayList<String>();
        for (int i = 0; i < contentJobCheckItems.size(); i ++) {
            if (jobMap.containsKey(i)) {
                jobChecked.add(i);
                jobMemos.add(jobMap.get(i));
            }
            else {
                jobMemos.add("");
            }
        }

        List<String> priMemos = new ArrayList<String>();
        for (int i = 0; i < contentPrivateCheckItems.size(); i ++) {
            if (priMap.containsKey(i)) {
                priMemos.add(priMap.get(i));
            }
            else {
                priMemos.add("");
            }
        }
        setContentJobCheckedList(jobChecked);
        setContentJobMemos(jobMemos);
        setContentPrivateMemos(priMemos);

        for (InterviewAttach attach : _result.getInterviewAttaches()) {
            setAttachedFilename(attach.getFilename());
        }
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
     * 従業員名字
     */
    private String employeeFName;

    /**
     * 入社年月
     */
    private String employeeHireYM;

    /**
     * 採用種別
     */
    private String employeeAdopt;

    /**
     * 扶養有無
     */
    private String employeeSupport;

    /**
     * 就業種別
     */
    private String employeeEmploy;

    /**
     * リファイナーユーザーID
     */
    private String refinerUsername;

    /**
     * リファイナー名
     */
    private String refinerDisplayName;

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
     * 過去面談結果リスト
     */
    private List<InterviewRequest> pastInterviews;

    /**
     * 添付ファイル
     */
    @AttachedFile
    private MultipartFile attachedFile;

    /**
     * 添付ファイル名
     */
    @Size(max=200)
    private String attachedFilename;

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
     * 面談日
     * @return 面談日(Date形式)
     */
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
     * 文字列形式の面談時間
     * @return 面談時間文字列
     */
    public String getInterviewTime() {
        try {
            return Consts.INTERVIEWTIME_NAMES[interviewTimeCode];
        }
        catch (Exception e) {
            return Consts.INTERVIEWTIME_NAMES[0];
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列
     */
    public String getDisclose() {
        try {
            return Consts.DISCLOSE_NAMES[discloseCode];
        }
        catch (Exception e) {
            return Consts.DISCLOSE_NAMES[0];
        }
    }

    /**
     * 文字列形式の面談時間
     * @return 面談時間文字列(短縮版)
     */
    public String getInterviewTimeShort() {
        try {
            return Consts.INTERVIEWTIME_SHORTNAMES[interviewTimeCode];
        }
        catch (Exception e) {
            return Consts.INTERVIEWTIME_SHORTNAMES[0];
        }
    }

    /**
     * 文字列形式の情報開示
     * @return 情報開示文字列(短縮版)
     */
    public String getDiscloseShort() {
        try {
            return Consts.DISCLOSE_SHORTNAMES[discloseCode];
        }
        catch (Exception e) {
            return Consts.DISCLOSE_SHORTNAMES[0];
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
     * 面談内容・会社関連アイテム名文字列(短縮版)
     * @return 面談内容・会社関連アイテム名文字列(短縮版)
     */
    public String getContentJobNamesShort() {
        StringBuilder names = new StringBuilder();

        Collections.sort(contentJobCheckedList);
        for (int i = 0; i < contentJobCheckedList.size(); i ++) {
            names.append(Consts.JOB_SHORTNAMES[contentJobCheckedList.get(i)]);

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

    /**
     * 面談結果(CSV形式)
     * @param
     * @return 面談結果(InterviewCSV)
     */
    public InterviewCSV toCSV(int userRole) {
        InterviewCSV csv = new InterviewCSV();

        csv.setCompanyID(companyID);
        csv.setCompanyName(companyName);
        csv.setEmployeeCode(employeeCode);
        csv.setEmployeeFName(employee.getEmployeeFName());
        csv.setInterviewDate(interviewDate);
        csv.setRefinerName(refinerDisplayName);
        csv.setInterviewTime(interviewTimeItems.get(interviewTimeCode));
        csv.setDisclose(discloseItems.get(discloseCode));

        if (userRole == Consts.USERROLECODE_CLIENTADMIN && discloseCode != Consts.DISCLOSECODE_OK) {
            csv.setInterviewerComment("－");
            csv.setAdminComment("－");
            csv.setContentJobStatus1("－");
            csv.setContentJobMemo1("－");
            csv.setContentJobStatus2("－");
            csv.setContentJobMemo2("－");
            csv.setContentJobStatus3("－");
            csv.setContentJobMemo3("－");
            csv.setContentJobStatus4("－");
            csv.setContentJobMemo4("－");
            csv.setContentJobStatus5("－");
            csv.setContentJobMemo5("－");
            csv.setContentJobStatus6("－");
            csv.setContentJobMemo6("－");
            csv.setContentPrivateMemo1("－");
            csv.setContentPrivateMemo2("－");
            csv.setContentPrivateMemo3("－");
        }
        else {
            csv.setInterviewerComment(interviewerComment);
            csv.setAdminComment(adminComment);

            for (int i = 0; i < contentJobCheckItems.size(); i ++) {
                switch (i) {
                case 0:
                    if (containsContentJobChecked(i)) {
                        csv.setContentJobStatus1(Consts.EXIST_NAME);
                        csv.setContentJobMemo1(contentJobMemos.get(i));
                    }
                    else {
                        csv.setContentJobStatus1(Consts.NOTEXIST_NAME);
                    }
                    break;
                case 1:
                    if (containsContentJobChecked(i)) {
                        csv.setContentJobStatus2(Consts.EXIST_NAME);
                        csv.setContentJobMemo2(contentJobMemos.get(i));
                    }
                    else {
                        csv.setContentJobStatus2(Consts.NOTEXIST_NAME);
                    }
                    break;
                case 2:
                    if (containsContentJobChecked(i)) {
                        csv.setContentJobStatus3(Consts.EXIST_NAME);
                        csv.setContentJobMemo3(contentJobMemos.get(i));
                    }
                    else {
                        csv.setContentJobStatus3(Consts.NOTEXIST_NAME);
                    }
                    break;
                case 3:
                    if (containsContentJobChecked(i)) {
                        csv.setContentJobStatus4(Consts.EXIST_NAME);
                        csv.setContentJobMemo4(contentJobMemos.get(i));
                    }
                    else {
                        csv.setContentJobStatus4(Consts.NOTEXIST_NAME);
                    }
                    break;
                case 4:
                    if (containsContentJobChecked(i)) {
                        csv.setContentJobStatus5(Consts.EXIST_NAME);
                        csv.setContentJobMemo5(contentJobMemos.get(i));
                    }
                    else {
                        csv.setContentJobStatus5(Consts.NOTEXIST_NAME);
                    }
                    break;
                case 5:
                    if (containsContentJobChecked(i)) {
                        csv.setContentJobStatus6(Consts.EXIST_NAME);
                        csv.setContentJobMemo6(contentJobMemos.get(i));
                    }
                    else {
                        csv.setContentJobStatus6(Consts.NOTEXIST_NAME);
                    }
                    break;
                }
            }

            for (int i = 0; i < contentJobCheckItems.size(); i ++) {
                if (i < contentPrivateMemos.size()) {
                    switch (i) {
                    case 0:
                        csv.setContentPrivateMemo1(contentPrivateMemos.get(i));
                        break;
                    case 1:
                        csv.setContentPrivateMemo2(contentPrivateMemos.get(i));
                        break;
                    case 2:
                        csv.setContentPrivateMemo3(contentPrivateMemos.get(i));
                        break;
                    }
                }
            }
        }

        return csv;
    }
}
