package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class InterviewRequest implements Serializable {

    public InterviewRequest() {
        contentJobCheckItems = new LinkedHashMap<Integer, String>();
        contentJobCheckItems.put(0, "人間関係の相談");
        contentJobCheckItems.put(1, "給与面の相談");
        contentJobCheckItems.put(2, "経済面の相談");
        contentJobCheckItems.put(3, "業務関連の相談");
        contentJobCheckItems.put(4, "職場環境の相談");
        contentJobCheckItems.put(5, "社内への要望");

        contentPrivateCheckItems = new LinkedHashMap<Integer, String>();
        contentPrivateCheckItems.put(0, "ダミー");
        contentPrivateCheckItems.put(1, "ダミー");
        contentPrivateCheckItems.put(2, "ダミー");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        interviewDate = dateFormat.format(new Date());
    }

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
     * 面談内容(会社関連)チェックボックス表示内容マップ
     */
    private Map<Integer, String> contentJobCheckItems;

    /**
     * 面談内容(会社関連)チェック結果リスト
     */
    private List<Integer> contentJobCheckedList;

    /**
     * 面談内容(会社関連)チェックメモリスト
     */
    private List<String> contentJobMemos;
    /**
     * 面談内容(プライベート)チェックボックス表示内容(ダミー)
     */
    private Map<Integer, String> contentPrivateCheckItems;

    /**
     * 面談内容(プライベート)チェック結果リスト
     */
    private List<Integer> contentPrivateCheckedList;

    /**
     * 面談内容(プライベート)チェックメモリスト
     */
    private List<String> contentPrivateMemos;

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
}
