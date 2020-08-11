package com.cfiv.sysdev.rrs.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

/**
 * 面談結果CSV Entity
 */
@Data
public class InterviewCSV {

    /**
     * 企業コード
     */
    @CsvBindByName(column = "企業コード", required = true)
    @CsvBindByPosition(position = 0)
    private String companyID;

    /**
     * 企業コード
     */
    @CsvBindByName(column = "企業名称", required = true)
    @CsvBindByPosition(position = 1)
    private String companyName;

    /**
     * 従業員番号
     */
    @CsvBindByName(column = "従業員番号", required = true)
    @CsvBindByPosition(position = 2)
    private String employeeCode;

    /**
     * 従業員番号
     */
    @CsvBindByName(column = "従業員名字", required = true)
    @CsvBindByPosition(position = 3)
    private String employeeFName;

    /**
     * リファイナー名
     */
    @CsvBindByName(column = "リファイナー名", required = true)
    @CsvBindByPosition(position = 4)
    private String refinerName;

    /**
     * 面談日
     */
    @CsvBindByName(column = "面談日", required = true)
    @CsvBindByPosition(position = 5)
    private String interviewDate;

    /**
     * 面談時間
     */
    @CsvBindByName(column = "面談時間", required = true)
    @CsvBindByPosition(position = 6)
    private String interviewTime;

    /**
     * 情報開示
     */
    @CsvBindByName(column = "情報開示", required = true)
    @CsvBindByPosition(position = 7)
    private String disclose;

    /**
     * 相談内容
     */
    @CsvBindByName(column = "相談内容", required = true)
    @CsvBindByPosition(position = 8)
    private String interviewerComment;

    /**
     * 管理者コメント
     */
    @CsvBindByName(column = "管理者コメント", required = true)
    @CsvBindByPosition(position = 9)
    private String adminComment;

    /**
     * 人間関係
     */
    @CsvBindByName(column = "人間関係", required = true)
    @CsvBindByPosition(position = 10)
    private String contentJobStatus1;

    /**
     * 人間関係メモ
     */
    @CsvBindByName(column = "人間関係メモ", required = true)
    @CsvBindByPosition(position = 11)
    private String contentJobMemo1;

    /**
     * 給与面
     */
    @CsvBindByName(column = "給与面", required = true)
    @CsvBindByPosition(position = 12)
    private String contentJobStatus2;

    /**
     * 給与面メモ
     */
    @CsvBindByName(column = "給与面メモ", required = true)
    @CsvBindByPosition(position = 13)
    private String contentJobMemo2;

    /**
     * 経済面
     */
    @CsvBindByName(column = "経済面", required = true)
    @CsvBindByPosition(position = 14)
    private String contentJobStatus3;

    /**
     * 経済面メモ
     */
    @CsvBindByName(column = "経済面メモ", required = true)
    @CsvBindByPosition(position = 15)
    private String contentJobMemo3;

    /**
     * 業務関連
     */
    @CsvBindByName(column = "業務関連", required = true)
    @CsvBindByPosition(position = 16)
    private String contentJobStatus4;

    /**
     * 業務関連メモ
     */
    @CsvBindByName(column = "業務関連メモ", required = true)
    @CsvBindByPosition(position = 17)
    private String contentJobMemo4;

    /**
     * 職場環境
     */
    @CsvBindByName(column = "職場環境", required = true)
    @CsvBindByPosition(position = 18)
    private String contentJobStatus5;

    /**
     * 職場環境メモ
     */
    @CsvBindByName(column = "職場環境メモ", required = true)
    @CsvBindByPosition(position = 19)
    private String contentJobMemo5;

    /**
     * 社内要望
     */
    @CsvBindByName(column = "社内要望", required = true)
    @CsvBindByPosition(position = 20)
    private String contentJobStatus6;

    /**
     * 社内要望メモ
     */
    @CsvBindByName(column = "社内要望メモ", required = true)
    @CsvBindByPosition(position = 21)
    private String contentJobMemo6;

    /**
     * プライベート1メモ
     */
    @CsvBindByName(column = "プライベート1メモ", required = true)
    @CsvBindByPosition(position = 22)
    private String contentPrivateMemo1;

    /**
     * プライベート2メモ
     */
    @CsvBindByName(column = "プライベート2メモ", required = true)
    @CsvBindByPosition(position = 23)
    private String contentPrivateMemo2;

    /**
     * プライベート3メモ
     */
    @CsvBindByName(column = "プライベート3メモ", required = true)
    @CsvBindByPosition(position = 24)
    private String contentPrivateMemo3;
 }