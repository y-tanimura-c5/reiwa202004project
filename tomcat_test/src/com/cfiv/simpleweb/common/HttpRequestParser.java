package com.cfiv.simpleweb.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * HTTPリクエストのパラメータパーサークラス</p>
 *
 * 主にHttpServletRequest#getAttribute()のラッパー用のクラスです。
 * 型変換や値取得先を意識せずにパラメータの取り出しが行えます。
 *
 * @author katom
 */
public class HttpRequestParser {

    private final HttpServletRequest request;

    /**
     * コンストラクタ
     *
     * @param request パース対象のリクエストオブジェクト
     */
    public HttpRequestParser(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 指定したパラメータが存在するか判定します。
     *
     * @param param パラメータ名
     * @return パラメータが存在する場合はtrue
     */
    public boolean exists(String param) {
        return existsAttr(param)
                || existsSession(param);
    }

    /**
     * 指定したパラメータがリクエストスコープに存在するか判定します。
     *
     * @param param パラメータ名
     * @return パラメータがリクエストスコープに存在する場合はtrue
     */
    public boolean existsAttr(String param) {
        return null != request.getAttribute(param);
    }

    /**
     * 指定したパラメータがセッションスコープに存在するか判定します。
     *
     * @param param パラメータ名
     * @return パラメータがセッションスコープに存在する場合はtrue
     */
    public boolean existsSession(String param) {
        return null != request.getSession().getAttribute(param);
    }

    /**
     * 指定したパラメータがint型であるか判定します。
     *
     * @param param パラメータ名
     * @return パラメータがint型である場合はtrue
     */
    public boolean isInteger(String param) {
        try {
            getInt(param);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 指定したパラメータのint型の値を取得します。 リクエストスコープに存在しない場合は、セッションスコープから取得します。
     *
     * @param param パラメータ名
     * @return 値
     * @throws ClassCastException リクエストスコープにもセッションスコープにも存在しない場合
     */
    public int getInt(String param) {
        if (existsAttr(param)) {
            return Integer.parseInt(String.valueOf(request.getAttribute(param)));
        }
        else {
            return (Integer) request.getSession().getAttribute(param);
        }
    }

    /**
     * 指定したパラメータの値を取得します。 リクエストスコープに存在しない場合は、セッションスコープから取得します。
     * セッションスコープにも存在しない場合はnullを返します。
     *
     * @param param パラメータ名
     * @return 値
     */
    public String getString(String param) {
        String str = null;
        if (existsAttr(param)) {
            str = (String) request.getAttribute(param);
        }
        else {
            str = (String) request.getSession().getAttribute(param);
        }
        return str;
    }

    /**
     * 指定したパラメータの値をint配列として取得します。 値は以下のフォーマットを想定しています。
     *
     * x;y;z または x;y;z;
     *
     * x,y,zには数値が入ります。
     *
     * @param param パラメータ名
     * @return int配列
     * @throws NumberFormatException 値が想定外のフォーマットの場合
     */
    public List<Integer> getIntList(String param) {
        // ADD-START 20130923 TAKENO 課題リストNo.47
        if (getString(param) == null) {
            return null;
        }
        // ADD-END 20130923 TAKENO 課題リストNo.47
        List<Integer> list = new ArrayList<Integer>();
        String[] array = getString(param).split(";");

        if (array.length == 1 && "".equals(array[0])) {
            return list;
        }
        for (String str : array) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    /**
     * 指定したパラメータの値をString配列として取得します。 値は以下のフォーマットを想定しています。
     *
     * x;y;z または x;y;z;
     *
     * x,y,zには文字列が入ります。
     *
     * @param param パラメータ名
     * @return String配列
     */
    public List<String> getStringList(String param) {
        if (getString(param) == null) {
            return null;
        }

        List<String> list = new ArrayList<>();
        String[] array = getString(param).split(";");

        if (array.length == 1 && "".equals(array[0])) {
            return list;
        }

        for (String str : array) {
            list.add(str);
        }

        return list;
    }

    /**
     * 指定したパラメータの値をセッションスコープから取得します。
     *
     * @param param パラメータ名
     * @return 値
     */
    public String getStrFromSession(String param) {
        return (String) request.getSession().getAttribute(param);
    }
}
