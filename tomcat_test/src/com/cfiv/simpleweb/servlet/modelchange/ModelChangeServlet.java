/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.servlet.modelchange;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cfiv.simpleweb.common.Defines;
import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.common.Util;
import com.cfiv.simpleweb.task.FormCreateTask;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "ModelChangeServlet", urlPatterns = { "/ModelChangeServlet" })
public class ModelChangeServlet extends HttpServlet {

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

        // 選択された機器番号取得
        List<String> telNoList = Util.convertStringList(request.getParameterValues(Defines.SELECTED_DEVICE));

        // 登録種別
        int entryKind = Defines.ENTRYKIND_MODELCHANGE;

        // 登録種別文字列
        String entryKindString = Defines.ENTRYKINDSTRING_MODELCHANGE;

        // ログ先頭文字列作成
        String logHeader = entryKindString + " 登録：";

        Log.log(Log.INFO, logHeader + "お客様ID = " + customerID);

        try {
            // お客様IDが設定されている場合
            if (customerID != null) {
                // 機種変更対象となる機器が設定されている場合
                if (telNoList != null && telNoList.size() != 0) {
                    // 入力フォーム情報取得タスクを生成する
                    FormCreateTask formTask = new FormCreateTask(customerID);

                    // 入力フォーム情報取得を行う
                    formTask.execute();

                    // Sessionにアカウント情報を格納
                    HttpSession session = request.getSession();
                    session.setAttribute(Defines.LOGIN_STATUS, "OK");
                    session.setAttribute(Defines.ENTRY_KIND, entryKind);
                    session.setAttribute(Defines.ENTRYKIND_STRING, entryKindString);
                    session.setAttribute(Defines.CUSTOMER_ID, formTask.getCustomerId());
                    session.setAttribute(Defines.USER_KIND, formTask.getUserKind());
                    session.setAttribute(Defines.COMPANY_NAME, formTask.getCompanyName());
                    session.setAttribute(Defines.SECTION_NAME, formTask.getSectionName());
                    session.setAttribute(Defines.CHARGE_NAME, formTask.getChargeName());
                    session.setAttribute(Defines.CUSTOMER_ZIPCODE, formTask.getZipCode());
                    session.setAttribute(Defines.CUSTOMER_ADDRESS, formTask.getAddress());
                    session.setAttribute(Defines.CUSTOMER_TELNO, formTask.getTelNo());
                    session.setAttribute(Defines.CUSTOMER_MAILADDRESS, formTask.getMailAddress());
                    session.setAttribute(Defines.PASS_CODE, formTask.getPassCode());
                    session.setAttribute(Defines.DEVICE_NAME, formTask.getDeviceName());
                    session.setAttribute(Defines.STORAGE_SIZE, formTask.getStorageSize());
                    session.setAttribute(Defines.COLOR_NAME, formTask.getColorName());
                    session.setAttribute(Defines.CONTRACT_KIND, formTask.getContractKind());
                    session.setAttribute(Defines.CONTRACT_SPAN, formTask.getContractSpan());
                    session.setAttribute(Defines.PRICE_PLAN, formTask.getPrisePlan());
                    session.setAttribute(Defines.TELNO_LIST, telNoList);

                    target = Defines.JSP_MODELCHANGE_FORM;

                    Log.log(Log.INFO, logHeader + "ユーザ種別 = " + formTask.getUserKind());
                    Log.log(Log.INFO, logHeader + "会社名称 = " + formTask.getCompanyName());
                    Log.log(Log.INFO, logHeader + "部署 = " + formTask.getSectionName());
                    Log.log(Log.INFO, logHeader + "担当者氏名 = " + formTask.getChargeName());
                    Log.log(Log.INFO, logHeader + "郵便番号 = " + formTask.getZipCode());
                    Log.log(Log.INFO, logHeader + "所在地 = " + formTask.getAddress());
                    Log.log(Log.INFO, logHeader + "電話番号 = " + formTask.getTelNo());
                    Log.log(Log.INFO, logHeader + "メールアドレス = " + formTask.getMailAddress());
                    Log.log(Log.INFO, logHeader + "暗証番号 = " + formTask.getPassCode());
                    Log.log(Log.INFO, logHeader + "機器名称 = " + formTask.getDeviceName());
                    Log.log(Log.INFO, logHeader + "容量 = " + formTask.getStorageSize());
                    Log.log(Log.INFO, logHeader + "色名称 = " + formTask.getColorName());
                    Log.log(Log.INFO, logHeader + "契約形態 = " + formTask.getContractKind());
                    Log.log(Log.INFO, logHeader + "契約期間 = " + formTask.getContractSpan());
                    Log.log(Log.INFO, logHeader + "料金プラン = " + formTask.getPrisePlan());
                    Log.log(Log.INFO, logHeader + "対象機器数 = " + telNoList.size());
                    for (int i = 0; i < telNoList.size(); i++) {
                        Log.log(Log.INFO, logHeader + "対象機器 = " + telNoList.get(i));
                    }
                }
                else {
                    target = Defines.JSP_MODELCHANGE_LIST;
                    errMsg = "機種変更対象となる機器を選択してください。";

                    Log.log(Log.INFO, logHeader + "対象機器 = なし");
                }
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
            throw new ServletException("Model Change Form Create Failed(" + e.getMessage() + ")");
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
