<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Client</title>
</head>
<body>
<b><big>Welcome McSense Client</big></b>
<br>
<br>
<i>Describe the task and hit submit.</i>
<br>
<form name="input" action="ClientServlet" method="post">
High Level Gobal Sensing Task: <br>
<br>
<textarea name="taskDesc" rows="10" cols="30"></textarea><br />
<br>
<input type="submit" value="Submit" />
</form>
</body>
</html>