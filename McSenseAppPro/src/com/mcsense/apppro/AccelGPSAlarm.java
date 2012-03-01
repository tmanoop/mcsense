package com.mcsense.apppro;

import java.util.ArrayList;
import java.util.Calendar;

import com.mcsense.json.JTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AccelGPSAlarm extends BroadcastReceiver {
	
	private final String REMINDER_BUNDLE = "AccelGPSServiceReminderBundle";
	private JTask currentTask;
	private long serverTime;
	
	// this constructor is called by the alarm manager.
	public AccelGPSAlarm() {
		
	}
	
	// you can use this constructor to create the alarm.
	// Just pass in the main activity as the context,
	// any extras you'd like to get later when triggered
	// and the timeout
	public AccelGPSAlarm(Context context, Bundle extras, int timeoutInSeconds) {
		
		//get task ID
		ArrayList<JTask> taskList = extras.getParcelableArrayList("task");
		if(taskList != null && taskList.size()>0)
			currentTask = taskList.get(0);
		
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AccelGPSAlarm.class);
		intent.putExtra("JTask", currentTask);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		//Every minute - 60*1000
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 60*1000, 
				pendingIntent);
		Toast.makeText(context, "Campus Sensing Started", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "Campus Sensing now", Toast.LENGTH_SHORT).show();
		Log.d(AppConstants.TAG, "Campus Sensing now");
		
		JTask jTaskObjInToClass = intent.getExtras().getParcelable("JTask");
		currentTask = jTaskObjInToClass;
		
		//add total sensed time criteria to identify completion status
		 SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		 String countString = settings.getString("CampusSenseCount", "0");
		 int CampusSenseCount = Integer.parseInt(countString);
		 
		//check if task expired or not and complete it if expires. 
		//10 hour (600min) of sensing is limited just to limit upload file size below 1mb.
			if(!AppUtils.hasTaskExpired(context, currentTask) && CampusSenseCount < AppConstants.MAX_SENSING_THRESHOLD_MINS){
				if(AppUtils.isGPSEnabled(context)){
					CampusSenseCount++;
					logCampusSenseCount(CampusSenseCount, context);
					iniCampusSenseService(context);
				} else{
					//stop AccelGPS alarm
					AppUtils.stopAccelGPSAlarm(context);
				}			
			} else {
				AppUtils.uploadCampusSensedData(context, currentTask);
			}		
	}
	
	protected void iniCampusSenseService(Context context) {
		Intent intent = new Intent(context, CampusSenseService.class);
		intent.putExtra("JTask", currentTask);
		context.startService(intent);
	}

	protected void logCampusSenseCount(int CampusSenseCount, Context context) {
		SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("CampusSenseCount", CampusSenseCount+"");
		editor.commit();
	}

}