<%-- 
    Document   : error
    Created on : 2013/09/08, 14:16:28
    Author     : katom
--%>

<%
  String contextPath = request.getContextPath();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<%= contextPath%>/wstyle.css" type="text/css">
    <script type="text/javascript" src="<%= contextPath%>/js/jquery.min.js"></script>
    <title>エラー</title>
  </head>
  <body>

    <table>
      <tr>
        <td valign="top">
          <!-- ↓↓↓ --- main --- ↓↓↓ -->
          <table>
            <tr>
              <td>
                予期しないエラーが発生しました。
              </td>
            </tr>
            <tr>
              <td>
                <p>エラーコード：&nbsp;${errCode}</p>
                <p>エラーメッセージ：&nbsp;${errMsg}</p>
              </td>
            </tr>
          </table>
          <!-- ↑↑↑ --- main --- ↑↑↑ -->
        </td>
      </tr>
    </table>

  </body>
</html>
