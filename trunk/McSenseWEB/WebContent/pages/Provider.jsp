<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Provider</title>
</head>
<body>
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
</body>
</html>