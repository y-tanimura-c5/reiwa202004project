<%-- 
    Document   : index
    Created on : 2013/08/27, 14:33:18
    Author     : katom
--%>
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

    <table width="100%" height="400" border="0">

      <%
        int entryKind = (Integer) session.getAttribute("entryKind");
      %>

      <tr>
        <td align="center">
          <table class="headerStyle">
            <tr>
              <td align="left" width="60%">Webサイト受付システム ${sessionScope.entryKindString} 登録完了</td>
              <td align="right" width="40%">${sessionScope.companyName} 様</td>
            </tr>
            <tr>
              <td align="right" width="100%" colspan="2"><a href="<%= contextPath%>/LogoutServlet">ログアウト</a></td>
            </tr>
          </table>

          <br/><br/><br/>

          <form method="post" action="<%= contextPath%>/MenuServlet" name="submit">
            <table class="fullb0">
              <tr>
                <td align="center">
                  <table class="formWidth">
                    <tr>
                      <td align="center" width="100%">
                        <font class="normalFont">
                        <br/>
                        ${sessionScope.entryKindString}の登録が完了いたしました。<br/>
                        <%
                          if (entryKind != 3) {
                        %>
                        <br/>
                        登録内容につきましては、担当者様および実施者様へ自動送信メールをお送りしておりますのでご確認ください。
                        <%
                          }
                        %>
                        <br/><br/>
                        </font>
                      </td>
                    </tr>
                  </table>

                  <br/>

                  <table class="submitButtonWidth">
                    <tr>
                      <td align="left"> <br/> </td>
                      <td align="center" width="300px">
                        <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                        <input type="hidden" name="companyName" value="${sessionScope.companyName}">
                        <input type="submit"
                               value="メニューへ戻る"
                               style="background-color:#cc9900;"
                               onmouseover="this.style.fontWeight = '900'" 
                               onmouseout="this.style.fontWeight = 'normal'">
                      </td>
                      <td align="right"> <br/> </td>
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
