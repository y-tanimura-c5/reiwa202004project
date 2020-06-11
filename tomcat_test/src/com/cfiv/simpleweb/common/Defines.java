package com.cfiv.simpleweb.common;

/**
 * 定数定義クラス
 *
 * @author katom
 */
public class Defines {
    /**
     * jspファイル一覧
     */
    public static final String REDIRECT = "REDIRECT:";
    public static final String JSP_INDEX = "/index.jsp";
    public static final String JSP_LOGIN = "/login.jsp";
    public static final String JSP_ERROR = "/error.jsp";
    public static final String JSP_LOGIN_ERROR = "/loginerror.jsp";
    public static final String JSP_SUBMIT = "/submit.jsp";

    public static final String JSP_NEWCONTRACT_FORM = "/newcontract/form.jsp";
    public static final String JSP_NEWCONTRACT_CONFIRM = "/newcontract/confirm.jsp";

    public static final String JSP_MODELCHANGE_LIST = "/modelchange/list.jsp";
    public static final String JSP_MODELCHANGE_FORM = "/modelchange/form.jsp";
    public static final String JSP_MODELCHANGE_CONFIRM = "/modelchange/confirm.jsp";

    public static final String JSP_CANCELLATION_LIST = "/cancellation/list.jsp";
    public static final String JSP_CANCELLATION_CONFIRM = "/cancellation/confirm.jsp";

    public static final String JSP_CONTRACTENTRY_FORM = "/contractentry/form.jsp";
    public static final String JSP_CONTRACTENTRY_CONFIRM = "/contractentry/confirm.jsp";

    /**
     * ログイン情報
     */
    public static final String LOGIN_STATUS = "login";

    /**
     * メニューコメント
     */
    public static final String MENU_COMMENT = "menuComment";

    /**
     * お客様情報
     */
    public static final String CUSTOMER_ID = "customerID";
    public static final String USER_KIND = "userKind";
    public static final String COMPANY_NAME = "companyName";
    public static final String SECTION_NAME = "sectionName";
    public static final String CHARGE_NAME = "chargeName";
    public static final String CUSTOMER_ZIPCODE = "customerZipCode";
    public static final String CUSTOMER_ADDRESS = "customerAddress";
    public static final String CUSTOMER_TELNO = "customerTelNo";
    public static final String CUSTOMER_MAILADDRESS = "customerMailAddress";
    public static final String PASS_CODE = "passCode";

    /**
     * 機種情報
     */
    public static final String DEVICE_NAME = "deviceName";
    public static final String STORAGE_SIZE = "storageSize";
    public static final String COLOR_NAME = "colorName";
    public static final String CONTRACT_KIND = "contractKind";
    public static final String CONTRACT_SPAN = "contractSpan";
    public static final String PRICE_PLAN = "prisePlan";

    /**
     * 登録種別情報
     */
    public static final String ENTRY_KIND = "entryKind";
    public static final int ENTRYKIND_NEWCONTRACT = 0;
    public static final int ENTRYKIND_MODELCHANGE = 1;
    public static final int ENTRYKIND_CANCELLATION = 2;
    public static final int ENTRYKIND_CONTRACTENTRY = 3;

    /**
     * 登録種別文字列情報
     */
    public static final String ENTRYKIND_STRING = "entryKindString";
    public static final String ENTRYKINDSTRING_NEWCONTRACT = "新規・追加";
    public static final String ENTRYKINDSTRING_MODELCHANGE = "機種変更";
    public static final String ENTRYKINDSTRING_CANCELLATION = "解約";
    public static final String ENTRYKINDSTRING_CONTRACTENTRY = "契約情報";

    /**
     * 登録情報
     */
    public static final String TETHERING_NONE = "tetheringNone";
    public static final String TETHERING_YES = "tetheringYes";
    public static final String PRACTIC_ZIPCODE = "practicZipCode";
    public static final String PRACTIC_ADDRESS = "practicAddress";
    public static final String PRACTIC_NAME = "practicName";
    public static final String PRACTIC_TELNO = "practicTelNo";
    public static final String PRACTIC_MAILADDRESS = "practicMailAddress";

    /**
     * 電話番号情報
     */
    public static final String SELECTED_DEVICE = "selectedDevice";
    public static final String TELNO_LIST = "telNoList";

    /**
     * 契約情報
     */
    public static final String CONTRACT_DATALIST = "contractDataList";
    public static final String DEVICE_TELNO = "deviceTelNo";
    public static final String START_MONTH = "startMonth";
    public static final String END_MONTH = "endMonth";
    public static final String NOW_PRICE = "nowPrice";
    public static final String CONTINUATION_PRICE = "continuationPrice";
    public static final String RENEWAL_PRICE = "renewalPrice";
    public static final String UPDATE_DATE = "updateDate";

    /**
     * ユーザ名
     */
    public static final String TAG_USERNAME = "username";

    /**
     * パスワード
     */
    public static final String TAG_PASSWORD = "password";

    /*
     * ユーザ属性(0＝管理者／1＝一般ユーザ)
     */
    /**
     * ユーザ属性：管理者
     */
    public static final int USERKIND_ADMIN = 0;
    /**
     * ユーザ属性：一般ユーザ
     */
    public static final int USERKIND_NORMAL = 1;

    /*
     * ログイン結果(0＝ログイン成功／1＝認証失敗／2＝アカウント無効／3＝アカウント失効)
     */
    /**
     * ログイン結果：ログイン成功
     */
    public static final int LOGINSTATUS_SUCCEEDED = 0;
    /**
     * ログイン結果：認証失敗
     */
    public static final int LOGINSTATUS_FAILED = 1;

    /**
     * 契約情報文字数
     */
    public static final int MAXLENGTH_CUSTOMERID = 5;
    public static final int MAXLENGTH_CONTRACTNAME = 100;
    public static final int MAXLENGTH_DEVICETELNO = 11;
    public static final int MAXLENGTH_DEVICENAME = 100;
    public static final int MAXLENGTH_NOWPRICE = 10;
    public static final int MAXLENGTH_CONTINUATIONPRICE = 10;
    public static final int MAXLENGTH_RENEWALPRICE = 10;

    /**
     * ローカルホストアドレス
     */
    public static final String LOCALHOST_ADDRESS = "127.0.0.1";

    /**
     * プロジェクトホームディレクトリ
     */
    public static final String PROJECTHOME_DIR = "/usr/local/simpleweb/";

    /**
     * コンフィグファイル名
     */
    public static final String CONFIG_FILENAME = PROJECTHOME_DIR + "conf/simpleweb.properties";

    /**
     * コンテンツファイルホームディレクトリ
     */
    public static final String CONTENTSFILE_HOME = "webapps";
}
