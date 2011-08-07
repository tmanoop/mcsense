<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<b><big>McSense Admin Services</big></b>
<br>
<br>
<i>Create a McSense Account</i>
<br>
<form name="search" action="AdminServlet" method="post">
<p align="left">Last Name:
<input name="lname" type="text" size="15" value="">
<br>
First Name:
<input name="fname" type="text" size="15" value="">
<br>
Bank Account ID:
<input name="bankid" type="text" size="10" value="">
<br>
Address:
<input name="address" type="text" size="30" value="">
<br>
<br>
<input type="hidden" name="htmlFormName" value="register">
<input type="submit" value="Register"></p>
</form>
<br>
<i>Delete any user</i>
<br>
<form name="delete" action="AdminServlet" method="post">
Person ID:
<input name="id" type="text" size="10" value="">

<input type="hidden" name="htmlFormName" value="delete">
<input type="submit" value="Delete" />
</form>
<br>
<i>Deposit in bank.</i>
<br>
<form name="deposit" action="AdminServlet" method="post">
TaskID:
<input name="taskId" type="text" size="10" value="">
Amount: $
<input name="amount" type="text" size="10" value="">
<br>
<br>
<input type="hidden" name="htmlFormName" value="deposit">
<input type="submit" value="Deposit" />
</form>
</body>
</html>