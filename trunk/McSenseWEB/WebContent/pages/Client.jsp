<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Client</title>

<SCRIPT TYPE="text/javascript">
function currencyFormat(fld, milSep, decSep, e) {
  var sep = 0;
  var key = '';
  var i = j = 0;
  var len = len2 = 0;
  var strCheck = '0123456789';
  var aux = aux2 = '';
  var whichCode = (window.Event) ? e.which : e.keyCode;

  if (whichCode == 13) return true;  // Enter
  if (whichCode == 8) return true;  // Delete
  key = String.fromCharCode(whichCode);  // Get key value from key code
  if (strCheck.indexOf(key) == -1) return false;  // Not a valid key
  len = fld.value.length;
  for(i = 0; i < len; i++)
  if ((fld.value.charAt(i) != '0') && (fld.value.charAt(i) != decSep)) break;
  aux = '';
  for(; i < len; i++)
  if (strCheck.indexOf(fld.value.charAt(i))!=-1) aux += fld.value.charAt(i);
  aux += key;
  len = aux.length;
  if (len == 0) fld.value = '';
  if (len == 1) fld.value = '0'+ decSep + '0' + aux;
  if (len == 2) fld.value = '0'+ decSep + aux;
  if (len > 2) {
    aux2 = '';
    for (j = 0, i = len - 3; i >= 0; i--) {
      if (j == 3) {
        aux2 += milSep;
        j = 0;
      }
      aux2 += aux.charAt(i);
      j++;
    }
    fld.value = '';
    len2 = aux2.length;
    for (i = len2 - 1; i >= 0; i--)
    fld.value += aux2.charAt(i);
    fld.value += decSep + aux.substr(len - 2, len);
  }
  return false;
}

</SCRIPT>
</head>
<body>
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
$<input name="pay" size="15" MAXLENGTH=5 onKeyPress="return currencyFormat(this,',','.',event)">
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