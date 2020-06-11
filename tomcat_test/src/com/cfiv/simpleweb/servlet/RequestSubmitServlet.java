/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cfiv.simpleweb.common.Defines;
import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.task.ContractDataSubmitTask;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "RequestSubmitServlet", urlPatterns = { "/RequestSubmitServlet" })
public class RequestSubmitServlet extends HttpServlet {

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
        request.setCharacterEncoding("UTF-8");

        String logHeader = " 登録完了：";

        try {
            // 各種パラメータ取得
            String customerID = request.getParameter(Defines.CUSTOMER_ID);
            String deviceName = request.getParameter(Defines.DEVICE_NAME);
            String colorName = request.getParameter(Defines.COLOR_NAME);
            String customerZipCode = request.getParameter(Defines.CUSTOMER_ZIPCODE);
            String customerAddress = request.getParameter(Defines.CUSTOMER_ADDRESS);
            String sectionName = request.getParameter(Defines.SECTION_NAME);
            String chargeName = request.getParameter(Defines.CHARGE_NAME);
            String customerTelNo = request.getParameter(Defines.CUSTOMER_TELNO);
            String customerMailAddress = request.getParameter(Defines.CUSTOMER_MAILADDRESS);
            String practicZipCode = request.getParameter(Defines.PRACTIC_ZIPCODE);
            String practicAddress = request.getParameter(Defines.PRACTIC_ADDRESS);
            String practicName = request.getParameter(Defines.PRACTIC_NAME);
            String practicTelNo = request.getParameter(Defines.PRACTIC_TELNO);
            String practicMailAddress = request.getParameter(Defines.PRACTIC_MAILADDRESS);

            int entryKind = 0;
            int storageSize = 0;
            int tetheringNone = 0;
            int tetheringYes = 0;
            try {
                entryKind = Integer.parseInt(request.getParameter(Defines.ENTRY_KIND));
                storageSize = Integer.parseInt(request.getParameter(Defines.STORAGE_SIZE));
                tetheringNone = Integer.parseInt(request.getParameter(Defines.TETHERING_NONE));
                tetheringYes = Integer.parseInt(request.getParameter(Defines.TETHERING_YES));
            }
            catch (NumberFormatException e) {
                // 数値の取得失敗は無視
            }

            HttpSession session = request.getSession();
            List<String> telNoList = new ArrayList<>();
            try {
                telNoList = (List<String>) session.getAttribute(Defines.TELNO_LIST);
            }
            catch (ClassCastException e) {
                // 電話番号リストの取得失敗は無視
            }

            // ログ先頭文字列作成
            switch (entryKind) {
            case Defines.ENTRYKIND_NEWCONTRACT:
                logHeader = Defines.ENTRYKINDSTRING_NEWCONTRACT + logHeader;
                break;

            case Defines.ENTRYKIND_MODELCHANGE:
                logHeader = Defines.ENTRYKINDSTRING_MODELCHANGE + logHeader;
                break;

            case Defines.ENTRYKIND_CANCELLATION:
                logHeader = Defines.ENTRYKINDSTRING_CANCELLATION + logHeader;
                break;

            case Defines.ENTRYKIND_CONTRACTENTRY:
                logHeader = Defines.ENTRYKINDSTRING_CONTRACTENTRY + logHeader;
                break;
            }

            Log.log(Log.INFO, logHeader + "お客様ID = " + customerID);

            // お客様IDが設定されている場合
            if (customerID != null) {
                // 新規・追加および機種変更の場合は登録情報ログ出力
                if (entryKind == Defines.ENTRYKIND_NEWCONTRACT || entryKind == Defines.ENTRYKIND_MODELCHANGE) {
                    Log.log(Log.INFO, logHeader + "機種 = " + deviceName);
                    Log.log(Log.INFO, logHeader + "容量 = " + storageSize + "GB");
                    Log.log(Log.INFO, logHeader + "色 = " + colorName);
                    Log.log(Log.INFO, logHeader + "テザリングなし = " + tetheringNone + "台");
                    Log.log(Log.INFO, logHeader + "テザリングあり = " + tetheringYes + "台");
                    Log.log(Log.INFO, logHeader + "担当者：郵便番号 = " + customerZipCode);
                    Log.log(Log.INFO, logHeader + "担当者：所在地 = " + customerAddress);
                    Log.log(Log.INFO, logHeader + "担当者：部署 = " + sectionName);
                    Log.log(Log.INFO, logHeader + "担当者：氏名 = " + chargeName);
                    Log.log(Log.INFO, logHeader + "担当者：電話番号 = " + customerTelNo);
                    Log.log(Log.INFO, logHeader + "担当者：e-mailアドレス = " + customerMailAddress);
                    Log.log(Log.INFO, logHeader + "実施者：郵便番号 = " + practicZipCode);
                    Log.log(Log.INFO, logHeader + "実施者：送付先住所 = " + practicAddress);
                    Log.log(Log.INFO, logHeader + "実施者：氏名 = " + practicName);
                    Log.log(Log.INFO, logHeader + "実施者：電話番号 = " + practicTelNo);
                    Log.log(Log.INFO, logHeader + "実施者：e-mailアドレス = " + practicMailAddress);
                }

                // 機種変更および解約の場合は電話番号リストログ出力
                if (entryKind == Defines.ENTRYKIND_MODELCHANGE || entryKind == Defines.ENTRYKIND_CANCELLATION) {
                    for (int i = 0; i < telNoList.size(); i++) {
                        Log.log(Log.INFO, logHeader + "対象番号(解約) = " + telNoList.get(i));
                    }
                }

                // 契約情報の場合は情報登録＆ログ出力
                if (entryKind == Defines.ENTRYKIND_CONTRACTENTRY) {
                    // 契約情報を取得する
                    List<Map<String, String>> cList = (List<Map<String, String>>) session.getAttribute("contractDataList");

                    // 契約情報登録タスクを生成する
                    ContractDataSubmitTask contractTask = new ContractDataSubmitTask(cList);

                    // 契約情報登録を行う
                    contractTask.execute();
                }

                // 登録完了画面を表示する
                target = Defines.JSP_SUBMIT;
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
            throw new ServletException("Request Submit Failed(" + e.getMessage() + ")");
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
