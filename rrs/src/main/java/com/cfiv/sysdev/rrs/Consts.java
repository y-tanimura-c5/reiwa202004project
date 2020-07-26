package com.cfiv.sysdev.rrs;

public class Consts {
    public static final boolean EXIST = false;
    public static final boolean DELETED = true;

    public static final int CONTENTKIND_JOB = 0;
    public static final int CONTENTKIND_PRIVATE = 1;

    public static final int INTERVIEWDATECODE_LAST = 0;
    public static final int INTERVIEWDATECODE_REGION = 1;

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
            0,
            1,
            10
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

    public static int USERROLE_REFINER_CODE = USERROLE_CODES[2];
    public static int PASTINTERVIEW_NUM = 3;
    public static int CONVERTEDIMAGE_WIDTH = 1600;
}