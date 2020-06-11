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
import com.cfiv.simpleweb.task.CustomerDetailTask;
import com.cfiv.simpleweb.task.MenuCommentTask;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "MenuServlet", urlPatterns = { "/MenuServlet" })
public class MenuServlet extends HttpServlet {

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

        // お客様ID取得
        String customerID = request.getParameter(Defines.CUSTOMER_ID);

        Log.log(Log.INFO, "メニュー：お客様ID = " + customerID);

        try {
            // お客様IDが設定されている場合
            if (customerID != null) {
                // お客様情報取得タスクを生成する
                CustomerDetailTask customerTask = new CustomerDetailTask(customerID);

                // お客様情報取得を行う
                customerTask.execute();

                // メニューコメント取得タスクを生成する
                MenuCommentTask menuCommentTask = new MenuCommentTask(customerTask.getUserKind());

                // メニューコメント取得を行う
                menuCommentTask.execute();

                // Sessionにアカウント情報を格納
                HttpSession session = request.getSession();
                session.setAttribute(Defines.LOGIN_STATUS, "OK");
                session.setAttribute(Defines.CUSTOMER_ID, customerTask.getCustomerId());
                session.setAttribute(Defines.USER_KIND, customerTask.getUserKind());
                session.setAttribute(Defines.COMPANY_NAME, customerTask.getCompanyName());
                session.setAttribute(Defines.SECTION_NAME, customerTask.getSectionName());
                session.setAttribute(Defines.CHARGE_NAME, customerTask.getChargeName());
                session.setAttribute(Defines.CUSTOMER_ZIPCODE, customerTask.getZipCode());
                session.setAttribute(Defines.CUSTOMER_ADDRESS, customerTask.getAddress());
                session.setAttribute(Defines.CUSTOMER_TELNO, customerTask.getTelNo());
                session.setAttribute(Defines.CUSTOMER_MAILADDRESS, customerTask.getMailAddress());
                session.setAttribute(Defines.PASS_CODE, customerTask.getPassCode());
                session.setAttribute(Defines.MENU_COMMENT, menuCommentTask.getMenuComment());
                Log.log(Log.INFO, "メニュー：ユーザ種別 = " + customerTask.getUserKind());
                Log.log(Log.INFO, "メニュー：会社名称 = " + customerTask.getCompanyName());
                Log.log(Log.INFO, "メニュー：部署 = " + customerTask.getSectionName());
                Log.log(Log.INFO, "メニュー：担当者氏名 = " + customerTask.getChargeName());
                Log.log(Log.INFO, "メニュー：郵便番号 = " + customerTask.getZipCode());
                Log.log(Log.INFO, "メニュー：所在地 = " + customerTask.getAddress());
                Log.log(Log.INFO, "メニュー：電話番号 = " + customerTask.getTelNo());
                Log.log(Log.INFO, "メニュー：メールアドレス = " + customerTask.getMailAddress());
                Log.log(Log.INFO, "メニュー：暗証番号 = " + customerTask.getPassCode());
                Log.log(Log.INFO, "メニュー：メニューコメント = " + menuCommentTask.getMenuComment());
                target = Defines.JSP_INDEX;
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
    }// </editor-fold>

}
