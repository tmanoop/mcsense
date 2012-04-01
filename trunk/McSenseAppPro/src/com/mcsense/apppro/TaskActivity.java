package com.mcsense.apppro;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.mcsense.apppro.MyLocation.LocationResult;
import com.mcsense.json.JTask;

public class TaskActivity extends Activity {
	private static final int PICTURE_RESULT = 9;
	private static final boolean test = false;
	LinearLayout linear;
	Bitmap bitmap;
	JTask currentTask;
	Context context;
	String currentLocation = "";
	private ProgressDialog pDialog;

	TextView text;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        linear = new LinearLayout(this);
//
//        linear.setOrientation(LinearLayout.VERTICAL);
//
//        text = new TextView(this);
//
//
//        text.setText("This is an example for the Bright Hub!");
//
//        linear.addView(text);

//        setContentView(linear);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		context = getApplicationContext();
        setContentView(R.layout.task);
        
        JTask jTaskObjInToClass = getIntent().getExtras().getParcelable("JTask");
//        showToast(jTaskObjInToClass.getTaskDescription());
        text = (TextView) findViewById(R.id.txtTest);
        text.setText(jTaskObjInToClass.getTaskName());
        currentTask = jTaskObjInToClass;
//        loadTaskDetails(jTaskObjInToClass);
        
//        iniAccel();
        
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		loadTaskDetails(currentTask);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.gc();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ImageView bmImage = (ImageView)findViewById(R.id.imageView1);//new ImageView(this);//
		bmImage.setImageBitmap(null);
		bmImage = null;
		System.gc();
	}
	
	private void loadTaskDetails(JTask jTask) {
//		LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.linearLayout1);
		//get calling tab type
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		final String tab_type = getIntent().getExtras().getString("tab_type");
		String tskStat = jTask.getTaskStatus();
		
		if(tab_type.equals("completed")){
			TextView clientPayTextView = (TextView) findViewById(R.id.clientPayTextView); 
			if(tskStat.equals("C"))
				clientPayTextView.setText("Dollars earned:");
			else if(tskStat.equals("U"))
				clientPayTextView.setText("Dollars pending:");
			
			String startTime = settings.getString("acceptTime", "");			
			TextView start = (TextView) findViewById(R.id.start); 
			
			
			TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
			textElapsed.setText("Task Completed on: ");
			
			TextView duration = (TextView) findViewById(R.id.duration);
			TextView textDuration = (TextView) findViewById(R.id.textView7);
			if(jTask.getTaskType().equals("photo")){
				textDuration.setVisibility(View.INVISIBLE);
				duration.setVisibility(View.INVISIBLE);
			} else {
				String sensedDuration = jTask.getSensedDataFileLocation();
				textDuration.setText("Sensing Data Recorded for:");
				if(sensedDuration==null)
					sensedDuration = "0";
				duration.setText(sensedDuration+" mins");
			}
			
			

			TextView elapsed = (TextView) findViewById(R.id.elapsed);

			if(jTask.getTaskCompletionTime() != null)
				elapsed.setText(AppUtils.getFormatedTime(jTask.getTaskCompletionTime()));
			
			if(jTask.getTaskAcceptedTime() != null)
				start.setText(AppUtils.getFormatedTime(jTask.getTaskAcceptedTime()));
			
			
		} else if(tab_type.equals("pending")){
//			String taskIDString = settings.getString("taskID", "");
//			String status = settings.getString("status", "");
//			int sensingTaskID = 0;
//			if(!taskIDString.equals(""))
//				sensingTaskID = Integer.parseInt(taskIDString);
			if(jTask.getTaskType().equals("campusSensing")){//&& status.equals("IP")
//				String startTime = settings.getString("acceptTime", "");
//				String elapsedTime = settings.getString("elapsedTime", "");			
				
				TextView start = (TextView) findViewById(R.id.start); 
//				start.setText(startTime);
				if(jTask.getTaskAcceptedTime() != null)
					start.setText(AppUtils.getFormatedTime(jTask.getTaskAcceptedTime()));
				
				
				TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
				TextView elapsed = (TextView) findViewById(R.id.elapsed);
//				textElapsed.setVisibility(View.INVISIBLE);
//				elapsed.setVisibility(View.INVISIBLE);
			
				textElapsed.setText("Task Expire at:");
				
				if(jTask.getTaskExpirationTime() != null)
					elapsed.setText(AppUtils.getFormatedTime(jTask.getTaskExpirationTime()));
				
//				if(!elapsedTime.equals("") && status.equals("IP")){
//					TextView elapsed = (TextView) findViewById(R.id.elapsed);
//					float elapsedTimeMin = Long.parseLong(elapsedTime)/(60*1000F);
//					int min = Math.round(elapsedTimeMin);
//					if(min < 60 )
//						elapsed.setText(min +" mins");
//					else{
//						int hour = min / 60;
//						int rem_min = min % 60;
//						elapsed.setText(hour +" Hours " + rem_min +" mins");
//					}
//						
//				}
			}
			
			if(jTask.getTaskType().equals("photo")){
				TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
				textElapsed.setText("Task Expires on: ");
				TextView elapsed = (TextView) findViewById(R.id.elapsed);
				if(jTask.getTaskExpirationTime() != null)
					elapsed.setText(AppUtils.getFormatedTime(jTask.getTaskExpirationTime()));
				
				TextView start = (TextView) findViewById(R.id.start); 
//				start.setText(startTime);
				if(jTask.getTaskAcceptedTime() != null)
					start.setText(AppUtils.getFormatedTime(jTask.getTaskAcceptedTime()));
				
				TextView textDuration = (TextView) findViewById(R.id.duration);
				TextView durationVal = (TextView) findViewById(R.id.textView7);
				textDuration.setVisibility(View.INVISIBLE);
				durationVal.setVisibility(View.INVISIBLE);
			}
			
			if(jTask.getTaskType().equals("bluetooth")){
				TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
				textElapsed.setText("Task Expires on: ");
				TextView elapsed = (TextView) findViewById(R.id.elapsed);
				if(jTask.getTaskExpirationTime() != null)
					elapsed.setText(AppUtils.getFormatedTime(jTask.getTaskExpirationTime()));
				
				TextView start = (TextView) findViewById(R.id.start); 
//				start.setText(startTime);
				if(jTask.getTaskAcceptedTime() != null)
					start.setText(AppUtils.getFormatedTime(jTask.getTaskAcceptedTime()));
				
			} else {
				TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
				textElapsed.setText("Task Expires on: ");
				TextView elapsed = (TextView) findViewById(R.id.elapsed);
				if(jTask.getTaskExpirationTime() != null)
					elapsed.setText(AppUtils.getFormatedTime(jTask.getTaskExpirationTime()));
				
				TextView start = (TextView) findViewById(R.id.start); 
//				start.setText(startTime);
				if(jTask.getTaskAcceptedTime() != null)
					start.setText(AppUtils.getFormatedTime(jTask.getTaskAcceptedTime()));
			}
		}  else if(tab_type.equals("new")){
			TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
			TextView elapsed = (TextView) findViewById(R.id.elapsed);
			textElapsed.setVisibility(View.INVISIBLE);
			elapsed.setVisibility(View.INVISIBLE);
		}
		
		TextView clientPay = (TextView) findViewById(R.id.clientPay); 
		clientPay.setText("$"+jTask.getClientPay());
		
		TextView taskStatus = (TextView) findViewById(R.id.taskStatus);
		
		if(tskStat.equals("P"))
			tskStat = "Available";
		else if(tskStat.equals("IP"))
			tskStat = "Accepted";
		else if(tskStat.equals("C"))
			tskStat = "Success";
		else if(tskStat.equals("E"))
			tskStat = "Failed";
		else if(tskStat.equals("U"))
			tskStat = "Pending Upload";
		else if(tskStat.equals("V"))
			tskStat = "Validation Pending";
		taskStatus.setText(tskStat);
		
		TextView taskType = (TextView) findViewById(R.id.taskType); 
		taskType.setText(jTask.getTaskType());
		
		TextView durationText = (TextView) findViewById(R.id.textView7); 
		TextView duration = (TextView) findViewById(R.id.duration); 
		int sensingDuration = 0;
		
		if(tskStat.equals("Available")){
			
			durationText.setText("Task Expire at:");
//			duration.setText("10 PM");	
			
			if(jTask.getTaskExpirationTime() != null)
				duration.setText(AppUtils.getFormatedTime(jTask.getTaskExpirationTime()));
			
		} else if(tskStat.equals("Accepted")) {
			long currentTime = AppUtils.getServerTime(jTask.getTaskId());
			Date acceptDate = new Date();
			if(currentTime != 0)
				acceptDate = new Date(currentTime);
			Calendar acceptCal=Calendar.getInstance();
			acceptCal.setTime(acceptDate);
			int hour = acceptCal.get(Calendar.HOUR_OF_DAY);
			
			//get exp time
			Timestamp expTime = currentTask.getTaskExpirationTime();
			Log.d(AppConstants.TAG, "Exp time hour: "+expTime);
			Date expDate = new Date();
			int expHour = 22;
			if(expTime != null){
				expDate = new Date(expTime.getTime());
				Calendar expCal=Calendar.getInstance();
				expCal.setTime(expDate);
				expHour = expCal.get(Calendar.HOUR_OF_DAY);
			}
						
			int totalMins = (expHour - hour) * 60;
			totalMins = totalMins + currentTask.getTaskExpirationTime().getMinutes();
			int hourMins = acceptCal.get(Calendar.MINUTE);
			
			sensingDuration = totalMins - hourMins;
			if(sensingDuration < 0)
				sensingDuration = 0;
//			String durationMins = settings.getString("duration", "");
			durationText.setText("Sensing Data Recorded for:");
			if(jTask.getTaskType().equals("campusSensing")){
//				int elapsedTotalMins = findElapsedMins();
//				String elapsedTotalMins = settings.getString("elapsedMins", "0");
				String countString = settings.getString("CampusSenseCount", "0");
				int CampusSenseCount = Integer.parseInt(countString);
				int elapsedTotalMins = (CampusSenseCount -1);
				if(elapsedTotalMins<0)
					elapsedTotalMins = 0;
				duration.setText(elapsedTotalMins+" mins");
			} else if(jTask.getTaskType().equals("bluetooth")){
				String countString = settings.getString("BLScanCount", "0");
				int BLScanCount = Integer.parseInt(countString);
				int elapsedTotalMins = (BLScanCount -1) * 5;
				if(elapsedTotalMins<0)
					elapsedTotalMins = 0;
				duration.setText(elapsedTotalMins+" mins");
			} else if(jTask.getTaskType().equals("appUsage")){
				String countString = settings.getString("AppScanCount", "0");
				int AppScanCount = Integer.parseInt(countString);
				int elapsedTotalMins = (AppScanCount -1) * 5;
				if(elapsedTotalMins<0)
					elapsedTotalMins = 0;
				duration.setText(elapsedTotalMins+" mins");
			}
		}
		
		final TextView taskDescTxt = (TextView) findViewById(R.id.taskDescTextView);
		final TextView taskDesc = (TextView) findViewById(R.id.taskDescription); 
		if(jTask.getTaskType().equals("photo") && tab_type.equals("pending") && bitmap!=null){
			taskDescTxt.setVisibility(View.INVISIBLE);
			taskDesc.setVisibility(View.INVISIBLE);
		} else{
			taskDesc.setText(jTask.getTaskDescription());
		}
		
		final Button button = (Button) findViewById(R.id.button1); 
		
		//get task ID
		final String taskID = jTask.getTaskId()+"";
		final String tskType = jTask.getTaskType();
		String buttonName = "";
		if(tab_type.equals("completed")){
        	button.setVisibility(View.INVISIBLE);
        } else if(tab_type.equals("new")){
        	buttonName = "Accept Task";
        } else if(tab_type.equals("pending")){
			if(tskType.equals("campusSensing")){
				if(sensingDuration == 0)
					buttonName = "Upload Sensed Data";
				else{
					if(AppUtils.isAccelGPSAlarmExist(getApplicationContext()))
						buttonName = "Stop Sensing";
					else
						buttonName = "Re-start Sensing";
				}				
			} else if(tskType.equals("bluetooth")){
				if(sensingDuration == 0)
					buttonName = "Upload Sensed Data";
				else{

					if(AppUtils.isBluetoothAlarmExist(getApplicationContext()))
						buttonName = "Stop Sensing";
					else
						buttonName = "Re-start Sensing";
				}			
			} else if(tskType.equals("photo")){
				if(bitmap!=null)
					buttonName = "Upload Photo";
				else
					buttonName = "Take Photo";
			} else if(tskType.equals("appUsage")){
				if(sensingDuration == 0)
					buttonName = "Upload Sensed Data";
				else{

					if(AppUtils.isAppUsageAlarmExist(getApplicationContext()))
						buttonName = "Stop Sensing";
					else
						buttonName = "Re-start Sensing";
				}	
			} else if(tskType.equals("wifi")){
				if(sensingDuration == 0)
					buttonName = "Upload Sensed Data";
				else{

					if(AppUtils.isHardwareMonitorAlarmExist(getApplicationContext()))
						buttonName = "Stop Sensing";
					else
						buttonName = "Re-start Sensing";
				}	
			}
		}
		
		button.setText(buttonName);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(tab_type.equals("new")){
            		//check for already running service
            		if(tskType.equals("campusSensing") && AppUtils.isServiceRunning(getApplicationContext())){
            			showToast("Another Sensing Task is already running.");
            			taskDesc.setText(currentTask.getTaskDescription());
//            			taskDesc.append("\t\n \t\n Task not accepted. \n Another Sensing Task is already running.");
            			String appendText = "\t\n \t\n Task not accepted. \n Another Sensing Task is already running.";
            			Spannable WordtoSpan = new SpannableString(appendText);

            			WordtoSpan.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, appendText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            			taskDesc.append(WordtoSpan);
            		} else {
            			if(AppUtils.hasRequiredSensors(currentTask)){
            				//call server to accept the task and update its status
                    		String resp = acceptTask(taskID);
                    		showToast(resp);
                    		if(resp.equals("Accepted")){
                        		if(tskType.equals("campusSensing"))
                            		iniSensingService();
                        		else if(tskType.equals("bluetooth"))
                            		iniBluetoothAlarmService();
                        		else if(tskType.equals("appUsage"))
                        			iniAppUsageAlarmService();
                        		else if(tskType.equals("wifi"))
                        			iniHardwareMonitorAlarmService();
                        		else if(tskType.equals("photo"))
                        			finish();
                    		}
                    		logAcceptTime(taskID);
            			} else {
            				showToast("Required Sensors or Radios not available!!");
            			}
            			
//                		finish();
            		}          		
            	} else if(tab_type.equals("pending")){
            		String buttonName = button.getText().toString();
            		if(tskType.equals("photo")){
            			if(buttonName.equals("Upload Photo"))
                    		uploadProgress();
            			else{

//                			showToast("Start Camera");
                    		bitmap = null;
                    		photo();
            			}
            		} else if(tskType.equals("campusSensing")){
//            			String buttonName = button.getText().toString();
            			if(buttonName.equals("Stop Sensing")){
//            				stopService(new Intent(TaskActivity.this, SensingService.class));   
            				AppUtils.stopAccelGPSAlarm(context);
            				AppUtils.addToSuspendedList(currentTask, context);
            				button.setText("Re-start Sensing");
            			} else if(buttonName.equals("Re-start Sensing")){
            				iniSensingService();
            				button.setText("Stop Sensing");
            			} else if(buttonName.equals("Upload Sensed Data")){
            				AppUtils.uploadCampusSensedData(context, currentTask);
            				finish();
            			}
            		} else if(tskType.equals("bluetooth")){
            			if(buttonName.equals("Stop Sensing")){
            				//stop bluetooth alarm
            				AppUtils.stopBluetoothAlarm(context);
            				AppUtils.addToSuspendedList(currentTask, context);
            				button.setText("Re-start Sensing");
            			} else if(buttonName.equals("Re-start Sensing")){
            				iniBluetoothAlarmService();
            				button.setText("Stop Sensing");
            			} else if(buttonName.equals("Upload Sensed Data")){
            				AppUtils.uploadBluetoothSensedData(context, currentTask);
            				finish();
            			}
            		} else if(tskType.equals("appUsage")){
            			if(buttonName.equals("Stop Sensing")){
            				//stop bluetooth alarm
            				AppUtils.stopAppUsageAlarm(context);
            				AppUtils.addToSuspendedList(currentTask, context);
            				button.setText("Re-start Sensing");
            			} else if(buttonName.equals("Re-start Sensing")){
            				iniAppUsageAlarmService();
            				button.setText("Stop Sensing");
            			} else if(buttonName.equals("Upload Sensed Data")){
            				AppUtils.uploadAppUsageSensedData(context, currentTask);
            				finish();
            			}
            		} else if(tskType.equals("wifi")){
            			if(buttonName.equals("Stop Sensing")){
            				//stop bluetooth alarm
            				AppUtils.stopHardwareMonitorAlarm(context);
            				AppUtils.addToSuspendedList(currentTask, context);
            				button.setText("Re-start Sensing");
            			} else if(buttonName.equals("Re-start Sensing")){
            				iniHardwareMonitorAlarmService();
            				button.setText("Stop Sensing");
            			} else if(buttonName.equals("Upload Sensed Data")){
            				AppUtils.uploadHardwareSensedData(context, currentTask);
            				finish();
            			}
            		}
            	}
            }
        });
