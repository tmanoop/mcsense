<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<b><big>Welcome to McSense Reputation Service</big></b>
<br>
<br>
<i>Search for any user details.</i>
<br>
<form name="reputation" action="BankServlet" method="post">
Last Name:
<input name="lname" type="text" size="20" value="">
Person ID:
<input name="id" type="text" size="10" value="">
<br>
<br>
<input type="hidden" name="htmlFormName" value="reputation">
<input type="submit" value="Search" />
</form>
</body>
</html>