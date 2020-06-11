<%-- 
    Document   : loginerror
    Created on : 2013/09/09, 15:05:52
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
        <title>ログインエラー</title>
    </head>
    <body>
        <h1>ログインに失敗しました。</h1>
        <p>エラーコード：&nbsp;${errCode}</p>
        <p>エラーメッセージ：&nbsp;${errMsg}</p>
        <a href="<%= contextPath %>/login.jsp">再試行</a>
    </body>
</html>
