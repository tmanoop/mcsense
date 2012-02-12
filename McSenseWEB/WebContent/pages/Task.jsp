<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.mcsense.util.WebConstants"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<SCRIPT TYPE="text/javascript" >
function viewPhoto() {
	var taskNum = document.forms[0].taskId.value;
	var image = document.getElementById("image_i_want_to_change");
	//
	//String fileLoc = WebConstants.DESTINATION_DIR_PATH;
	//  
	image.src = "http://localhost:10080/files/"+taskNum+".jpg"; // change the image source so a different image will be displayed
	//var loc =  fileLoc;
	//image.src = "../"+loc+"\\"+taskNum+".jpg";
	image.style.display = "";
	//document.forms[0].expiration.value = currentTime; 
}
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
<tr><td align="center"><h1>Welcome <b><%= user%></b></h1><a href="../login.jsp">Logout</a></td></tr>
<%
} else {
	response.sendRedirect("../login.jsp");
}
%>
</table>
<b><big>McSense Task Services</big></b>
<br>
<br>
<i>Enter Task ID and view photo:</i>
<br>
<img name="image_i_want_to_change" id="image_i_want_to_change"   
src="dont_really_care_since_were_not_using_it_anyway" style="display: none" width="500" height="500">  
<form name="task" action="TaskServlet" method="post">
<input type="submit" name="submit" value="View Photo" ><p></p>
<input type="submit" name="submit" value="View Task Map"><p></p>
<p align="left">Task ID:
<input name="taskId" type="text" size="15" value="">
<br>
Task Status:
<select name="status">
<option value="C">Complete</option>
<option value="E">Error</option>
</select>
<br>
<input type="hidden" name="htmlFormName" value="updateTask">
<input type="submit" name="submit" value="Update Task"></p>
</form>

</body>
</html>