<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Provider</title>
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
<b><big>Welcome McSense Providers</big></b>
<br>
<br>
<i>Click on below button to pick one sensing task.</i>
<br>
<form name="input" action="ProviderServlet" method="get">
<p align="left">Enter McSense ID:
<br>
<input name="id" type="text" size="15" value="">
<br>
<br>
<input type="submit" value="Pick" />
</form>
<br>
<i>Complete Photo sensing task.</i>
<br>
<form name="completetask" action="ProviderServlet" enctype="multipart/form-data" method="post">
<p align="left">Task ID:
<br>
<input name="taskid" type="text" size="15" value="">
<br>
<p align="left">Choose Photo:
<br>
<input type="file" name="file1">
<br>
<input type="hidden" name="htmlFormName" value="completetask">
<br>
<input type="submit" value="Complete Photo Sensing Task" />
</form>
</body>
</html>