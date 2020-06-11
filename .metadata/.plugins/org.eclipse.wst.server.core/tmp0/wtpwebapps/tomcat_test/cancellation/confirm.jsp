<%-- 
    Document   : index
    Created on : 2013/08/27, 14:33:18
    Author     : katom
--%>
<%@page import="java.util.*"%>
<%
  String contextPath = request.getContextPath();

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

    <%
      request.setCharacterEncoding("UTF-8");

      String tetheringNone = request.getParameter("tetheringNone");
      String tetheringYes = request.getParameter("tetheringYes");
      String practicZipCode = request.getParameter("practicZipCode");
      String practicAddress = request.getParameter("practicAddress");
      String practicName = request.getParameter("practicName");
      String practicTelNo = request.getParameter("practicTelNo");
      String practicMailAddress = request.getParameter("practicMailAddress");
    %>

    <table width="100%" height="400" border="0">

      <tr>
        <td align="center">
          <table class="headerStyle">
            <tr>
              <td align="left" width="60%">Webサイト受付システム  ${sessionScope.entryKindString} 登録内容確認</td>
              <td align="right" width="40%">${sessionScope.companyName} 様</td>
            </tr>
            <tr>
              <td align="right" width="100%" colspan="2"><a href="<%= contextPath%>/LogoutServlet">ログアウト</a></td>
            </tr>
          </table>

          <br/><br/><br/>

          <form method="post" action="<%= contextPath%>/RequestSubmitServlet" name="confirm">
            <table class="fullb0">
              <tr>
                <td align="center">
                  <table class="formWidth">
                    <tr>
                      <td class="normalFont" align="left" width="30%" colspan="5"> 以下内容で確定いたします。よろしいでしょうか。 </td>
                    </tr>

                    <tr>
                      <td align="left" width="100%" colspan="3"> <br/> </td>
                    </tr>

                    <%
                      List<String> telNoList = (List<String>) session.getAttribute("telNoList");

                      if (telNoList != null) {
                        for (int i = 0; i < telNoList.size(); i ++) {
                          session.setAttribute("telNo", telNoList.get(i));

                          if (i == 0) {
                            session.setAttribute("telNoTitle", "対象番号(解約)");
                          }
                          else {
                            session.setAttribute("telNoTitle", "");
                          }
                    %>
                    <tr>
                      <td class="normalFont" align="left" width="30%" colspan="2"> ${telNoTitle} </td>
                      <td class="normalFont" width="70%"> ${telNo} </td>
                    </tr>
                    <%
                        }
                      }
                    %>
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
                               value="確定"
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
