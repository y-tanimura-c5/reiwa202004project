<%-- 
    Document   : index
    Created on : 2013/08/27, 14:33:18
    Author     : katom
--%>
<%@page import="java.util.*"%>
<%
  String contextPath = request.getContextPath();

  request.setCharacterEncoding("UTF-8");

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
<link rel="stylesheet" href="<%= contextPath%>/wstyle.css" type="text/css">
<script type="text/javascript" src="<%= contextPath%>/js/jquery.min.js"></script>
<title>Webサイト受付</title>
</head>

<body class="formBgColor">

<table width="100%" height="400" border="0">
  <tr>
    <td align="center">
      <table class="headerStyle">
        <tr>
          <td align="left" width="60%">Webサイト受付システム ${sessionScope.entryKindString} 登録</td>
          <td align="right" width="40%">${sessionScope.companyName} 様</td>
        </tr>
        <tr>
          <td align="right" width="100%" colspan="2"><a href="<%= contextPath%>/LogoutServlet">ログアウト</a></td>
        </tr>
      </table>

      <br/><br/><br/>

      <table class="formMsgWidth">
        <tr>
          <td align="left" class="errorFont"><%= errMsg %></td>
        </tr>
      </table>

      <form method="post" action="<%= contextPath%>/ContractEntryConfirmServlet" enctype="multipart/form-data" name="form">
      <table class="fullb0">
        <tr>
          <td align="center">
            <table class="formWidth">
              <tr>
                <td class="normalFont" align="left" width="30%">
                  契約情報CSVファイル
                </td>
                <td align="left" width="70%">
                  <input type="file" name="contractDataFile">
                </td>
              </tr>
            </table>

            <br/><br/><br/><br/>

            <table class="submitButtonWidth">
              <tr>
                <td align="left" width="250px">
                    <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                    <input type="hidden" name="companyName" value="${sessionScope.companyName}">
                    <input type="hidden" name="entryKind" value="${sessionScope.entryKind}">
                    <input type="hidden" name="entryKindString" value="${sessionScope.entryKindString}">
                    <input type="submit"
                           value="登録"
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
          </td>
        </tr>
      </table>
      </form>
    </td>
  </tr>
</table>

</body>
</html>