//		TextView acelValues = (TextView) findViewById(R.id.acelValues); 
//		String acelVal = getAccelValues();
//		acelValues.setText(acelVal);
//		linearLayout.addView(t);
		
	}

	protected void iniBluetoothAlarmService() {
//		AppUtils.enableBluetoothRadio(this);
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			enableBluetoothRadio(context);
		} else{
			startBluetoothAlarmService();
		}
		
	}
	
	protected void iniAppUsageAlarmService() {
		AppUtils.removeFromSuspendedList(currentTask,context);
		
		Bundle bundle = new Bundle();
		ArrayList<JTask> taskList = new ArrayList();
		taskList.add(currentTask);
		bundle.putParcelableArrayList("task", taskList);
		// add extras here..
		AppUsageAlarm alarm = new AppUsageAlarm(context, bundle, 30);
		finish();
	}
	
	protected void iniHardwareMonitorAlarmService() {
		AppUtils.removeFromSuspendedList(currentTask,context);
		
		Bundle bundle = new Bundle();
		ArrayList<JTask> taskList = new ArrayList();
		taskList.add(currentTask);
		bundle.putParcelableArrayList("task", taskList);
		// add extras here..
		HardwareMonitoringAlarm alarm = new HardwareMonitoringAlarm(context, bundle, 30);
		finish();
	}
	
	private void startBluetoothAlarmService(){
		AppUtils.removeFromSuspendedList(currentTask,context);
		
		Bundle bundle = new Bundle();
		ArrayList<JTask> taskList = new ArrayList();
		taskList.add(currentTask);
		bundle.putParcelableArrayList("task", taskList);
		// add extras here..
		BluetoothAlarm alarm = new BluetoothAlarm(context, bundle, 30);
		finish();
	}
	
	private void startAccelGPSAlarmService(){
		AppUtils.removeFromSuspendedList(currentTask,context);
		
		Bundle bundle = new Bundle();
		ArrayList<JTask> taskList = new ArrayList();
		taskList.add(currentTask);
		bundle.putParcelableArrayList("task", taskList);
		// add extras here..
		AccelGPSAlarm alarm = new AccelGPSAlarm(context, bundle, 30);
		finish();
	}
	
	private void enableBluetoothRadio(Context context){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Switch on Bluetooth?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						AppUtils.enableBluetoothRadio(TaskActivity.this);
						startBluetoothAlarmService();
					}
				});

		alert.setNegativeButton("Suspend",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("Task Suspended!!");
						AppUtils.stopBluetoothAlarm(getApplicationContext());
						AppUtils.addToSuspendedList(currentTask, getApplicationContext());
					}
				});

		alert.show();
	}

	protected void logAcceptTime(String taskID) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		long serverTime = AppUtils.getServerTime(Integer.parseInt(taskID));
		editor.putString("acceptTime", AppUtils.currentTime(serverTime));
		editor.putString("acceptTimeMillis", serverTime+"");
		editor.putString("taskID", taskID + "");
		editor.putString("status", "IP");
		AppConstants.status = "IP";
		editor.commit();
	}

	protected void logSensingElapsedTime(long elapsed,int taskID, String status) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("taskID", taskID + "");
		editor.putString("status", status);
		editor.putString("elapsedTime", elapsed+"");
		Log.d(AppConstants.TAG, "status: "+status+"taskID: "+ taskID);
		AppConstants.status = status;
		editor.putString("completedTime", AppUtils.currentTime());
		editor.commit();
	}
	
	protected void iniSensingService() {
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		boolean isGPS = mlocManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
		if(!isGPS)
			enableGPSSettings();
		else{
//			startSensingService();
			startAccelGPSAlarmService();
		}
	}
	
	private void startSensingService(){
		AppUtils.removeFromSuspendedList(currentTask,context);
		
		Intent intent = new Intent(this, SensingService.class);
		intent.putExtra("JTask", currentTask);
		startService(intent);
		finish();
	}

	private void enableGPSSettings() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Switch on GPS?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						TaskActivity.this.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//						startSensingService();
						startAccelGPSAlarmService();
					}
				});

		alert.setNegativeButton("Suspend",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("Task Suspended!!");
						AppUtils.stopAccelGPSAlarm(getApplicationContext());
						AppUtils.addToSuspendedList(currentTask, getApplicationContext());
					}
				});

		alert.show();
	}
	
	protected String acceptTask(String taskID) {

		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/ProviderServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("taskStatus", "Accepted"));
        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
        nameValuePairs.add(new BasicNameValuePair("taskId", currentTask.getTaskId()+""));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        
		// Execute HTTP Get Request
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			Log.d(AppConstants.TAG, "Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				Log.d(AppConstants.TAG, ""+sb);
			}
			is.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read task from servlet
		String resp = sb.toString();
		Log.d(AppConstants.TAG, resp);
		return resp.trim();
