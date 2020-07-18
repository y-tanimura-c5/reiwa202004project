package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class InterviewRequest implements Serializable {

    public InterviewRequest() {
    }

    /**
     * 面談日
     */
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
    private String interviewerComment;

    /**
     * 管理者コメント
     */
    private String adminComment;

    /**
     * チェック
     */
    private String interviewContentCheck1;

    /**
     * メモ
     */
    private String interviewContentMemo1;

    /**
     * チェック
     */
    private String interviewContentCheck2;

    /**
     * メモ
     */
    private String interviewContentMemo2;

    /**
     * チェック
     */
    private String interviewContentCheck3;

    /**
     * メモ
     */
    private String interviewContentMemo3;

    /**
     * チェック
     */
    private String interviewContentCheck4;

    /**
     * メモ
     */
    private String interviewContentMemo4;

    /**
     * チェック
     */
    private String interviewContentCheck5;

    /**
     * メモ
     */
    private String interviewContentMemo5;

    /**
     * チェック
     */
    private String interviewContentCheck6;

    /**
     * メモ
     */
    private String interviewContentMemo6;

    /**
     * チェック
     */
    private String interviewContentCheck7;

    /**
     * メモ
     */
    private String interviewContentMemo7;

    /**
     * チェック
     */
    private String interviewContentCheck8;

    /**
     * メモ
     */
    private String interviewContentMemo8;

    /**
     * チェック
     */
    private String interviewContentCheck9;

    /**
     * メモ
     */
    private String interviewContentMemo9;
}
