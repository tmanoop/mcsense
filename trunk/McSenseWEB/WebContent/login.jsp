<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login into McSense</title>
</head>
<body>
<%
session.removeAttribute("emailID");
%>
<form name="loginform" method="post" action="LoginServlet">
<br><br>
<table align="center"><tr><td><h2>McSense Client Login</h2></td></tr></table>
<table width="300px" align="center" style="border:1px solid #000000;background-color:#efefef;">
<tr><td colspan=2></td></tr>
<tr><td colspan=2> </td></tr>
  <tr>
  <td><b>Email ID</b></td>
  <td><input type="text" name="emailID" value=""></td>
  </tr>
  <tr>
  <td><b>Password</b></td>
  <td><input type="password" name="password" value=""></td>
  </tr>
  <tr>
  <td></td>
  <td><input type="submit" name="Submit" value="Submit"></td>
  </tr>
  <tr><td colspan=2> </td></tr>
  <%
if(session.getAttribute("error")!=null && session.getAttribute("error")!="")
{
String error = session.getAttribute("error").toString();
session.setAttribute("error","");
%>
<tr><td align="center"><i><%= error%></i></td></tr>
<%
}
%>
</table>
<input type="hidden" name="htmlFormName" value="login">
</form>
</body>
</html>