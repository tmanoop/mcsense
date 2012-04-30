<html>
<head>
<META HTTP-EQUIV="REFRESH" CONTENT="10">
<title>Test Page</title>
<script type="text/javascript">
        // Get rid of the Facebook residue hash in the URI
        // Must be done in JS cuz hash only exists client-side
        // IE and Chrome version of the hack
        if (String(window.location.hash).substring(0,1) == "#") {
                window.location.hash = "";
                window.location.href=window.location.href.slice(0, -1);
                }
        // Firefox version of the hack
        if (String(location.hash).substring(0,1) == "#") {
                location.hash = "";
                location.href=location.href.substring(0,location.href.length-3);
                }
</script>
<script>
function load_ucid(ucid){
	  self.location='login.jsp?ucid='+ucid;
}
</script>
<script>
  var statusList = new Array();
  var statusListSorted = new Array();
  var profile = new Array();
  var friendCount = 0;
  var maxResults = 100;

  
	
	function upload_data() {
		document.getElementById('login').innerHTML = "";
		get_public_info();
	}

	function get_public_info() {
		//alert('publicinfo');
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me', function(response) {
			profile = profile.concat("*****PublicInfo*****");
			profile = profile.concat(response);
			//alert('public:'+response.data);
				get_likes_info();
			});
	}

	function get_likes_info() {
		//alert('publicinfo');
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/likes', function(response) {
			profile = profile.concat("*****UserLikes*****");
			profile = profile.concat(response.data);
			//alert('public:'+response.data);
				get_friend_List();
			});
	}

	function get_friend_List() {
		//alert('friend');
		document.getElementById('test').innerHTML = "Requesting data from Facebook ... ";
		FB.api('/me/friends', function(response) {
			friendCount = response.data.length;
			//alert(friendCount);
			profile = profile.concat("*****FriendsCount*****");
			profile = profile.concat(friendCount);
			profile = profile.concat("*****FriendList*****");
			profile = profile.concat(response.data);
			get_movies_info();
		});
	}

	function get_movies_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/movies', function(response) {
			profile = profile.concat("*****MoviesInfo*****");
			profile = profile.concat(response.data);
			get_music_info();
		});
	}

	function get_music_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me/music', function(response) {
			profile = profile.concat("*****MusicInfo*****");
			profile = profile.concat(response.data);
			get_books_info();
		});
	}

	function get_books_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/books', function(response) {
			profile = profile.concat("*****BooksInfo*****");
			profile = profile.concat(response.data);
			get_notes_info();
		});
	}

	function get_notes_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me/notes', function(response) {
			profile = profile.concat("*****NotesInfo*****");
			profile = profile.concat(response.data);
			get_permissions_info();
		});
	}

	function get_permissions_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/permissions', function(response) {
			profile = profile.concat("*****PermissionsInfo*****");
			profile = profile.concat(response.data);
			get_photos_info();
		});
	}

	function get_photos_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me/albums', function(response) {
			profile = profile.concat("*****PhotosInfo*****");
			profile = profile.concat(response.data);
			get_videos_info();
		});
	}

	function get_videos_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/videos/uploaded', function(response) {
			profile = profile.concat("*****VideosInfo*****");
			profile = profile.concat(response.data);
			get_events_info();
		});
	}

	function get_events_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me/events', function(response) {
			profile = profile.concat("*****EventsInfo*****");
			profile = profile.concat(response.data);
			get_groups_info();
		});
	}

	function get_groups_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/groups', function(response) {
			profile = profile.concat("*****GroupsInfo*****");
			profile = profile.concat(response.data);
			get_checkins_info();
		});
	}

	function get_checkins_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me/checkins', function(response) {
			profile = profile.concat("*****CheckinsInfo*****");
			profile = profile.concat(response.data);
			get_locations_info();
		});
	}

	function get_locations_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook .......... ";
		FB.api('/me/locations', function(response) {
			profile = profile.concat("*****LocationsInfo*****");
			profile = profile.concat(response.data);
			get_wall_info();
		});
	}

	function get_wall_info() {
		document.getElementById('test').innerHTML = "Requesting "
				+ "data from Facebook ... ";
		FB.api('/me/feed', function(response) {
			profile = profile.concat("*****WallInfo*****");
			profile = profile.concat(response.data);
			upload_profile();
		});
	}
	
	function upload_profile() {

		if(document.getElementById('id').value === ""){
	        alert("Please enter UCID and Continue.");
	    } else{
			var params = JSON.stringify(profile);
			document.getElementById('data').value = params;
			document.getElementById('dataType').value = "\n\n*****profile*****\n\n";
			document.forms["fb"].submit();
	    }
	}
</script>
</head>
<body>
<img src="mcsense.png" height="200" width="200" />
<h1 id="heading">McSense Survey</h1>
<BR>

Please enter UCID and click on Agree button to start.
<% 

String ucid = request.getParameter("ucid");
if(ucid==null)
	ucid = "";
%>
<form name="fb" action="../FacebookServlet" method="post" accept-charset="UTF-8"><input
	type="hidden" id="data" name="data" value=""> <input
	type="hidden" id="dataType" name="dataType" value=""> UCID: <input
	type="text" id="id" name="id" value="<%= ucid%>" onblur="load_ucid(this.value);"></form>
	<div id="survey-text">Before we start to survey, we would like to
ask for your permission to collect your Facebook profile for research. <BR>
If your data will be used in any research, it will be properly
anonymized to protect your identity.</div>
<BR>
<div id="fb-root"></div>
<table >
<tr>
<td valign="middle"><div id="login"></div></td>
<td valign="top"><div id="survey"></div></td>
</tr>
</table>


<div id="test"></div>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script> 

FB.init({
    appId  : '316312168439370',
    channelUrl : 'http://mcsense.njit.edu:10080/McSenseWEB/facebook/channel.html',
    status : true, // check login status
    cookie : true, // enable cookies 
    xfbml  : true, // parse XFBML
    oauth  : true
  });
FB.Event.subscribe('auth.authResponseChange', function(response) {
	window.location.reload();
});

FB.getLoginStatus(function(response) {
  if (response.status == "connected") {
    // logged in and connected user, someone you know
    if(document.getElementById('id').value === ""){
    	document.getElementById('survey').innerHTML ='';
    	document.getElementById('login').innerHTML
        ='<a href="#" onclick="upload_data();">Start Survey</a><br/>';
        alert("Please enter UCID and Continue.");
    }
    else{
    	//alert('in');
    	upload_data();
    }
    
  } else {
		  //alert('not');
		  document.getElementById('survey').innerHTML ='<a href="#" onclick="upload_profile();">Do not Agree</a><br/>';

		  document.getElementById('login').innerHTML
	      ='<fb:login-button  show-faces="true" width="300" ' 
	      + ' max-rows="1" perms="user_about_me, user_activities, user_birthday, user_checkins, user_education_history, user_events, user_groups, user_hometown, user_interests, user_likes, user_notes, user_photos, user_relationships, user_status, user_videos, email">'
	      + 'Agree   </fb:login-button>';
	      //+ '<br/> <a href="#" onclick="upload_profile();">Do not Agree</a><br/>';
	    FB.XFBML.parse();
	  
    
  }
});


</script>
</body>
</html>
