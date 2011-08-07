<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
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