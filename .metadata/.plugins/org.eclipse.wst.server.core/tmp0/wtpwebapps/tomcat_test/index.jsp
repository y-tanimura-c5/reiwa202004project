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
    <title>面談結果登録システム</title>
  </head>
  <body class="formBgColor">
    <table width="100%" height="400" border="0">

      <%
        int userKind = (Integer) session.getAttribute("userKind");
      %>
      <tr>
        <td align="center" valign="top">
          <table class="headerStyle">
            <tr>
              <td align="left" width="60%">面談結果登録システム メニュー</td>
              <td align="right" width="40%">${sessionScope.companyName} 様</td>
            </tr>
            <tr>
              <td align="right" width="100%" colspan="2"><a href="<%= contextPath%>/LogoutServlet">ログアウト</a></td>
            </tr>
          </table>

          <br/><br/><br/>

          <table class="fullb0">
            <tr>
              <td align="center">
                <table class="menuWidth">
                  <%
                    if (userKind == 0) {
                  %>
                  <tr>
                    <td align="center">
                      <br/>
                      <form method="post" action="<%= contextPath%>/NewContractServlet" name="index">
                        <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                        <input type="submit"
                               value="新規登録"
                               style="background-color:#00b050;"
                               onmouseover="this.style.fontWeight = '900'"
                               onmouseout="this.style.fontWeight = 'normal'">
                      </form>
                    </td>
                  </tr>
                  <tr>
                    <td align="center">
                      <br/>
                      <form method="post" action="<%= contextPath%>/ModelChangeListServlet" name="index">
                        <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                        <input type="submit"
                               value="登録内容検索"
                               style="background-color:#00b0f0;"
                               onmouseover="this.style.fontWeight = '900'"
                               onmouseout="this.style.fontWeight = 'normal'">
                      </form>
                    </td>
                  </tr>
<%--
                  <tr>
                    <td align="center">
                      <br/>
                      <form method="post" action="<%= contextPath%>/CancellationListServlet" name="index">
                        <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                        <input type="submit"
                               value="解約"
                               style="background-color:#d99696;"
                               onmouseover="this.style.fontWeight = '900'"
                               onmouseout="this.style.fontWeight = 'normal'">
                      </form>
                    </td>
                  </tr>
--%>
                  <%
                    }
                  %>
                  <%
                    if (userKind == 1) {
                  %>
                  <tr>
                    <td align="center">
                      <br/>
                      <form method="post" action="<%= contextPath%>/ContractEntryServlet" name="menuform">
                        <input type="hidden" name="customerID" value="${sessionScope.customerID}">
                        <input type="submit"
                               value="契約情報登録"
                               style="background-color:#00b050;"
                               onmouseover="this.style.fontWeight = '900'"
                               onmouseout="this.style.fontWeight = 'normal'">
                      </form>
                    </td>
                  </tr>
                  <%
                    }
                  %>
                  <tr>
                    <td align="center"> <br/> </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td align="center">
                <table class="menuCommentWidth">
                  <tr>
                    <td class="normalFont">
                      ${sessionScope.menuComment}
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>

  </body>
</html>