//		showToast("Submitted: " + resp + " \r\n");
	}

	private void photo() {
//		showToast("Photo");
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File image = new File(Environment.getExternalStorageDirectory(),"McSenseImage.jpg");
		try {
			//create an internal writable file. This is to support phones, which do not have sdcard. 
			FileOutputStream fos = context.openFileOutput(AppConstants.imageFileName+currentTask.getTaskId(), Context.MODE_WORLD_WRITEABLE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File image = new File(context.getFilesDir().toString(),AppConstants.imageFileName+currentTask.getTaskId());
		camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

        this.startActivityForResult(camera, PICTURE_RESULT);
//        showToast("Photo Saved!!"+Uri.fromFile(image));
//        String imageInSD = Environment.getDownloadCacheDirectory() + "/McSenseImage.jpg";
//        showToast("imageInSD: "+imageInSD);
//	    Intent myIntent = new Intent(item.getIntent());
//        startActivity(myIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		showToast("requestCode: "+requestCode);
//		showToast("resultCode: "+resultCode);
		Log.d("McSense", "requestCode: "+requestCode+" resultCode: "+resultCode);
		Bitmap image = null;
	    if(requestCode == PICTURE_RESULT){
	        if(resultCode == Activity.RESULT_OK) {
	            if(data!=null){
//	                image = BitmapFactory.decodeFile(data.getExtras().get(MediaStore.Images.Media.TITLE).toString());
//	                preview(image);
//	                showToast("Photo Saved!!");
//	                grid.add(image);            
//	                images.addItem(image);
	            }
	            if(data==null){
//	                Toast.makeText(Team_Viewer.this, "no data.", Toast.LENGTH_SHORT).show();
	            }
	            preview(image);
	        }
	        else if(resultCode == Activity.RESULT_CANCELED) {
//	            Toast.makeText(Team_Viewer.this, "Picture could not be taken.", Toast.LENGTH_SHORT).show();
//	        	showToast("RESULT_CANCELED!!");
	        	
	        }
//	        showToast("data:!!"+data);
//	        preview(image);
//	        uploadPhoto();
	    }
	}
	
	private void preview(Bitmap image){
//		showToast("preview!!");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ImageView bmImage = (ImageView)findViewById(R.id.imageView1);//new ImageView(this);//
		   BitmapFactory.Options bmOptions;
		   bmOptions = new BitmapFactory.Options();
		   bmOptions.inSampleSize = 8;
		   bmOptions.requestCancelDecode();
//		   String imageInSD = "/sdcard/McSenseImage.jpg";
		   String imageInSD = context.getFilesDir().toString()+"/"+AppConstants.imageFileName+currentTask.getTaskId();
//		   showToast("imageInSD: "+imageInSD);
		   bitmap = BitmapFactory.decodeFile(imageInSD);
//		   showToast("bitmap: "+bitmap);
		   bmImage.setImageBitmap(bitmap);
		   
		   
		   //add upload button
		   addUploadButton();
//		   uploadPhoto();
		   
		 //get GPS location
			Runnable start = new Runnable( ) {
			    public void run( ) {
			    	MyLocation myLocation = new MyLocation();
					myLocation.getLocation(context, locationResult);
			    }
			};
			start.run();
	}
	
	private void addUploadButton() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout1);
		Log.d(AppConstants.TAG, "adding upload button");
		//add upload notification text
		TextView uploadTextView = new TextView(this); 
		uploadTextView.setText("On upload, the photo below will be used to complete the task.");
		uploadTextView.setTextColor(Color.YELLOW);
        RelativeLayout.LayoutParams t = new 
        RelativeLayout.LayoutParams( 
        		RelativeLayout.LayoutParams.FILL_PARENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT 
                    ); 
        rl.addView(uploadTextView,t);
        //upload button name is handled on loading task details.
	}

	public void uploadProgress() {
	    pDialog = ProgressDialog.show(this, "Uploading Photo..", "Please wait", true,false);
	    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    Thread thread = new Thread(new Runnable() {
	    	 public void run() {
		    		 // add upload code here
		    		 if(AppUtils.checkInternetConnection(context))
		    		 	uploadPhoto();
		    		 else
		    			 AppUtils.addToUploadList(currentTask, context);
		    		 handler.sendEmptyMessage(0);
	    		}     
	    	 });
	    thread.start();
	}
	
	private Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        pDialog.dismiss();
	        // handle the result here
			finish();
	    }
	};
	
	private void uploadPhoto(){
//		showToast("uploading!!");
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/PhotoServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		//read data
//		TextView taskIDTxt = (TextView) findViewById(R.id.taskID);;
//		TextView taskDescTxt = (TextView) findViewById(R.id.txtTest);
		
//		String imageInSD = Environment.getExternalStorageDirectory()+"/McSenseImage.jpg";
//		String imageInSD = "/sdcard/McSenseImage.jpg";
		//Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
//		bitmap = BitmapFactory.decodeFile(imageInSD);
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		int compressRatio = 70;
		String ba1="";
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, compressRatio, bao);
			Log.d(AppConstants.TAG, "Image size: "+bao.size());
			
			while(bao.size()>AppConstants.IMAGE_SIZE_THRESHOLD){
				bao = null;
				bao = new ByteArrayOutputStream();
				compressRatio = compressRatio - 10;//reduce image quality by 10 each time
				bitmap.compress(Bitmap.CompressFormat.JPEG, compressRatio, bao);
				Log.d(AppConstants.TAG, "Image size: "+bao.size());
			}
			
			bitmap.recycle();
			System.gc();
			
	        byte [] ba = bao.toByteArray();
