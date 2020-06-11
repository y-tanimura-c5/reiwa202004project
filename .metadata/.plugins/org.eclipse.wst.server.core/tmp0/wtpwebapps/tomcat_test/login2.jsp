<%-- 
    Document   : login
    Created on : 2015/01/06, 18:00:00
    Author     : katom
--%>

<%
  String contextPath = request.getContextPath();
  String errMsg = (String) request.getAttribute("errMsg");
  if (errMsg == null) {
    errMsg = "";
  }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" href="<%= contextPath%>/wstyle.css" type="text/css">

    <script type="text/javascript">
      function check() {
        var errMsg = "";
        if (document.loginform.username.value == "") {
          errMsg += "・お客様IDが未入力です。\n";
        }
        if (document.loginform.password.value == "") {
          errMsg += "・パスワードが未入力です。\n";
        }
        var ret = false;
        if (errMsg != "") {
          document.getElementById("emsg").innerText = errMsg;
          ;
          document.getElementById("sv-emsg").innerText = "";
          ret = false;
        }
        else {
          // OK
          ret = true;
        }

        return ret;
      }
    </script>

    <title>Webサイト受付システム</title>

  </head>
  <body class="formBgColor" onLoad="document.loginform.username.focus()">
    <table width="100%" height="400" border="0">
      <tr>
        <td align="center">

          <br/><br/><br/>

          <table class="loginTitleWidth">
            <tr>
              <td>
                <font class="titleFont">Webサイト受付システム ログイン</font>
              </td>
            </tr>
          </table>

          <br/><br/><br/>

          <table class="loginMsgWidth">
            <tr>
              <td align="left">
                <div class="c_red" id="sv-emsg"><%= errMsg%></div>
                <div class="c_red" id="emsg"></div>
              </td>
            </tr>
          </table>
          <form method="post" action="<%= contextPath%>/LoginServlet" name="login" onSubmit="return check();">
            <table class="fullb0">
              <tr>
                <td align="center">
                  <table class="loginWidth">
                    <tr>
                      <td align="left" width="100%">
                        <font class="normalFont">お客様ID</font>
                      </td>
                    </tr>
                    <tr>
                      <td align="left" width="100%">
                        <input type="text" name="username" size="38">
                      </td>
                    </tr>
                    <tr>
                      <td align="left" width="100%">
                        <br/>
                      </td>
                    </tr>
                    <tr>
                      <td align="left" width="100%">
                        <font class="normalFont">パスワード</font>
                      </td>
                    </tr>
                    <tr>
                      <td align="left" width="100%">
                        <input type="password" name="password" size="38">
                      </td>
                    </tr>
                    <tr>
                      <td align="left" width="100%">
                        <br/>
                      </td>
                    </tr>
                    <tr>
                      <td align="center">
                        <br/>
                        <input type="submit"
                               value="ログイン"
                               style="background-color:#cc9900;"
                               onmouseover="this.style.fontWeight = '900'" 
                               onmouseout="this.style.fontWeight = 'normal'">
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
