/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

/**
 * <p>タイトル: カラム情報クラス</p>
 * <p>説明: カラム情報(カラムタイプ等)を格納するクラス</p>
 * <p>著作権: Copyright (c) 2012</p>
 * <p>会社名: </p>
 * @author Tanimura
 * @version 1.0
 */
public class DBColumnInformation {
    /**
     * カラムタイプ
     */
    public int Type = 0;

    /**
     * カラム番号
     */
    public int Index = 0;

    /**
     * カラム名
     */
    public String Name = "";

    /**
     * コンストラクタ
     */
    public DBColumnInformation() {
    }

    /**
     * コンストラクタ
     * @param type カラムタイプ
     * @param index カラム番号
     * @param name カラム名
     */
    public DBColumnInformation(int type, int index, String name) {
        Type = type;
        Index = index;
        Name = name;
    }
}
