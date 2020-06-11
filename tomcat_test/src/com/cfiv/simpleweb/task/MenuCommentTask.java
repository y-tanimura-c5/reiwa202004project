/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.task;

import java.sql.SQLException;

import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.dbaccess.DBAccess;
import com.cfiv.simpleweb.dbaccess.DBController;

/**
 *
 * @author tanimura
 */
public class MenuCommentTask extends AbstractTask {
    private String menuComment;
    private int userKind;

    public MenuCommentTask(int kind) {
        userKind = kind;
    }

    public String getMenuComment() {
        return menuComment;
    }

    @Override
    protected boolean doTask() throws Exception {
        DBAccess dbAccess = new DBAccess();

        try {
            dbAccess.open();

            // メニューコメントを取得する
            DBController com = dbAccess.getMenuCommentController(userKind);

            // メニューコメントが存在する場合
            if (com != null) {
                menuComment = (String) com.getByName("MENU_COMMENT");
            }
        }
        catch (Exception e) {
            Log.printStackTrace(e);

            // Exceptionをスローする
            throw e;
        }
        finally {
            try {
                dbAccess.close();
            }
            catch (SQLException e) {
                //無視
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "MenuCommentTask{" + "MenuComment=" + getMenuComment() + '}';
    }
}
