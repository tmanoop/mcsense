<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<br><br><br><br>
<table align="center" style="border:1px solid #000000;">
<%
if(session.getAttribute("emailID")!=null && session.getAttribute("emailID")!="")
{
String user = session.getAttribute("emailID").toString();
%>
<tr><td align="center"><h1>Welcome <b><%= user%></b></h1></td></tr>
<%
} else {
	response.sendRedirect("login.jsp");
}
%>
</table>
<b><big>McSense Task Services</big></b>
<br>
<br>
<i>Create a McSense Task</i>
<br>
<form name="task" action="TaskServlet" method="post">
<p align="left">McSense ID:
<input name="id" type="text" size="15" value="">
<br>
Task Description:
<br>
<textarea name="taskDesc" rows="10" cols="30"></textarea><br />
<br>
<input type="hidden" name="htmlFormName" value="task">
<input type="submit" value="Create Task"></p>
</form>
</body>
</html>