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
<tr><td align="center"><h1>Welcome <b><%= user%></b></h1><a href="login.jsp">Logout</a></td></tr>
<%
} else {
	response.sendRedirect("login.jsp");
}
%>
</table>
<b><big>Welcome to McSense Task Services</big></b>
<br>
<br>
<i>Search for Task details.</i>
<br>
<form name="tasklookup" action="TaskServlet" method="get">
Task Status:
<select name="status">
  <option value="P">Pending</option>
  <option value="IP">In Progress</option>
  <option value="C">Complete</option>
  <option value="ALL">ALL</option>
</select>
ID:
<input name="id" type="text" size="10" value="">
<br>
<br>
<input type="hidden" name="htmlFormName" value="tasklookup">
<input type="submit" value="Task Lookup" />
</form>
</body>
</html>