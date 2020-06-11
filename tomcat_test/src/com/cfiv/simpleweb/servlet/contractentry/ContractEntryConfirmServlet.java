/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.servlet.contractentry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.cfiv.simpleweb.common.Defines;
import com.cfiv.simpleweb.common.FormatDate;
import com.cfiv.simpleweb.common.Log;
import com.cfiv.simpleweb.common.Util;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

/**
 *
 * @author tanimura
 */
@WebServlet(name = "ContractEntryConfirmServlet", urlPatterns = { "/ContractEntryConfirmServlet" })
public class ContractEntryConfirmServlet extends HttpServlet {

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
        String customerID = "";
        FileItem uploadItem = null;

        response.setContentType("text/html;charset=UTF-8");

        // ファイルデータ(FileItemオブジェクト)のListオブジェクト取得
        List<FileItem> fileItemlist = getFileItemList(request);

        // 登録種別
        int entryKind = Defines.ENTRYKIND_CONTRACTENTRY;

        // 登録種別文字列
        String entryKindString = Defines.ENTRYKINDSTRING_CONTRACTENTRY;

        // ログ先頭文字列作成
        String logHeader = entryKindString + " 契約情報一覧：";

        try {
            // ファイルデータ(FileItemオブジェクト)を順に処理
            Iterator<FileItem> iterator = fileItemlist.iterator();
            while (iterator.hasNext()) {
                FileItem fItem = (FileItem) iterator.next();

                // フィールドデータの場合
                if (fItem.isFormField()) {
                    // お客様IDの取得
                    if (fItem.getFieldName().equals(Defines.CUSTOMER_ID)) {
                        customerID = fItem.getString();
                        Log.log(Log.INFO, logHeader + "お客様ID = " + customerID);
                    }
                }
                // ファイルデータの場合
                else {
                    // ファイルデータのファイル名(PATH名含む)を取得
                    String fileName = fItem.getName();
                    Log.log(Log.INFO, logHeader + "アップロードファイル名 = " + fileName);

                    if ((fileName != null) && (!fileName.equals(""))) {
                        // ファイルデータを保存
                        uploadItem = fItem;
                    }
                }
            }

            if (uploadItem != null) {
                ReadCSVResult result = readCsv(uploadItem);

                if (result.result) {
                    HttpSession session = request.getSession();
                    session.setAttribute(Defines.LOGIN_STATUS, "OK");
                    session.setAttribute(Defines.ENTRY_KIND, entryKind);
                    session.setAttribute(Defines.ENTRYKIND_STRING, entryKindString);
                    session.setAttribute(Defines.CUSTOMER_ID, customerID);
                    session.setAttribute(Defines.CONTRACT_DATALIST, result.dataList);
                    target = Defines.JSP_CONTRACTENTRY_CONFIRM;
                }
                else {
                    errMsg = result.errorMessage;
                    target = Defines.JSP_CONTRACTENTRY_FORM;
                }
            }
            else {
                errMsg = "ファイルが選択されていません。";
                target = Defines.JSP_CONTRACTENTRY_FORM;
            }
        }
        catch (Exception e) {
            Log.log(Log.INFO, logHeader + "Exception発生");
            Log.printStackTrace(e);

            // InternalServerErrorを返す
            throw new ServletException("Contract List Failed(" + e.getMessage() + ")");
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

    private List<FileItem> getFileItemList(HttpServletRequest request) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> list;
        try {
            list = upload.parseRequest((RequestContext) request);
        }
        catch (FileUploadException e) {
            return new ArrayList<>();
        }

        return list;
    }

    class ReadCSVResult {
        public boolean result = true;
        public String errorMessage = "";
        List<Map<String, String>> dataList = new ArrayList<>();
    }

