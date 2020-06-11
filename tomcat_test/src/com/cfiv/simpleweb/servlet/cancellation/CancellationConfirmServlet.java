/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.servlet.cancellation;

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

/**
 *
 * @author tanimura
 */
@WebServlet(name = "CancellationConfirmServlet", urlPatterns = { "/CancellationConfirmServlet" })
public class CancellationConfirmServlet extends HttpServlet {

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

        // 登録種別文字列
        String entryKindString = Defines.ENTRYKINDSTRING_CANCELLATION;

        // ログ先頭文字列作成
        String logHeader = entryKindString + " 登録内容確認：";

        Log.log(Log.INFO, logHeader + "お客様ID = " + customerID);

        try {
            // お客様IDが設定されている場合
            if (customerID != null) {
                // 機種変更対象となる機器が設定されている場合
                if (telNoList != null && telNoList.size() != 0) {
                    // Sessionにアカウント情報を格納
                    HttpSession session = request.getSession();
                    session.setAttribute(Defines.LOGIN_STATUS, "OK");
                    session.setAttribute(Defines.ENTRYKIND_STRING, entryKindString);
                    session.setAttribute(Defines.CUSTOMER_ID, customerID);
                    session.setAttribute(Defines.TELNO_LIST, telNoList);

                    target = Defines.JSP_CANCELLATION_CONFIRM;

                    Log.log(Log.INFO, logHeader + "対象機器数 = " + telNoList.size());
                    for (int i = 0; i < telNoList.size(); i++) {
                        Log.log(Log.INFO, logHeader + "対象機器 = " + telNoList.get(i));
                    }
                }
                else {
                    target = Defines.JSP_CANCELLATION_LIST;
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
