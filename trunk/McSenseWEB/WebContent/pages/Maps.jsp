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
	var taskNum = document.forms[0].taskId.value;
	var image = document.getElementById("image_i_want_to_change");  
	image.src = "../files/"+taskNum+".jpg"; // change the image source so a different image will be displayed
	image.style.display = "";
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
        viewTaskMap();
      }
      
	  function viewTaskMap() {
		//var taskNum = document.forms[0].taskId.value;
		//window.location.replace("/Maps.jsp?taskNum="+taskNum);
		//window.location.reload();
	  	var i = 0;
	  	var bound = new google.maps.LatLngBounds();
	  	<%
			ArrayList<LocationInfo> locList = new ArrayList<LocationInfo>();
			String taskID = "";//"597";
			//String taskID ="<script>document.writeln(taskNum)</script>";
			if(session.getAttribute("taskId")!=null){
				taskID = session.getAttribute("taskId").toString(); 
				String path = session.getAttribute("path").toString();
				locList = CampusParser.parseCampusLog(path,taskID+".txt");
			}
			for(LocationInfo l : locList){
				double lat = Double.parseDouble(l.getLatitude());
				double lng = Double.parseDouble(l.getLongitude());
				String speed = l.getSpeed();
		%>
				var loc = new google.maps.LatLng(<%= lat%>,<%= lng%>);

				var marker = new google.maps.Marker({
			       position: loc,
			       map: map
			    });
			    marker.setTitle(""+i++);
			    attachSpeedInfo(marker,<%= speed%>);
			    //map.setCenter(loc);
	  			//map.setZoom(16);
	  			bound.extend(loc);
		<%
			}
		%>
		if(i == 0){
			var njit = new google.maps.LatLng(40.744038,-74.180181);
			var marker = new google.maps.Marker({
		       position: njit,
		       map: map
		    });
		    marker.setTitle("NJIT");
		    bound.extend(njit);
		  	map.setCenter(njit);
		  	map.setZoom(16);
		}
		map.fitBounds(bound);
		//}
	  }
	  
	// The markers show speed info when clicked
	// but that message is not within the marker's instance data.
	function attachSpeedInfo(marker, speed) {
	  var message = speed+" mph";
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
<br><br>
Map plotted for Task ID: <%= taskID%>
<br><br>
<div id="map_canvas" style="width:50%; height:50%"></div>
<br>
<P><A HREF=/McSenseWEB/pages/Task.jsp>back to Task Screen</A>
</body>
</html>