package com.mcsense.app;

import java.util.ArrayList;

import com.mcsense.json.JTask;

public class AppConstants {
//	protected static final String ip = "128.235.66.235";
//	protected static final String ip = "http://10.1.169.151:10080";
	protected static final String ip = "http://manoop.dyndns.org:10080";
//	protected static final String ip = "http://mcsense.njit.edu:10080";
	
	protected static final String PREFS_NAME = "myPref";
	// Set the timeout in milliseconds until a connection is established.
	protected static final int timeoutConnection = 2000;
	// Set the default socket timeout (SO_TIMEOUT) 
	// in milliseconds which is the timeout for waiting for data.
	protected static final int timeoutSocket = 5000;
	
	protected static final String SEED = "mcsense";
//	static final Task[] TASKS = {new Task(1,"Photo Task"),new Task(2,"GPS Task"),new Task(3,"Accelerometer Task"),new Task(4,"Magnetometer Task"),new Task(5,"Mic Task")};
	
	protected static String providerId = "";
	
	public static JTask selectedTask = null;
	
	protected static ArrayList<JTask> jTaskCompletedList = null;
	
	public static boolean gpsLocUpdated = false;
	
	protected static String status = "";
	
	protected static ArrayList failedTaskList = new ArrayList();
	
	static final String TAG = "McSense";
	
	public static final int SENSING_THRESHOLD = 6;
//	public static ArrayList<Task> getTaskList(){
//		ArrayList<Task> taskList = new ArrayList<Task>();
//		Task t;
//		Task t1 = new Task(1,"Photo Task");
//		taskList.add(t1);
//		Task t2 = new Task(2,"GPS Task");
//		taskList.add(t2);
//		Task t3 = new Task(3,"Accelerometer Task");
//		taskList.add(t3);
//		Task t4 = new Task(4,"Magnetometer Task");
//		taskList.add(t4);
//		Task t5 = new Task(5,"Mic Task");
//		taskList.add(t5);
//		
//		return taskList;
//	}
	
	static final String[] COUNTRIES = new String[] {
	    "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
	    "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
	    "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
	    "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
	    "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
	    "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
	    "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
	    "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde",
	    "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
	    "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
	    "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
	    "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
	    "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
	    "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
	    "Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia",
	    "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar",
	    "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
	    "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
	    "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
	    "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
	    "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
	    "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
	    "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
	    "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
	    "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
	    "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
	    "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
	    "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
	    "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena",
	    "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
	    "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal",
	    "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
	    "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea",
	    "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
	    "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas",
	    "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
	    "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
	    "Ukraine", "United Arab Emirates", "United Kingdom",
	    "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
	    "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara",
	    "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
	  };

	public static final String imageFileName = "McSenseImage.jpg";

	public static final String UPLOAD_PENDING = "upload_pending";

}
