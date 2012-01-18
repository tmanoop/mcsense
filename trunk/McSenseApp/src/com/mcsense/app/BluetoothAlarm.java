package com.mcsense.app;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mcsense.json.JTask;

public class BluetoothAlarm extends BroadcastReceiver {

	private final String REMINDER_BUNDLE = "BluetoothServiceReminderBundle";
	private JTask currentTask;
	private long serverTime;
	
	// this constructor is called by the alarm manager.
	public BluetoothAlarm() {
	}

	// you can use this constructor to create the alarm.
	// Just pass in the main activity as the context,
	// any extras you'd like to get later when triggered
	// and the timeout
	public BluetoothAlarm(Context context, Bundle extras, int timeoutInSeconds) {
		
		//get task ID
		ArrayList<JTask> taskList = extras.getParcelableArrayList("task");
		if(taskList != null && taskList.size()>0)
			currentTask = taskList.get(0);
		
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, BluetoothAlarm.class);
		intent.putExtra("JTask", currentTask);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		//Every 5minutes - 5*60*1000
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 5*60*1000, 
				pendingIntent);
		Toast.makeText(context, "Bluetooth Sensing Started", Toast.LENGTH_SHORT).show();
		
	}

	private boolean hasTaskExpired(Context context) {
		SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		
		//get server time
		Calendar cal=Calendar.getInstance();
		if(AppUtils.checkInternetConnection(context)){
			serverTime = AppUtils.getServerTime(currentTask.getTaskId());
			Log.d(AppConstants.TAG, "Server time: "+serverTime);
			Date serverDate = new Date(serverTime);
			cal.setTime(serverDate);
		}
		
		//get exp time
		Timestamp expTime = currentTask.getTaskExpirationTime();
		Log.d(AppConstants.TAG, "Server time hour: "+expTime);
		Date expDate = new Date();
		if(expTime != null)
			expDate = new Date(expTime.getTime());
		Calendar expCal=Calendar.getInstance();
		expCal.setTime(expDate);
		
		//call server to get time, if not 10pm continue sensing.
		int serverHour = cal.get(Calendar.HOUR_OF_DAY);
		int serverMin = cal.get(Calendar.MINUTE);
		Log.d(AppConstants.TAG, "Server time hour: "+serverHour);
		//if server time greater than 10pm, stop sensing
		if(cal.get(Calendar.DAY_OF_MONTH) == expCal.get(Calendar.DAY_OF_MONTH) 
				&& serverHour >= expCal.get(Calendar.HOUR_OF_DAY)
				&& serverMin >= expCal.get(Calendar.MINUTE)){
			//stop BL Alarm
			return true;
		}
		return false; 
	}

	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "Bluetooth Scan started", Toast.LENGTH_SHORT).show();
		
		JTask jTaskObjInToClass = intent.getExtras().getParcelable("JTask");
		currentTask = jTaskObjInToClass;
		
		//check if task expired or not and complete it if expires
		if(!hasTaskExpired(context)){
			if(AppUtils.isBluetoothEnabled(context)){
				AppConstants.BLScanCount++;
				logBluetoothScanCount(AppConstants.BLScanCount, context);
				iniBluetoothSensingService(context);
			} else{
				//stop bluetooth alarm
				AppUtils.stopBluetoothAlarm(context);
			}			
		} else {
			 String status = "C";
			 //add total sensed time criteria to identify completion status
			 SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
			 String countString = settings.getString("BLScanCount", "0");
			 int BLScanCount = Integer.parseInt(countString);
			 //if atleast 6 hours of BL scanning is not done, then task is not successfully complete. Mark it as "E".
			 if(BLScanCount < 72)
				 status = "E";
			 //upload sensed data
			 if(AppUtils.checkInternetConnection(context))
				AppUtils.uploadSensedData(context, status, currentTask.getTaskId());
    		 else {
    			currentTask.setTaskStatus(status); 
    			AppUtils.addToUploadList(currentTask, context);
    		 }

			//stop bluetooth alarm
			AppUtils.stopBluetoothAlarm(context);
		}		
	}

	protected void iniBluetoothSensingService(Context context) {
			Intent intent = new Intent(context, BluetoothService.class);
			intent.putExtra("JTask", currentTask);
			context.startService(intent);
	}
	
	protected void logBluetoothScanCount(int BLScanCount, Context context) {
		SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("BLScanCount", BLScanCount+"");
		editor.commit();
	}
}
