<%-- 
    Document   : index
    Created on : 2013/08/27, 14:33:18
    Author     : katom
--%>
<%@page import="java.util.*"%>
<%
  String contextPath = request.getContextPath();

  String errMsg = (String) request.getAttribute("errMsg");
  if (errMsg == null) {
    errMsg = "";
  }

  if (session.getAttribute("login") == null) {
%>
<jsp:forward page="/login.jsp" />
<%
  }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%= contextPath %>/wstyle.css" type="text/css">
<script type="text/javascript" src="<%= contextPath %>/js/jquery.min.js"></script>
<title>Webサイト受付</title>
</head>

<body class="formBgColor">
  
<table width="100%" height="400" border="0">
  <tr>
    <td align="center" valign="top">
      <table class="headerStyle">
        <tr>
          <td align="left" width="60%">Webサイト受付システム ${sessionScope.entryKindString}</td>
          <td align="right" width="40%">${sessionScope.companyName} 様</td>
        </tr>
        <tr>
          <td align="right" width="100%" colspan="2"><a href="<%= contextPath %>/LogoutServlet">ログアウト</a></td>
        </tr>
      </table>

      <br/><br/><br/>

      <table class="contractMsgWidth">
        <tr>
          <td align="left" class="errorFont"><%= errMsg %></td>
        </tr>
      </table>
            
      <form method="post" action="<%= contextPath%>/CancellationConfirmServlet" name="changelist">
        <table class="contractWidth">
          <tr>
            <td align="center">
              <table class="contractHeaderStyle">
                <tr>
                  <td align="right">${sessionScope.updateDate}時点 ※金額は税抜価格</td>
                </tr>
              </table>

              <table class="fullb0">
                <tr>
                  <td align="center" class="tableLineColor">
                    <table class="fullb0">
                      <tr>
                        <td class="tableTitleBgColor">
                          <table class="p100b0" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <th class="s_moji" align="center" width="4%">選択</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="4%">No</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="12%">ご契約名義</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="10%">電話番号</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="12%">機種</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="11%">ご契約月</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="11%">ご契約満了月</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="12%">現在のご利用金額</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="12%">【機変未実施の場合】<br/>現行機種を継続利用</th>
                              <th class="tableLineColor" width="1" height="1"></th>
                              <th class="s_moji" align="center" width="12%">【機変未実施の場合】<br/>新機種へ変更後</th>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>

                    <table class="fullb0">
                      <tr>
                        <td>
                          <%
                          List<Map> cList = (List<Map>) session.getAttribute("contractDataList");

                          // 契約情報を1行ずつ取得する
                          for (int i = 0; i < cList.size(); i ++) {
                            Map data = cList.get(i);
                            
                            String contractName = (String) data.get("CONTRACT_NAME");
                            String deviceTelNo = (String) data.get("DEVICE_TELNO");
                            String deviceName = (String) data.get("DEVICE_NAME");
                            String startMonth = (String) data.get("START_MONTH");
                            String endMonth = (String) data.get("END_MONTH");
                            String nowPrice = (String) data.get("NOW_PRICE");
                            String continuationPrice = (String) data.get("CONTINUATION_PRICE");
                            String renewalPrice = (String) data.get("RENEWAL_PRICE");
                          %>
                            <tr>
                              <td class="tableBgColor">
                                <table class="p100b0" border="0" cellspacing="0" cellpadding="0">
                                  <tr>
                                    <td class="s_moji" align="center" width="4%" align="center">
                                      <input type="checkbox" name="selectedDevice" value="<%= deviceTelNo %>">
                                    </td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="4%"><%= i+1 %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="12%"><%= contractName %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="10%"><%= deviceTelNo %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="12%"><%= deviceName %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="11%"><%= startMonth %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="11%"><%= endMonth %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="12%"><%= nowPrice %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="12%"><%= continuationPrice %></td>
                                    <td class="tableLineColor" width="1" height="1"></td>
                                    <td class="s_moji" align="center" width="12%"><%= renewalPrice %></td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          <%
                          }
                          %>
                        </tr>
                      </td>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>

        <br/>

        <table class="submitButtonWidth">
          <tr>
            <td align="left" width="250px">
                <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                <input type="hidden" name="companyName" value="${sessionScope.companyName}">
                <input type="hidden" name="entryKind" value="${sessionScope.entryKind}">
                <input type="hidden" name="entryKindString" value="${sessionScope.entryKindString}">
                <input type="submit"
                       value="解約"
                       style="background-color:#00b0f0;"
                       onmouseover="this.style.fontWeight = '900'" 
                       onmouseout="this.style.fontWeight = 'normal'">
            </td>
            <td align="left">
              <br/>
            </td>
            <td align="right" width="250px">
              <input type="button"
                     value="戻る"
                     style="background-color:#7f7f7f;"
                     onmouseover="this.style.fontWeight = '900'" 
                     onmouseout="this.style.fontWeight = 'normal'"
                     onClick="history.back()">
            </td>
          </tr>
        </table>
      </form>
    </td>
  </tr>
</table>

</body>
</html>
