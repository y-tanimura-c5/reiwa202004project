<%-- 
    Document   : index
    Created on : 2013/08/27, 14:33:18
    Author     : katom
--%>
<%
  String contextPath = request.getContextPath();

  request.setCharacterEncoding("UTF-8");

  String errMsg = (String) request.getAttribute("errMsg");
  if (errMsg == null) {
    errMsg = "";
  }

  String practicZipCode = (String) session.getAttribute("practicZipCode");
  if (practicZipCode == null) {
    practicZipCode = (String) session.getAttribute("customerZipCode");
  }
  
  String practicAddress = (String) session.getAttribute("practicAddress");
  if (practicAddress == null) {
    practicAddress = (String) session.getAttribute("customerAddress");
  }

  String practicName = (String) session.getAttribute("practicName");
  if (practicName == null) {
    practicName = (String) session.getAttribute("chargeName");
  }

  String practicTelNo = (String) session.getAttribute("practicTelNo");
  if (practicTelNo == null) {
    practicTelNo = (String) session.getAttribute("customerTelNo");
  }

  String practicMailAddress = (String) session.getAttribute("practicMailAddress");
  if (practicMailAddress == null) {
    practicMailAddress = (String) session.getAttribute("customerMailAddress");
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

      <form method="post" action="<%= contextPath%>/NewContractConfirmServlet" name="form">
      <table class="fullb0">
        <tr>
          <td align="center">
            <table class="formWidth">
              <tr>
                <td class="normalFont" align="left" width="30%" colspan="2">
                  1.機種
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="deviceName" size="20" value="${sessionScope.deviceName}">
                </td>
              </tr>

              <tr>
                <td align="left" width="100%" colspan="5">
                  <br/>
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" width="30%" colspan="2">
                  2.容量
                </td>
                <td align="left" width="20%" colspan="2">
                  <input type="text" disabled="disabled" name="storageSize" size="10" value="${sessionScope.storageSize}">
                </td>
                <td class="normalFont" align="left" width="50%">
                  GB
                </td>
              </tr>

              <tr>
                <td align="left" width="100%" colspan="5">
                  <br/>
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" width="30%" colspan="2">
                  3.色
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="colorName" size="10" value="${sessionScope.colorName}">
                </td>
              </tr>

              <tr>
                <td align="left" width="100%" colspan="5">
                  <br/>
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" width="30%" colspan="2">
                  4.テザリング(オプション)
                </td>
                <td class="normalFont" align="left" width="9%">
                  あり
                </td>
                <td align="left" width="11%">
                  <select name="tetheringYes">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                    <option value="13">13</option>
                    <option value="14">14</option>
                    <option value="15">15</option>
                    <option value="16">16</option>
                    <option value="17">17</option>
                    <option value="18">18</option>
                    <option value="19">19</option>
                    <option value="20">20</option>
                  </select>
                </td>
                <td class="normalFont" align="left" width="50%">
                  台
                </td>
              </tr>

              <tr>
                <td align="left" width="30%" colspan="2">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="9%">
                  なし
                </td>
                <td align="left" width="11%">
                  <select name="tetheringNone">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                    <option value="13">13</option>
                    <option value="14">14</option>
                    <option value="15">15</option>
                    <option value="16">16</option>
                    <option value="17">17</option>
                    <option value="18">18</option>
                    <option value="19">19</option>
                    <option value="20">20</option>
                  </select>
                </td>
                <td class="normalFont" align="left" width="50%">
                  台
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" width="100%" colspan="5">
                    ※テザリングをご契約いただく場合は、1台あたり「500円/月」(税別)が追加となります。
                </td>
              </tr>

              <tr>
                <td align="left" width="100%" colspan="5">
                  <br/>
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" width="100%" colspan="5">
                    ※本システムにて2台以上の同時申込発送先が同一の場合に限ります。
                </td>
              </tr>

              <tr>
                <td align="left" width="100%" colspan="5">
                  <br/>
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" colspan="5">
                  5.ご契約手続き 担当者様情報
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  郵便番号
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="customerZipCode" size="20" value="${sessionScope.customerZipCode}">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  所在地
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="customerAddress" size="50" value="${sessionScope.customerAddress}">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  部署
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="sectionName" size="50" value="${sessionScope.sectionName}">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  担当者様氏名
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="chargeName" size="20" value="${sessionScope.chargeName}">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  ご連絡先電話番号
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="customerTelNo" size="20" value="${sessionScope.customerTelNo}">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  e-mailアドレス
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" disabled="disabled" name="customerMailAddress" size="50" value="${sessionScope.customerMailAddress}">
                </td>
              </tr>

              <tr>
                <td align="left" width="100%" colspan="5">
                  <br/>
                </td>
              </tr>

              <tr>
                <td class="normalFont" align="left" colspan="5">
                  6.仮申込手続き 実施者様情報
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  ご送付先郵便番号
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" name="practicZipCode" size="20" value="<%= practicZipCode %>">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  ご送付先住所
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" name="practicAddress" size="50" value="<%= practicAddress %>">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  実施者様氏名
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" name="practicName" size="20" value="<%= practicName %>">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  ご連絡先電話番号
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" name="practicTelNo" size="20" value="<%= practicTelNo %>">
                </td>
              </tr>

              <tr>
                <td align="left" width="5%">
                  <br/>
                </td>
                <td class="normalFont" align="left" width="25%">
                  e-mailアドレス
                </td>
                <td align="left" width="70%" colspan="3">
                  <input type="text" name="practicMailAddress" size="50" value="<%= practicMailAddress %>">
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
