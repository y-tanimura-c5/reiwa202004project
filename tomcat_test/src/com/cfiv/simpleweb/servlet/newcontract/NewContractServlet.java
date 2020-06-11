/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.servlet.newcontract;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cfiv.simpleweb.common.Defines;
import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.task.FormCreateTask;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "NewContractServlet", urlPatterns = { "/NewContractServlet" })
public class NewContractServlet extends HttpServlet {

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

        // 登録種別
        int entryKind = Defines.ENTRYKIND_NEWCONTRACT;

        // 登録種別文字列
        String entryKindString = Defines.ENTRYKINDSTRING_NEWCONTRACT;

        // ログ先頭文字列作成
        String logHeader = entryKindString + " 登録：";

        Log.log(Log.INFO, logHeader + "お客様ID = " + customerID);

        try {
            // お客様IDが設定されている場合
            if (customerID != null) {
                // 入力フォーム情報取得タスクを生成する
                FormCreateTask task = new FormCreateTask(customerID);

                // 入力フォーム情報取得を行う
                task.execute();

                // Sessionにアカウント情報を格納
                HttpSession session = request.getSession();
                session.setAttribute(Defines.LOGIN_STATUS, "OK");
                session.setAttribute(Defines.ENTRY_KIND, entryKind);
                session.setAttribute(Defines.ENTRYKIND_STRING, entryKindString);
                session.setAttribute(Defines.CUSTOMER_ID, task.getCustomerId());
                session.setAttribute(Defines.USER_KIND, task.getUserKind());
                session.setAttribute(Defines.COMPANY_NAME, task.getCompanyName());
                session.setAttribute(Defines.SECTION_NAME, task.getSectionName());
                session.setAttribute(Defines.CHARGE_NAME, task.getChargeName());
                session.setAttribute(Defines.CUSTOMER_ZIPCODE, task.getZipCode());
                session.setAttribute(Defines.CUSTOMER_ADDRESS, task.getAddress());
                session.setAttribute(Defines.CUSTOMER_TELNO, task.getTelNo());
                session.setAttribute(Defines.CUSTOMER_MAILADDRESS, task.getMailAddress());
                session.setAttribute(Defines.PASS_CODE, task.getPassCode());
                session.setAttribute(Defines.DEVICE_NAME, task.getDeviceName());
                session.setAttribute(Defines.STORAGE_SIZE, task.getStorageSize());
                session.setAttribute(Defines.COLOR_NAME, task.getColorName());
                session.setAttribute(Defines.CONTRACT_KIND, task.getContractKind());
                session.setAttribute(Defines.CONTRACT_SPAN, task.getContractSpan());
                session.setAttribute(Defines.PRICE_PLAN, task.getPrisePlan());

                Log.log(Log.INFO, logHeader + "ユーザ種別 = " + task.getUserKind());
                Log.log(Log.INFO, logHeader + "会社名称 = " + task.getCompanyName());
                Log.log(Log.INFO, logHeader + "部署 = " + task.getSectionName());
                Log.log(Log.INFO, logHeader + "担当者氏名 = " + task.getChargeName());
                Log.log(Log.INFO, logHeader + "郵便番号 = " + task.getZipCode());
                Log.log(Log.INFO, logHeader + "所在地 = " + task.getAddress());
                Log.log(Log.INFO, logHeader + "電話番号 = " + task.getTelNo());
                Log.log(Log.INFO, logHeader + "メールアドレス = " + task.getMailAddress());
                Log.log(Log.INFO, logHeader + "暗証番号 = " + task.getPassCode());
                Log.log(Log.INFO, logHeader + "機器名称 = " + task.getDeviceName());
                Log.log(Log.INFO, logHeader + "容量 = " + task.getStorageSize());
                Log.log(Log.INFO, logHeader + "色名称 = " + task.getColorName());
                Log.log(Log.INFO, logHeader + "契約形態 = " + task.getContractKind());
                Log.log(Log.INFO, logHeader + "契約期間 = " + task.getContractSpan());
                Log.log(Log.INFO, logHeader + "料金プラン = " + task.getPrisePlan());

                target = Defines.JSP_NEWCONTRACT_FORM;
            }
            // ログイン画面からsubmitされていない場合
            else {
                target = Defines.JSP_LOGIN;
            }
        }
        catch (Exception e) {
            Log.log(Log.INFO, logHeader + "Exception発生");
            Log.printStackTrace(e);

            // InternalServerErrorを返す
            throw new ServletException("New Contract Form Create Failed(" + e.getMessage() + ")");
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
