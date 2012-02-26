package com.mcsense.app.pro;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.mcsense.app.R;
import com.mcsense.app.pro.MyLocation.LocationResult;
import com.mcsense.app.pro.Sensors.MyInnerLocationListener;
import com.mcsense.json.JTask;

public class SensingService extends Service {

	private static SensorManager mSensorManager;
	private static Sensor mAccelerometer;
	private static AcelSensor acelSensorListener;
	private static MyLocation myLocation;
	private static LocationManager mlocManager;
    private static LocationListener mlocListener;
	private JTask currentTask;
	private static int taskId;
	private static long serverTime;
	Context appContext;
	int sensingDuration;
	private static final int NOTIFICATION_EX = 1;
	private NotificationManager notificationManager;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		appContext = this.getApplicationContext();
		startAccelerometerSensing();
//		myLocation = new MyLocation();
//		startGPSSensing();
	}

	@Override
	public void onDestroy() {
		Log.d("McSense", "stopping srvice");
		showToast("sensing service stopped");
		if(mSensorManager!=null)
			mSensorManager.unregisterListener(acelSensorListener);
//        mlocManager.removeUpdates(mlocListener);
		if(notificationManager!=null)
			notificationManager.cancel(NOTIFICATION_EX);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Log.d("McSense", "starting srvice");
		showToast("sensing service started");
		
		boolean startSensing = true;
		
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		Calendar cal=Calendar.getInstance();
		boolean resumingTask = false;
		//get task details
		if(intent!=null && intent.hasExtra("JTask")){
			JTask jTaskObjInToClass = intent.getExtras().getParcelable("JTask");
			currentTask = jTaskObjInToClass;
			sensingDuration = jTaskObjInToClass.getTaskDuration();
			taskId = currentTask.getTaskId();
		} else{
			//load taskId
			String taskIDString = settings.getString("taskID", "");
			String status = settings.getString("status", "");
			sensingDuration = Integer.parseInt(settings.getString("duration", "0"));
//			int sensingTaskID = 0;
			if(!taskIDString.equals("")){
				taskId = Integer.parseInt(taskIDString);
				if(status.equals("C"))
					stopService(new Intent(SensingService.this, SensingService.class));
				resumingTask = true;
			}
		}
		
		//get server time
		serverTime = AppUtils.getServerTime(taskId);
		Log.d(AppConstants.TAG, "Server time: "+serverTime);
		Date serverDate = new Date(serverTime);
		cal.setTime(serverDate);
		
		//get prev start time
		String prevStartTime = settings.getString("startTimeMillisecs", "");
		Date prevStartDate = serverDate;
		if(resumingTask && !prevStartTime.equals(""))
			prevStartDate = new Date(Long.parseLong(prevStartTime.trim()));
		Calendar prevCal=Calendar.getInstance();
		prevCal.setTime(prevStartDate);
		
		calculateElapsedMins();
		
		//call server to get time, if not 10pm continue sensing.
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		Log.d(AppConstants.TAG, "Server time hour: "+hour);
		//if server time greater than 10pm, stop sensing
//		int SENSING_STOP_TIME = 22; //TODO replace 10pm code if necessary
		int SENSING_STOP_TIME = currentTask.getTaskExpirationTime().getHours();
		if(cal.get(Calendar.DAY_OF_MONTH) == prevCal.get(Calendar.DAY_OF_MONTH) 
				&& hour<=SENSING_STOP_TIME){
			startSensing = true;
			int totalMins = (SENSING_STOP_TIME - hour) * 60;
			totalMins = totalMins + currentTask.getTaskExpirationTime().getMinutes();
			int hourMins = cal.get(Calendar.MINUTE);
			sensingDuration = totalMins - hourMins;
			Log.d(AppConstants.TAG,"sensingDuration: "+sensingDuration);
		} else {
//			stop sensing
			startSensing = false;
		}	
		
		if(startSensing){
//			startGPSSensing();
			startSensing();
//			loadNotificationIcon();
			Thread t = new Thread(new Runnable() {
		    	 public void run() {
							//check time and stop service
							// Get current time
							 long start = System.currentTimeMillis();
							 logSensingStartTime(serverTime, taskId, "IP");
							// Get elapsed time in milliseconds
							 long elapsedTimeMillis = System.currentTimeMillis()-start;
							// Get elapsed time in minutes
							 float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
							 float nextTrigger = 1;
							 logSensingElapsedTime(elapsedTimeMillis, taskId, "IP");
							 while(elapsedTimeMin < sensingDuration){
								 elapsedTimeMillis = System.currentTimeMillis()-start;
								 
								 elapsedTimeMin = elapsedTimeMillis/(60*1000F);
								 //trigger sensing at every min
								 if(elapsedTimeMin - nextTrigger >= 0){
									 Log.d(AppConstants.TAG, "Elapsed time: "+elapsedTimeMin);
									 if(!AppUtils.isServiceRunning(getApplicationContext()))
										 break;
									 logSensingElapsedTime(elapsedTimeMillis, taskId, "IP");
									 startHandler.sendEmptyMessage(0);
									 nextTrigger++;
								 }		
								 
								//stop sensing after 15secs secs
								 if(nextTrigger - elapsedTimeMin <= 0.75){
									 stopHandler.sendEmptyMessage(0);
								 }
							 }
							 
							 if(AppUtils.isServiceRunning(getApplicationContext())){
								 stopService(new Intent(SensingService.this, SensingService.class));
								 calculateElapsedMins();
								 String status = "C";
								 if(!AppUtils.checkCompletionStatus(currentTask.getTaskDuration(),getApplicationContext())){
									 status = "E";
								 }
								 if(AppUtils.checkInternetConnection(getApplicationContext()))
									AppUtils.uploadSensedData(appContext,status,taskId, 0);
					    		 else {
					    			currentTask.setTaskStatus(status); 
					    			currentTask.setSensedDataFileLocation(""+0);
					    			AppUtils.addToUploadList(currentTask, getApplicationContext());
					    		 }
								 logSensingElapsedTime(0, taskId, status);
							 } 
			    		}     
			    	 });
			t.start();
		} else {
			if(AppUtils.isServiceRunning(getApplicationContext())){
				 stopService(new Intent(SensingService.this, SensingService.class));
				 String status = "C";
				 if(!AppUtils.checkCompletionStatus(currentTask.getTaskDuration(),getApplicationContext())){
					 status = "E";
				 }
				 if(AppUtils.checkInternetConnection(getApplicationContext()))
					 AppUtils.uploadSensedData(appContext,status,taskId, 0);
	    		 else {
	    			 JTask newTask = new JTask(taskId, "");
	    			 newTask.setTaskStatus(status);
	    			 newTask.setTaskType("campusSensing");
	    			 newTask.setSensedDataFileLocation(""+0);
	    			 AppUtils.addToUploadList(newTask, getApplicationContext());
	    		 }
				 logSensingElapsedTime(0, taskId, status);
			} 
		}
		
	}
	
	private void calculateElapsedMins() {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		//calculate sensed time in minutes
		String elapsedTime = settings.getString("elapsedTime", "");	
		int mins = 0;
		if(!elapsedTime.equals("")){
			float elapsedTimeMin = Long.parseLong(elapsedTime)/(60*1000F);
			mins = Math.round(elapsedTimeMin);
		}
		
		//elapsedTotalMins and append. save it
		String elapsedTotalMins = settings.getString("elapsedMins", "0");	
		mins = mins + Integer.parseInt(elapsedTotalMins);
		logSensedMins(mins);
	}

	private void logSensedMins(int min) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("elapsedMins", min+"");
		editor.commit();
	}

	protected void logSensingStartTime(long start,int taskID, String status) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("startTime", AppUtils.currentTime(start));
		editor.putString("duration", sensingDuration+"");
		editor.putString("startTimeMillisecs", start+"");
		editor.putString("taskID", taskID + "");
		editor.putString("status", status);
		AppConstants.status = status;
		editor.commit();
	}
	
	protected void logSensingElapsedTime(long elapsed,int taskID, String status) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("taskID", taskID + "");
		editor.putString("status", status);
		AppConstants.status = status;
		if(elapsed == 0){
			editor.putString("elapsedTime", "");
			editor.putString("elapsedMins", "0");
		}
		else
			editor.putString("elapsedTime", elapsed+"");
		
		editor.commit();
	}

	private Handler startHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        // handle the process here
	    	startSensing();
	    }
	};
	
	private Handler stopHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        // handle the process here
	    	stopSensing();
	    }
	};
	
	protected void stopSensing() {
		if(mSensorManager!=null)
			mSensorManager.unregisterListener(acelSensorListener,mAccelerometer);
		mSensorManager = null;
//        mlocManager.removeUpdates(mlocListener);
	}

	private void startSensing() {
		startAccelerometerSensing();
		acelSensorListener = new AcelSensor(getApplicationContext(),currentTask);
		mSensorManager.registerListener(acelSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
//		myLocation.getLocation(this, locationResult);
//		mlocListener = new MyLocationListener(getApplicationContext(),currentTask);
		//can set the min time to 60000.
//		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0 , 0, mlocListener);
	}

	private void startAccelerometerSensing() {
		if(mSensorManager==null){
			mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
			mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
        
	}
	
	private void startGPSSensing() {
    	mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//    	boolean isGPS = mlocManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
//		if(!isGPS)
//			enableGPSSettings();
//    	mlocListener = new MyLocationListener(getApplicationContext(),currentTask);
//		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 60000 , 0, mlocListener);
	}
		
	private void enableGPSSettings() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Switch on GPS?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						TaskActivity ta = new TaskActivity();
						ta.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
					}
				});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("McSense App cannot sense with out GPS!!");
						stopService(new Intent(SensingService.this, SensingService.class));
					}
				});

		alert.show();
	}

