/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cfiv.simpleweb.common.Defines;
import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.task.AuthTask;
import com.cfiv.simpleweb.task.MenuCommentTask;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String target = null;
        String errMsg = "";

        response.setContentType("text/html; charset=UTF-8");

        // ユーザ名取得
        String userName = request.getParameter(Defines.TAG_USERNAME);
        // パスワード取得
        String password = request.getParameter(Defines.TAG_PASSWORD);

        Log.log(Log.INFO, "ログイン：ユーザ名 = " + userName);

        try {
            // ユーザ名が設定されている場合
            if (userName != null) {
                // ユーザ認証タスクを生成する
                AuthTask authTask = new AuthTask(userName, password);

                // ユーザ認証を行う
                authTask.execute();

                // 認証結果を取得する
                boolean auth = authTask.isAuth();

                // 認証に成功した場合
                if (auth) {
                    Log.log(Log.INFO, "ログイン：成功");

                    // メニューコメント取得タスクを生成する
                    MenuCommentTask menuCommentTask = new MenuCommentTask(authTask.getUserKind());

                    // メニューコメント取得を行う
                    menuCommentTask.execute();

                    // Sessionにアカウント情報を格納
                    HttpSession session = request.getSession();
                    session.setAttribute(Defines.LOGIN_STATUS, "OK");
                    session.setAttribute(Defines.CUSTOMER_ID, authTask.getCustomerId());
                    session.setAttribute(Defines.USER_KIND, authTask.getUserKind());
                    session.setAttribute(Defines.COMPANY_NAME, authTask.getCompanyName());
                    session.setAttribute(Defines.SECTION_NAME, authTask.getSectionName());
                    session.setAttribute(Defines.CHARGE_NAME, authTask.getChargeName());
                    session.setAttribute(Defines.CUSTOMER_ZIPCODE, authTask.getZipCode());
                    session.setAttribute(Defines.CUSTOMER_ADDRESS, authTask.getAddress());
                    session.setAttribute(Defines.CUSTOMER_TELNO, authTask.getTelNo());
                    session.setAttribute(Defines.CUSTOMER_MAILADDRESS, authTask.getMailAddress());
                    session.setAttribute(Defines.PASS_CODE, authTask.getPassCode());
                    session.setAttribute(Defines.MENU_COMMENT, menuCommentTask.getMenuComment());
                    Log.log(Log.INFO, "ログイン：お客様ID = " + authTask.getCustomerId());
                    Log.log(Log.INFO, "ログイン：ユーザ種別 = " + authTask.getUserKind());
                    Log.log(Log.INFO, "ログイン：会社名称 = " + authTask.getCompanyName());
                    Log.log(Log.INFO, "ログイン：部署 = " + authTask.getSectionName());
                    Log.log(Log.INFO, "ログイン：担当者氏名 = " + authTask.getChargeName());
                    Log.log(Log.INFO, "ログイン：郵便番号 = " + authTask.getZipCode());
                    Log.log(Log.INFO, "ログイン：所在地 = " + authTask.getAddress());
                    Log.log(Log.INFO, "ログイン：電話番号 = " + authTask.getTelNo());
                    Log.log(Log.INFO, "ログイン：メールアドレス = " + authTask.getMailAddress());
                    Log.log(Log.INFO, "ログイン：暗証番号 = " + authTask.getPassCode());
                    Log.log(Log.INFO, "メニュー：メニューコメント = " + menuCommentTask.getMenuComment());
                    target = Defines.JSP_INDEX;
                }
                // 失敗した場合
                else {
                    Log.log(Log.INFO, "ログイン：失敗");

                    errMsg = "アカウントまたはパスワードが一致しません。";
                    Log.log(Log.INFO, errMsg);
                    target = Defines.JSP_LOGIN;
                }
            }
            // ログイン画面からsubmitされていない場合
            else {
                target = Defines.JSP_LOGIN;
            }
        }
        catch (Exception e) {
            Log.log(Log.INFO, "ログイン：Exception発生");
            Log.printStackTrace(e);

            // InternalServerErrorを返す
            throw new ServletException("Login Failed(" + e.getMessage() + ")");
        }
        finally {
            try {
                if (target != null) {
                    request.setAttribute("errMsg", errMsg);
                    getServletContext().getRequestDispatcher(target).forward(request, response);
                }
            }
            catch (IOException e) {
                //無視
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
