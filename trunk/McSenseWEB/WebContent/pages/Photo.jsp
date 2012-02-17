<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/googlemaps.tld" prefix="googlemaps" %>    
<!DOCTYPE html>

<%@page import="com.mcsense.parser.CampusParser"%>
<%@page import="com.mcsense.parser.LocationInfo"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map_canvas { height: 100% }
</style>
<title>Insert title here</title>
<SCRIPT TYPE="text/javascript">
function viewPhoto() {
		<%		
			String taskID = "";//"597";
			if(session.getAttribute("taskId")!=null){
				taskID = session.getAttribute("taskId").toString(); 
			}
		%>
	var taskNum = <%= taskID%>;
	var image = document.getElementById("image_i_want_to_change");
	
	//
	//String fileLoc = WebConstants.DESTINATION_DIR_PATH;
	//  
	image.src = "http://mcsense.njit.edu:10080/files/"+taskNum+".jpg"; // change the image source so a different image will be displayed
	//var loc =  fileLoc;
	//image.src = "../"+loc+"\\"+taskNum+".jpg";
	image.style.display = "";
	//document.forms[0].expiration.value = currentTime; 
}
</SCRIPT>
<script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDjZPZFiASvf0OEQ3cvzTSUqKkrR3ljRfY&sensor=false">
</script>
<script type="text/javascript">
      var map;
      function initialize() {
      
        var myOptions = {
          center: new google.maps.LatLng(40.744038,-74.180181),
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById("map_canvas"),
            myOptions);
        viewPhoto();
        viewTaskMap();
      }
      
	  function viewTaskMap() {
		<%
		String time = "";//"597";
		double lat = 0;
		double lng = 0;
		
		if(session.getAttribute("time")!=null){
			time = session.getAttribute("time").toString();
			String temp1 = session.getAttribute("lat").toString();
			String temp2 = session.getAttribute("lng").toString();
			lat = Double.parseDouble(temp1);
			lng = Double.parseDouble(temp2);
		}
		%>
		var pLoc = new google.maps.LatLng(<%= lat%>,<%= lng%>);
		var marker = new google.maps.Marker({
	       position: pLoc,
	       map: map
	    });
	    marker.setTitle("PHOTO");
	    attachTimeInfo(marker,"<%= time%>");
	  	map.setCenter(pLoc);
	  	map.setZoom(16);
	  }
	  
	// The markers show speed info when clicked
	// but that message is not within the marker's instance data.
	function attachTimeInfo(marker, time) {
	  var message = "Photo Taken at: \n"+time;
	  var infowindow = new google.maps.InfoWindow(
	      { content: message,
	        size: new google.maps.Size(50,50)
	      });
	  google.maps.event.addListener(marker, 'click', function() {
	    infowindow.open(map,marker);
	  });
	}
</script>
</head>
<body onload="initialize()">
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
<b><big>McSense Map Services</big></b>
<br>
Photo for Task ID: <%= taskID%>
<br>
<img name="image_i_want_to_change" id="image_i_want_to_change"   
src="dont_really_care_since_were_not_using_it_anyway" style="display: none" width="500" height="500">
<br>
Photo location and time:
<br>
<div id="map_canvas" style="width:50%; height:50%"></div>
<br>
<P><A HREF=/McSenseWEB/pages/Task.jsp>back to Task Screen</A>
</body>
</html>