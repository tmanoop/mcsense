<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>McSense Utility</title>
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
	response.sendRedirect("../login.jsp");
}
%>
</table>
<BR>
McSense Utility Screens
<BR>
<P><A HREF=/McSenseWEB/pages/Client.jsp>Client Screen</A>
<P><A HREF=/McSenseWEB/pages/Provider.jsp>Provider Screen</A>
<P><A HREF=/McSenseWEB/pages/Bank.jsp>Bank Screen</A>
<P><A HREF=/McSenseWEB/pages/Reputation.jsp>Reputation Screen</A>
<P><A HREF=/McSenseWEB/pages/Admin.jsp>Admin Screen</A>
<P><A HREF=/McSenseWEB/pages/Task.jsp>Task Screen</A>
<P><A HREF=/McSenseWEB/pages/TaskLookup.jsp>Task Lookup Screen</A>
<P><A HREF=/McSenseWEB/files>Uploaded Files Screen</A>
</body>
</html>