//			ba1 = Base64.encodeToString(ba,0);
//	        ArrayList<String> baList = getBitmapEncodedString(ba);
//	        ba1 = baList.get(0);
	    
			// upload with multipart
			ByteArrayBody bab = new ByteArrayBody(ba, "image");
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			 
			reqEntity.addPart("taskId", new StringBody(currentTask.getTaskId()+""));
	        reqEntity.addPart("providerId", new StringBody(AppConstants.providerId));
	        reqEntity.addPart("type", new StringBody("mobile"));
	        reqEntity.addPart("image",bab);
	        reqEntity.addPart("currentLocation",new StringBody(currentLocation));
			httppost.setEntity(reqEntity);
			/*
			// Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	//        nameValuePairs.add(new BasicNameValuePair("taskDesc", currentTask.getTaskDescription()));
	        nameValuePairs.add(new BasicNameValuePair("taskId", currentTask.getTaskId()+""));
	        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
	        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
	        nameValuePairs.add(new BasicNameValuePair("image",ba1));
	        nameValuePairs.add(new BasicNameValuePair("currentLocation",currentLocation));
        
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			*/
			response = httpclient.execute(httppost);
			Log.d(AppConstants.TAG, "Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				Log.d(AppConstants.TAG, ""+sb);
			}
			is.close();
			bao.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
