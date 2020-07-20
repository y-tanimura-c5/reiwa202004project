package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PastInterviewRequest implements Serializable {
    /**
     * 面談日
     */
    private String interviewDate;

    /**
     * 面談時間コード
     */
    private String interviewTime;

    /**
     * 情報開示コード
     */
    private String disclose;

    /**
     * 相談内容
     */
    private String interviewerComment;

    /**
     * 管理者コメント
     */
    private String adminComment;

    /**
     * 添付ファイル名
     */
    private String attachedFilename;

    /**
     * 面談内容(会社関連)表示内容リスト
     */
    private List<String> contentJobNames;

    /**
     * 面談内容(会社関連)メモリスト
     */
    private List<String> contentJobMemos;

    /**
     * 面談内容(プライベート)メモリスト
     */
    private List<String> contentPrivateMemos;
}
