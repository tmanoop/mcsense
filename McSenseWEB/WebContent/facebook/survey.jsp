<!DOCTYPE html> 
<html xmlns:fb="https://www.facebook.com/2008/fbml">
  <head> 
    <title> 
      McSense Survey
    </title> 
  </head> 
<body>
<script>
function validateForm(theForm) {
	var listSelected1 = false;
	var listSelected2 = false;
	var listSelected3 = false;
	var listSelected4 = false;
	var listSelected5 = false;
	var listSelected6 = false;
	var listSelected7 = false;
	var listSelected8 = false;
	var listSelected9 = false;
	var listSelected10 = false;
	var listSelected11 = false;
	var listSelected12 = false;
	var listSelected13 = false;
	var listSelected14 = false;
	var listSelected15 = false;
	var listSelected16 = false;
	
	
	for ( var i = 0; i < theForm.rad1.length; i++ ) {
		if ( theForm.rad1[i].checked ) {
			listSelected1 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad2.length; i++ ) {
		if ( theForm.rad2[i].checked ) {
			listSelected2 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad3.length; i++ ) {
		if ( theForm.rad3[i].checked ) {
			listSelected3 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad4.length; i++ ) {
		if ( theForm.rad4[i].checked ) {
			listSelected4 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad5.length; i++ ) {
		if ( theForm.rad5[i].checked ) {
			listSelected5 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad6.length; i++ ) {
		if ( theForm.rad6[i].checked ) {
			listSelected6 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad7.length; i++ ) {
		if ( theForm.rad7[i].checked ) {
			listSelected7 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad8.length; i++ ) {
		if ( theForm.rad8[i].checked ) {
			listSelected8 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad9.length; i++ ) {
		if ( theForm.rad9[i].checked ) {
			listSelected9 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad10.length; i++ ) {
		if ( theForm.rad10[i].checked ) {
			listSelected10 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad11.length; i++ ) {
		if ( theForm.rad11[i].checked ) {
			listSelected11 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad12.length; i++ ) {
		if ( theForm.rad12[i].checked ) {
			listSelected12 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad13.length; i++ ) {
		if ( theForm.rad13[i].checked ) {
			listSelected13 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad14.length; i++ ) {
		if ( theForm.rad14[i].checked ) {
			listSelected14 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad15.length; i++ ) {
		if ( theForm.rad15[i].checked ) {
			listSelected15 = true;
			break;
		}
	}
	for ( var i = 0; i < theForm.rad16.length; i++ ) {
		if ( theForm.rad16[i].checked ) {
			listSelected16 = true;
			break;
		}
	}
	
	if (!listSelected1 || !listSelected2 || !listSelected3 || !listSelected4 || !listSelected5 || !listSelected6 || !listSelected7 || !listSelected8 || !listSelected9 || !listSelected10 || !listSelected11 || !listSelected12 || !listSelected13 || !listSelected14 || !listSelected15 || !listSelected16){
		alert('Please answer all questions.');
		return false;
	}else
		return true;
}
</script>
<h2>Please complete below survey:</h2>
<BR>
<form name="input" action="../FacebookServlet" method="post" accept-charset="UTF-8" onsubmit="return validateForm(this)">
<input	type="hidden" id="id" name="id" value="<%= request.getParameter("id")%>">
1. I participated in this study to earn money
<BR>
<BR>
<input type="radio" name="rad1" value="0" >strongly disagree
<input type="radio" name="rad1" value="1" >disagree
<input type="radio" name="rad1" value="2" >neutral
<input type="radio" name="rad1" value="3" >agree
<input type="radio" name="rad1" value="4" >strongly agree
<BR>
<BR>
2. I participated in this study to learn more about mobile
computing/people-centric sensing
<BR>
<BR>
<input type="radio" name="rad2" value="0" >strongly disagree
<input type="radio" name="rad2" value="1" >disagree
<input type="radio" name="rad2" value="2" >neutral
<input type="radio" name="rad2" value="3" >agree
<input type="radio" name="rad2" value="4" >strongly agree
<BR>
<BR>
3. The phone was with me most of the time during the study
<BR>
<BR>
<input type="radio" name="rad3" value="0" >strongly disagree
<input type="radio" name="rad3" value="1" >disagree
<input type="radio" name="rad3" value="2" >neutral
<input type="radio" name="rad3" value="3" >agree
<input type="radio" name="rad3" value="4" >strongly agree
<BR>
<BR>
4. The price was right (the payment for the offered tasks reflected the
amount of work/time/resources necessary to complete them)
<BR>
<BR>
<input type="radio" name="rad4" value="0" >strongly disagree
<input type="radio" name="rad4" value="1" >disagree
<input type="radio" name="rad4" value="2" >neutral
<input type="radio" name="rad4" value="3" >agree
<input type="radio" name="rad4" value="4" >strongly agree
<BR>
<BR>
5. Automatic tasks represent a good deal because they do not require my
direct input
<BR>
<BR>
<input type="radio" name="rad5" value="0" >strongly disagree
<input type="radio" name="rad5" value="1" >disagree
<input type="radio" name="rad5" value="2" >neutral
<input type="radio" name="rad5" value="3" >agree
<input type="radio" name="rad5" value="4" >strongly agree
<BR>
<BR>
6. Manual tasks (photo tasks) represent a good deal because they allow for
higher earnings
<BR>
<BR>
<input type="radio" name="rad6" value="0" >strongly disagree
<input type="radio" name="rad6" value="1" >disagree
<input type="radio" name="rad6" value="2" >neutral
<input type="radio" name="rad6" value="3" >agree
<input type="radio" name="rad6" value="4" >strongly agree
<BR>
<BR>
7. I tried to fool the system by providing photos from other locations
than those specified in the tasks (the answer does not influence the
payment)
<BR>
<BR>
<input type="radio" name="rad7" value="0" >strongly disagree
<input type="radio" name="rad7" value="1" >disagree
<input type="radio" name="rad7" value="2" >neutral
<input type="radio" name="rad7" value="3" >agree
<input type="radio" name="rad7" value="4" >strongly agree
<BR>
<BR>
8. Executing these tasks did not consume too much battery power (I did not
need to re-charge the phone more often than once a day)
<BR>
<BR>
<input type="radio" name="rad8" value="0" >strongly disagree
<input type="radio" name="rad8" value="1" >disagree
<input type="radio" name="rad8" value="2" >neutral
<input type="radio" name="rad8" value="3" >agree
<input type="radio" name="rad8" value="4" >strongly agree
<BR>
<BR>
9. I stopped the automatic tasks (resulting in incomplete tasks) when my
battery was low
<BR>
<BR>
<input type="radio" name="rad9" value="0" >strongly disagree
<input type="radio" name="rad9" value="1" >disagree
<input type="radio" name="rad9" value="2" >neutral
<input type="radio" name="rad9" value="3" >agree
<input type="radio" name="rad9" value="4" >strongly agree
<BR>
<BR>
10. The automatic task executions did not slow down my phone
<BR>
<BR>
<input type="radio" name="rad10" value="0" >strongly disagree
<input type="radio" name="rad10" value="1" >disagree
<input type="radio" name="rad10" value="2" >neutral
<input type="radio" name="rad10" value="3" >agree
<input type="radio" name="rad10" value="4" >strongly agree
<BR>
<BR>
11. I was concerned about my privacy while participating in the
user study
<BR>
<BR>
<input type="radio" name="rad11" value="0" >strongly disagree
<input type="radio" name="rad11" value="1" >disagree
<input type="radio" name="rad11" value="2" >neutral
<input type="radio" name="rad11" value="3" >agree
<input type="radio" name="rad11" value="4" >strongly agree
<BR>
<BR>
12. I would like to be able to specify when and where the automatic tasks
may collect data
<BR>
<BR>
<input type="radio" name="rad12" value="0" >strongly disagree
<input type="radio" name="rad12" value="1" >disagree
<input type="radio" name="rad12" value="2" >neutral
<input type="radio" name="rad12" value="3" >agree
<input type="radio" name="rad12" value="4" >strongly agree
<BR>
<BR>
13. I would like to receive recommendations about new mobile phone apps
and services, better vehicular traffic routes, or healthier lifestyle
based on the analysis of the data collected from my phone
<BR>
<BR>
<input type="radio" name="rad13" value="0" >strongly disagree
<input type="radio" name="rad13" value="1" >disagree
<input type="radio" name="rad13" value="2" >neutral
<input type="radio" name="rad13" value="3" >agree
<input type="radio" name="rad13" value="4" >strongly agree
<BR>
<BR>
14. I would provide some sensing data for free in exchange for
recommendations such as those mentioned above
<BR>
<BR>
<input type="radio" name="rad14" value="0" >strongly disagree
<input type="radio" name="rad14" value="1" >disagree
<input type="radio" name="rad14" value="2" >neutral
<input type="radio" name="rad14" value="3" >agree
<input type="radio" name="rad14" value="4" >strongly agree
<BR>
<BR>
15. I would be willing to share my Facebook profile data (for free) to
help the system provide higher quality recommendations
<BR>
<BR>
<input type="radio" name="rad15" value="0" >strongly disagree
<input type="radio" name="rad15" value="1" >disagree
<input type="radio" name="rad15" value="2" >neutral
<input type="radio" name="rad15" value="3" >agree
<input type="radio" name="rad15" value="4" >strongly agree
<BR>
<BR>
16. I would be willing to execute sensing tasks offered by other
organizations (commercial or non-commercial) as long as they offer
privacy/anonymity guarantees.
<BR>
<BR>
<input type="radio" name="rad16" value="0" >strongly disagree
<input type="radio" name="rad16" value="1" >disagree
<input type="radio" name="rad16" value="2" >neutral
<input type="radio" name="rad16" value="3" >agree
<input type="radio" name="rad16" value="4" >strongly agree
<BR>
<BR>
<input type="submit" name="btnSubmit" value="Submit">
</form>
</body> 
</html>