//	protected void uploadSensedData(String status) {
//		// "accel_file"+taskId
//		// http servlet call
//		HttpClient httpclient = new DefaultHttpClient();
//		String providerURL = AppConstants.ip+"/McSenseWEB/pages/ProviderServlet";
//		HttpPost httppost = new HttpPost(providerURL);
//		HttpResponse response = null;
//		InputStream is = null;
//		StringBuilder sb = new StringBuilder();
//		
//		//read sensed data file and set as string
//		String sensedData = AppUtils.readFile(appContext,"sensing_file"+taskId);
////		String sensedData = AppUtils.readXFile("sensing_file"+taskId);
//		
//		// Add your data
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        nameValuePairs.add(new BasicNameValuePair("taskStatus", "Completed"));
//        nameValuePairs.add(new BasicNameValuePair("completionStatus", status));
//        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
//        nameValuePairs.add(new BasicNameValuePair("taskId", taskId+""));
//        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
//        nameValuePairs.add(new BasicNameValuePair("sensedData", sensedData));
//        
//		// Execute HTTP Get Request
//		try {
//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			
//			response = httpclient.execute(httppost);
//			System.out.println("Reading response...");
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(is, "iso-8859-1"), 8);
//
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//				System.out.println(sb);
//			}
//			is.close();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// read task from servlet
//		String resp = sb.toString();
//		System.out.println(resp);
//	}

	private void loadNotificationIcon() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.ic_launcher;
	    CharSequence tickerText = "McSense";
	    long when = System.currentTimeMillis();
	
	    Notification notification = new Notification(icon, tickerText, when);
	
	    Context context = getApplicationContext();
	    CharSequence contentTitle = "Mcsense";
	    CharSequence contentText = "Sense the world";
	    Intent notificationIntent = new Intent(this, Main.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	
	    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	
	    notificationManager.notify(NOTIFICATION_EX, notification);
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	public LocationResult locationResult = (new LocationResult(){
	    @Override
	    public void gotLocation(final Location loc){
	        //Got the location!
	    	Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
//			String gpsVals = "My current location is: " +	"\n Latitude = " + loc.getLatitude() +	"\n Longitude = " + loc.getLongitude();
			String gpsVals = "Timestamp:"+currentTimestamp+",TaskId:"+taskId+",ProviderId:"+AppConstants.providerId+",Latitude:" + loc.getLatitude() +	",Longitude:" + loc.getLongitude()+ ",Speed:" + loc.getSpeed()+" \n";
			
			AppUtils.writeToFile(getApplicationContext(), gpsVals,"sensing_file"+taskId);
//			AppUtils.writeToXFile(gpsVals,"sensing_file"+taskId);
			AppConstants.gpsLocUpdated = true;
	    }
	});
}
