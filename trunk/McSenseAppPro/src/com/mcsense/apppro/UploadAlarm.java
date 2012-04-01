package com.mcsense.apppro;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mcsense.json.JTask;

public class UploadAlarm extends BroadcastReceiver {
	private final String REMINDER_BUNDLE = "UploadReminderBundle";
	
	// this constructor is called by the alarm manager.
	public UploadAlarm() {
	}

	// you can use this constructor to create the alarm.
	// Just pass in the main activity as the context,
	// any extras you'd like to get later when triggered
	// and the timeout
	public UploadAlarm(Context context, Bundle extras, int timeoutInSeconds) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, UploadAlarm.class);
		intent.putExtra(REMINDER_BUNDLE, extras);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		//Every 30secs - 30*1000
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 30*1000, 
				pendingIntent);
//		Toast.makeText(context, "Upload Alarm is set", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "Upload Alarm went off", Toast.LENGTH_SHORT).show();
		//write the upload code here
		//check for network availability
		if(AppUtils.checkInternetConnection(context)){
			//check for completed task that are waiting for upload
			ArrayList<JTask> jTaskList = AppUtils.getLastSavedTabList(AppConstants.UPLOAD_PENDING, context);
			//upload files for each task
			if(jTaskList != null && jTaskList.size()>0){
				for(JTask task : jTaskList){
					Log.d(AppConstants.TAG, "Uploading: Task#" + task.getTaskId());
					if(task.getTaskType().equals("photo")){
						//perform photo upload
						AppUtils.uploadPhoto(context, task.getTaskId());
					} else if(task.getTaskType().equals("campusSensing")){
						//perform sensing file upload. final sensing duration stored in SensedDataFileLocation field
						int sensedDuration = 0;
						if(task.getSensedDataFileLocation()!=null && task.getSensedDataFileLocation().matches("[\\d]+"))
							sensedDuration = Integer.parseInt(task.getSensedDataFileLocation());
						AppUtils.uploadSensedData(context, task.getTaskStatus(), task.getTaskId(), sensedDuration);
					} else if(task.getTaskType().equals("bluetooth")){
						//perform BL sensing file upload
						int sensedDuration = 0;
						if(task.getSensedDataFileLocation()!=null && task.getSensedDataFileLocation().matches("[\\d]+"))
							sensedDuration = Integer.parseInt(task.getSensedDataFileLocation());
						AppUtils.uploadSensedData(context, task.getTaskStatus(), task.getTaskId(), sensedDuration);
					} else {
						//perform sensing file upload
						int sensedDuration = 0;
						if(task.getSensedDataFileLocation()!=null && task.getSensedDataFileLocation().matches("[\\d]+"))
							sensedDuration = Integer.parseInt(task.getSensedDataFileLocation());
						AppUtils.uploadSensedData(context, task.getTaskStatus(), task.getTaskId(), sensedDuration);
					}
				}
				//remove all upload pending tasks after completing upload
				AppUtils.removeUploadList(context);
			}
		}
	}
}
