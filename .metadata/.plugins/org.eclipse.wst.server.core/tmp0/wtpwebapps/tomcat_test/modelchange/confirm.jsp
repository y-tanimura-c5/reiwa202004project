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

                <tr>
                  <td class="normalFont" align="left" width="30%" colspan="2"> 1.機種 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.deviceName} </td>
                </tr>

                <tr>
                  <td align="left" width="100%" colspan="3"> <br/> </td>
                </tr>

                <tr>
                  <td class="normalFont" align="left" width="30%" colspan="2"> 2.容量 </td>
                  <td class="normalFont" align="left" width="20%"> ${sessionScope.storageSize} GB </td>
                </tr>

                <tr>
                  <td align="left" width="100%" colspan="3"> <br/> </td>
                </tr>

                <tr>
                  <td class="normalFont" align="left" width="30%" colspan="2"> 3.色 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.colorName} </td>
                </tr>

                <tr>
                  <td align="left" width="100%" colspan="3"> <br/> </td>
                </tr>

                <tr>
                  <td class="normalFont" align="left" width="30%" colspan="2"> 4.テザリング(オプション) </td>
                  <td class="normalFont" align="left" width="70%"> あり ${sessionScope.tetheringYes} 台 </td>
                </tr>
                <tr>
                  <td class="normalFont" align="left" width="30%" colspan="2"> <br/> </td>
                  <td class="normalFont" align="left" width="70%"> なし ${sessionScope.tetheringNone} 台 </td>
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
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> ${telNoTitle} </td>
                  <td class="normalFont" width="70%"> ${telNo} </td>
                </tr>
                <%
                    }
                  }
                %>

                <tr>
                  <td align="left" width="100%" colspan="3"> <br/> </td>
                </tr>

                <tr>
                  <td class="normalFont" align="left" colspan="3"> 5.ご契約手続き 担当者様情報 </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> 郵便番号 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.customerZipCode} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> 所在地 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.customerAddress} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> 部署 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.sectionName} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> 担当者様氏名 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.chargeName} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> ご連絡先電話番号 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.customerTelNo} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> e-mailアドレス </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.customerMailAddress} </td>
                </tr>

                <tr>
                  <td align="left" width="100%" colspan="3"> <br/> </td>
                </tr>

                <tr>
                  <td class="normalFont" align="left" colspan="3"> 6.仮申込手続き 実施者様情報 </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> ご送付先郵便番号 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.practicZipCode} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> ご送付先住所 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.practicAddress} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> 実施者様氏名 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.practicName} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> ご連絡先電話番号 </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.practicTelNo} </td>
                </tr>
                <tr>
                  <td align="left" width="5%"> <br/> </td>
                  <td class="normalFont" align="left" width="25%"> e-mailアドレス </td>
                  <td class="normalFont" align="left" width="70%"> ${sessionScope.practicMailAddress} </td>
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
                    <input type="hidden" name="deviceName" value="${sessionScope.deviceName}">
                    <input type="hidden" name="storageSize" value="${sessionScope.storageSize}">
                    <input type="hidden" name="colorName" value="${sessionScope.colorName}">
                    <input type="hidden" name="tetheringNone" value="${sessionScope.tetheringNone}">
                    <input type="hidden" name="tetheringYes" value="${sessionScope.tetheringYes}">
                    <input type="hidden" name="customerZipCode" value="${sessionScope.customerZipCode}">
                    <input type="hidden" name="customerAddress" value="${sessionScope.customerAddress}">
                    <input type="hidden" name="sectionName" value="${sessionScope.sectionName}">
                    <input type="hidden" name="chargeName" value="${sessionScope.chargeName}">
                    <input type="hidden" name="customerTelNo" value="${sessionScope.customerTelNo}">
                    <input type="hidden" name="customerMailAddress" value="${sessionScope.customerMailAddress}">
                    <input type="hidden" name="practicZipCode" value="${sessionScope.practicZipCode}">
                    <input type="hidden" name="practicAddress" value="${sessionScope.practicAddress}">
                    <input type="hidden" name="practicName" value="${sessionScope.practicName}">
                    <input type="hidden" name="practicTelNo" value="${sessionScope.practicTelNo}">
                    <input type="hidden" name="practicMailAddress" value="${sessionScope.practicMailAddress}">
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
