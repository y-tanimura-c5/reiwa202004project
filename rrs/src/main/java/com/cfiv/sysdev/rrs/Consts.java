package com.cfiv.sysdev.rrs;

public class Consts {
    public static final int PAGENATION_PAGESIZE = 50;
    public static final int PASTINTERVIEW_NUM = 3;
    public static final int INTERVIEWERCOMMENT_CONDNUM = 3;
    public static final int ADMINCOMMENT_CONDNUM = 3;
    public static final int CONVERTEDIMAGE_WIDTH = 1600;

    public static final boolean EXIST = false;
    public static final boolean DELETED = true;

    public static final boolean DISABLED = false;
    public static final boolean ENABLED = true;

    public static final String EXIST_NAME = "あり";
    public static final String NOTEXIST_NAME = "なし";

    public static final int CONTENTKIND_JOB = 0;
    public static final int CONTENTKIND_PRIVATE = 1;

    public static final int INTERVIEWDATECODE_LAST = 0;
    public static final int INTERVIEWDATECODE_REGION = 1;

    public static final int USERROLECODE_ADMIN = 0;
    public static final int USERROLECODE_CLIENTADMIN = 1;
    public static final int USERROLECODE_REFINER = 10;

    public static final int CONDITIONKIND_USESEARCH = 0;
    public static final int CONDITIONKIND_INTERVIEWDATECODE = 1;
    public static final int CONDITIONKIND_INTERVIEWDATEREGION = 2;
    public static final int CONDITIONKIND_INTERVIEWDATELAST = 3;
    public static final int CONDITIONKIND_INTERVIEWTIME = 4;
    public static final int CONDITIONKIND_DISCLOSE = 5;
    public static final int CONDITIONKIND_CONTENTJOB = 6;
    public static final int CONDITIONKIND_CONTENTPRIVATE = 7;
    public static final int CONDITIONKIND_INTERVIEWERCOMMENT = 8;
    public static final int CONDITIONKIND_ADMINCOMMENT = 9;
    public static final int CONDITIONKIND_HIREREGION = 10;
    public static final int CONDITIONKIND_ADOPT = 11;
    public static final int CONDITIONKIND_SUPPORT = 12;
    public static final int CONDITIONKIND_EMPLOY = 13;

    public static final int DISCLOSECODE_OK = 0;
    public static final int DISCLOSECODE_PARTIALNG = 1;
    public static final int DISCLOSECODE_ALLNG = 2;

    public static final Long COMPANYID_ADMIN = 0L;

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
            "勤務先へ全ての情報を開示して欲しくない"
            };

    public static final String[] DISCLOSE_SHORTNAMES = {
            "OK",
            "一部NG",
            "全てNG"
            };

    public static final String[] ADOPT_NAMES = {
            "新規採用",
            "中途採用"
            };

    public static final String[] SUPPORT_NAMES = {
            "扶養なし",
            "扶養あり"
            };

    public static final String[] EMPLOY_NAMES = {
            "在籍中",
            "退職済"
            };

    public static final String[] INTERVIEWDATELAST_NAMES = {
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12"
            };

    public static final String[] HIRELENGTH_NAMES = {
            "-",
            "1",
            "2",
            "3",
            "5",
            "7",
            "10"
            };

    public static final int[] USERROLE_CODES = {
            USERROLECODE_ADMIN,
            USERROLECODE_CLIENTADMIN,
            USERROLECODE_REFINER
            };

    public static final String[] USERROLE_NAMES = {
            "全体管理者",
            "企業管理者",
            "リファイナー"
            };

    public static final int[] ENABLED_CODES = {
            1,
            0
            };

    public static final String[] ENABLED_NAMES = {
            "有効",
            "無効"
            };

    public static final String[] EXIST_NAMES = {
            NOTEXIST_NAME,
            EXIST_NAME
            };

    public static final String[] INTERVIEWCSV_HEADER = {
            "企業コード",
            "企業名称",
            "従業員番号",
            "従業員名字",
            "リファイナー名",
            "面談日",
            "面談時間",
            "情報開示",
            "相談内容",
            "管理者コメント",
            JOB_SHORTNAMES[0],
            JOB_SHORTNAMES[0] + "メモ",
            JOB_SHORTNAMES[1],
            JOB_SHORTNAMES[1] + "メモ",
            JOB_SHORTNAMES[2],
            JOB_SHORTNAMES[2] + "メモ",
            JOB_SHORTNAMES[3],
            JOB_SHORTNAMES[3] + "メモ",
            JOB_SHORTNAMES[4],
            JOB_SHORTNAMES[4] + "メモ",
            JOB_SHORTNAMES[5],
            JOB_SHORTNAMES[5] + "メモ",
            "プライベート1メモ",
            "プライベート2メモ",
            "プライベート3メモ"
            };
}
