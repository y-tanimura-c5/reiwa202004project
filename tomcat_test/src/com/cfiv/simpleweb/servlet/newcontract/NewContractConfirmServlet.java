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
import com.cfiv.simpleweb.common.Util;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "NewContractConfirmServlet", urlPatterns = { "/NewContractConfirmServlet" })
public class NewContractConfirmServlet extends HttpServlet {

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

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // 各種パラメータ取得
        String customerID = request.getParameter(Defines.CUSTOMER_ID);
        String tetheringYes = request.getParameter(Defines.TETHERING_YES);
        String tetheringNone = request.getParameter(Defines.TETHERING_NONE);
        String practicZipCode = request.getParameter(Defines.PRACTIC_ZIPCODE);
        String practicAddress = request.getParameter(Defines.PRACTIC_ADDRESS);
        String practicName = request.getParameter(Defines.PRACTIC_NAME);
        String practicTelNo = request.getParameter(Defines.PRACTIC_TELNO);
        String practicMailAddress = request.getParameter(Defines.PRACTIC_MAILADDRESS);

        // 登録種別文字列
        String entryKindString = Defines.ENTRYKINDSTRING_NEWCONTRACT;

        // ログ先頭文字列作成
        String logHeader = entryKindString + " 登録内容確認：";

        Log.log(Log.INFO, logHeader + "お客様ID = " + customerID);

        try {
            // お客様IDが設定されている場合
            if (customerID != null) {
                HttpSession session = request.getSession();
                session.setAttribute(Defines.TETHERING_YES, tetheringYes);
                session.setAttribute(Defines.TETHERING_NONE, tetheringNone);
                session.setAttribute(Defines.PRACTIC_ZIPCODE, practicZipCode);
                session.setAttribute(Defines.PRACTIC_ADDRESS, practicAddress);
                session.setAttribute(Defines.PRACTIC_NAME, practicName);
                session.setAttribute(Defines.PRACTIC_TELNO, practicTelNo);
                session.setAttribute(Defines.PRACTIC_MAILADDRESS, practicMailAddress);

                Log.log(Log.INFO, logHeader + "テザリングあり = " + tetheringYes);
                Log.log(Log.INFO, logHeader + "テザリングなし = " + tetheringNone);
                Log.log(Log.INFO, logHeader + "送付先郵便番号 = " + practicZipCode);
                Log.log(Log.INFO, logHeader + "送付先住所 = " + practicAddress);
                Log.log(Log.INFO, logHeader + "担当者氏名 = " + practicName);
                Log.log(Log.INFO, logHeader + "担当者電話番号 = " + practicTelNo);
                Log.log(Log.INFO, logHeader + "担当者メールアドレス = " + practicMailAddress);

                int tetheringYesNum = 0;
                int tetheringNoneNum = 0;
                try {
                    tetheringYesNum = Integer.parseInt(request.getParameter(Defines.TETHERING_YES));
                }
                catch (NumberFormatException e) {
                    // 無視
                }
                try {
                    tetheringNoneNum = Integer.parseInt(request.getParameter(Defines.TETHERING_NONE));
                }
                catch (NumberFormatException e) {
                    // 無視
                }

                boolean checkNG = false;

                // 申込台数チェック
                if (tetheringYesNum + tetheringNoneNum <= 0) {
                    errMsg += "申込台数が0台です。<br/>";
                    checkNG = true;
                }

                // 郵便番号文字列チェック
                if (!Util.isEmpty(practicZipCode)) {
                    if (practicZipCode.length() > 8) {
                        errMsg += "郵便番号はハイフン含む8文字としてください。<br/>";
                        checkNG = true;
                    }
                    else if (!Util.isDigitAndDash(practicZipCode)) {
                        errMsg += "郵便番号は数値およびハイフンのみ入力してください。<br/>";
                        checkNG = true;
                    }
                }

                // 住所文字列チェック
                if (!Util.isEmpty(practicAddress)) {
                    if (practicAddress.length() > 100) {
                        errMsg += "100文字を超える住所は入力できません。<br/>";
                        checkNG = true;
                    }
                }

                // 住所文字列チェック
                if (!Util.isEmpty(practicName)) {
                    if (practicName.length() > 100) {
                        errMsg += "25文字を超える氏名は入力できません。<br/>";
                        checkNG = true;
                    }
                }

                // 電話番号文字列チェック
                if (!Util.isEmpty(practicTelNo)) {
                    if (practicTelNo.length() > 13) {
                        errMsg += "電話番号はハイフン含む13文字までとしてください。<br/>";
                        checkNG = true;
                    }
                    else if (!Util.isDigitAndDash(practicTelNo)) {
                        errMsg += "電話番号は数値およびハイフンのみ入力してください。<br/>";
                        checkNG = true;
                    }
                }

                // メールアドレス文字列チェック
                if (!Util.isEmpty(practicMailAddress)) {
                    if (practicMailAddress.length() > 100) {
                        errMsg += "100文字を超えるメールアドレスは入力できません。<br/>";
                        checkNG = true;
                    }
                    else if (!Util.canUseMailAddress(practicMailAddress)) {
                        errMsg += "全角文字、特殊文字を含むメールアドレスは入力できません。<br/>";
                        checkNG = true;
                    }
                }

                // チェックNGの場合
                if (checkNG) {
                    // フォーム画面を再表示する
                    target = Defines.JSP_NEWCONTRACT_FORM;
                }
                else {
                    // 確認画面を表示する
                    target = Defines.JSP_NEWCONTRACT_CONFIRM;
                }
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
