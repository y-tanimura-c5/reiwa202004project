/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

import java.util.ArrayList;

/**
 * <p>タイトル: DB操作リストクラス</p>
 * <p>説明: 複数のDB操作データを格納するクラス</p>
 * <p>著作権: Copyright (c) 2012</p>
 * <p>会社名: </p>
 * @author Tanimura
 * @version 1.0
 */
public class DBControllerList {
    /**
    * DB操作リスト
    */
    ArrayList<DBController> ControllerList = new ArrayList<DBController>();

    /**
     * コンストラクタ
     */
    public DBControllerList() {
    }

    /**
     * リストへの追加
     * @param control DB操作データ
     * @return boolean 追加結果
     */
    public boolean add(DBController control) {
        return ControllerList.add(control);
    }

    /**
     * リストへの追加
     * @param controls レコードリスト
     * @return boolean 追加結果
     */
    public boolean addList(DBControllerList controls) {
        boolean result = false;

        if (controls != null) {
            result = ControllerList.addAll(controls.getList());
        }

        return result;
    }

    /**
     * 指定インデックスのデータ通知
     * @param index インデックス
     * @return JADBControl レコードデータ
     */
    public DBController get(int index) {
        return ControllerList.get(index);
    }

    /**
     * データリストの通知
     * @return ArrayList データリスト
     */
    public ArrayList<DBController> getList() {
        return ControllerList;
    }

    /**
     * データ数の通知
     * @return int データ数
     */
    public int size() {
        return ControllerList.size();
    }

    /**
     * リストのクリア
     */
    public void clear() {
        ControllerList.clear();
    }
}