//			showToast("uploading error!! "+e1.getMessage());
		}		

		// read task from servlet
		String resp = sb.toString();
		Log.d(AppConstants.TAG, resp);
		deletePhoto();
//		showToast("Uploaded: \r\n");
	}
	
	private void deletePhoto() {
		context.deleteFile(AppConstants.imageFileName+currentTask.getTaskId());
		bitmap = null;
	}

	private ArrayList<String> getBitmapEncodedString(byte[] ba) {
		ArrayList<String> ba1 = new ArrayList<String>();
		int chunkSize = 256;
        for(int i =0; i<ba.length;i=i+chunkSize){
        	try {
        		int nextSize = i+chunkSize;
            	if(ba.length<i+chunkSize)
            		nextSize = ba.length;
            	Log.d(AppConstants.TAG, "nextSize: "+nextSize);
				String temp = Base64.encodeToString(ba,i,nextSize,Base64.DEFAULT);
				ba1.add(temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast("uploading error!! \r\n"+e.getMessage());
			}
        }
		return ba1;
	}
	
	private String getAccelValues() {
		String FILENAME = "accel_file";
		String acelValues = "";
		try {
			//reading
			FileInputStream fis = openFileInput(FILENAME);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			acelValues = new String(buffer);
			
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return acelValues;
	}

	private void iniAccel() {
		Intent i = new Intent(getApplicationContext(), Sensors.class);
        startActivity(i);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	protected void uploadSensedData() {
		// "accel_file"+currentTask.getTaskId()
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/ProviderServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		//read sensed data file and set as string
		String sensedData = AppUtils.readFile(getApplicationContext(),"sensing_file"+currentTask.getTaskId());
//		String sensedData = AppUtils.readXFile("sensing_file"+currentTask.getTaskId());
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("taskStatus", "Completed"));
        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
        nameValuePairs.add(new BasicNameValuePair("taskId", currentTask.getTaskId()+""));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        nameValuePairs.add(new BasicNameValuePair("sensedData", sensedData));
        
		// Execute HTTP Get Request
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			Log.d(AppConstants.TAG, "Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				Log.d(AppConstants.TAG, ""+sb);
			}
			is.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read task from servlet
		String resp = sb.toString();
		Log.d(AppConstants.TAG, resp);
		finish();
	}
	
	public LocationResult locationResult = (new LocationResult(){
	    @Override
	    public void gotLocation(final Location loc){
	        //Got the location!
//	    	Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	    	if(loc != null)
	    		currentLocation = "Latitude:" + loc.getLatitude() +	",Longitude:" + loc.getLongitude()+ " \n";
			
			AppConstants.gpsLocUpdated = true;
	    }
	});
	
	protected void logBluetoothScanCount(int BLScanCount, Context context) {
		SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("BLScanCount", BLScanCount+"");
		editor.commit();
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
	
	private int findElapsedMins() {
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
		return mins;
	}
	
	
}