    private ReadCSVResult readCsv(FileItem fileItem) throws FileNotFoundException, IOException {
        ReadCSVResult result = new ReadCSVResult();
        InputStream is = null;
        Reader r = null;
        CSVReader reader = null;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        try {
            is = fileItem.getInputStream();
            r = new InputStreamReader(is, "SJIS");
            reader = new CSVReader(r);
            String[] line;

            int count = 1;
            try {
                while ((line = reader.readNext()) != null) {
                    count++;
                    if (line.length < 9) {
                        result.errorMessage += "CSVファイルの列数が足りません(" + count + "行目、列数=" + line.length + ")<br/>";
                        result.result = false;
                        break;
                    }

                    // データチェック
                    String customerID = line[0];
                    if (customerID.length() > Defines.MAXLENGTH_CUSTOMERID) {
                        result.errorMessage += "お客様IDは" + Defines.MAXLENGTH_CUSTOMERID + "文字以内としてください。<br/>";
                        result.result = false;
                    }

                    String contractName = line[1];
                    if (contractName.length() > Defines.MAXLENGTH_CONTRACTNAME) {
                        result.errorMessage += "契約名義は全角最大" + Defines.MAXLENGTH_CONTRACTNAME + "文字までとしてください。<br/>";
                        result.result = false;
                    }

                    String deviceTelNo = line[2];
                    if (deviceTelNo.length() > Defines.MAXLENGTH_DEVICETELNO) {
                        result.errorMessage += "電話番号は数値のみ" + Defines.MAXLENGTH_DEVICETELNO + "文字までとしてください。<br/>";
                        result.result = false;
                    }

                    String deviceName = line[3];
                    if (deviceName.length() > Defines.MAXLENGTH_DEVICENAME) {
                        result.errorMessage += "機種名称は最大" + Defines.MAXLENGTH_DEVICENAME + "文字までとしてください。<br/>";
                        result.result = false;
                    }

                    FormatDate startMonth = Util.getDateFromYYYYMString(line[4]);
                    if (startMonth == null) {
                        result.errorMessage += "契約月は「○○○○年○月」の形式で記載してください。<br/>";
                        result.result = false;
                    }

                    FormatDate endMonth = Util.getDateFromYYYYMString(line[5]);
                    if (endMonth == null) {
                        result.errorMessage += "満了月は「○○○○年○月」の形式で記載してください。<br/>";
                        result.result = false;
                    }

                    int nowPrice = Util.getPriceFromString(line[6]);
                    if (nowPrice < 0) {
                        result.errorMessage += "現行利用料金は金額(\\○,○○○)または数値を設定してください。<br/>";
                        result.result = false;
                    }

                    int continuationPrice = Util.getPriceFromString(line[7]);
                    if (continuationPrice < 0) {
                        result.errorMessage += "継続時利用料金は金額(\\○,○○○)または数値を設定してください。<br/>";
                        result.result = false;
                    }

                    int renewalPrice = Util.getPriceFromString(line[8]);
                    if (renewalPrice < 0) {
                        result.errorMessage += "機変後利用料金は金額(\\○,○○○)または数値を設定してください。<br/>";
                        result.result = false;
                    }

                    if (!result.result) {
                        break;
                    }

                    Map<String, String> data = new HashMap<String, String>();
                    data.put("CUSTOMER_ID", customerID);
                    data.put("CONTRACT_NAME", contractName);
                    data.put("DEVICE_TELNO", deviceTelNo);
                    data.put("DEVICE_NAME", deviceName);
                    data.put("START_MONTH", startMonth.toJPMonthString());
                    data.put("END_MONTH", endMonth.toJPMonthString());
                    data.put("NOW_PRICE", currencyFormat.format(nowPrice));
                    data.put("CONTINUATION_PRICE", currencyFormat.format(continuationPrice));
                    data.put("RENEWAL_PRICE", currencyFormat.format(renewalPrice));

                    result.dataList.add(data);
                }
            }
            catch (CsvValidationException | IOException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }
        finally {
            try {
                if (r != null) {
                    r.close();
                }
            }
            catch (IOException e) {
            }
            try {
                if (is != null) {
                    is.close();
                }
            }
            catch (IOException e) {
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e) {
            }
        }

        return result;
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
