<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Client</title>

<SCRIPT TYPE="text/javascript">

</SCRIPT>
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
<b><big>Welcome McSense Client</big></b>
<br>
<br>
<i>Describe the task and hit submit.</i>
<br>
<form name="input" action="ClientServlet" method="post">
Enter McSense ID:
<br>
<input name="id" type="text" size="15" value="">
<br>
<br>
Task Name:
<br>
<input name="name" type="text" size="30" MAXLENGTH=30 value="">
<br>
<br>
Task Type:
<br>
<select name="taskType">
<option value="campusSensing">Campus Sensing Task</option>
<option value="photo">Photo Task</option>
<option value="parking">Parking Sensing Task</option>
</select>
<br>
<br>
Pay:
<br>
$<input name="pay" size="5" MAXLENGTH=3 onkeyup="this.value=this.value.replace(/[^\d]/,'')">
<br>
<br>
Duration:
<br>
<input name="duration" size="5" MAXLENGTH=3 onkeyup="this.value=this.value.replace(/[^\d]/,'')"> Minutes
<br>
<br>
Sensors:<br>
<input type="checkbox" name="accelerometer" value="1"> Accelerometer<br>
<input type="checkbox" name="gps" value="1" > GPS<br>
<input type="checkbox" name="camera" value="1"> Camera<br>
<input type="checkbox" name="mic" value="1"> Mic<br>
<input type="checkbox" name="wifi" value="1" > WiFi<br>
<input type="checkbox" name="bluetooth" value="1"> Bluetooth<br>
<input type="checkbox" name="magnetometer" value="1"> Magnetometer<br>
<input type="checkbox" name="proximity" value="1" > Proximity Sensor<br>
<input type="checkbox" name="ambient" value="1"> Ambient Light Sensor<br>
<br>
High Level Gobal Sensing Task: <br>
<textarea name="taskDesc" rows="10" cols="30"></textarea><br />
<br>
<input type="submit" value="Submit" />
</form>
</body>
</html>