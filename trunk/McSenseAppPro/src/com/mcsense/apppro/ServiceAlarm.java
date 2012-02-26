package com.mcsense.apppro;

import java.util.ArrayList;
import java.util.Calendar;

import com.mcsense.app.R;
import com.mcsense.json.JTask;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ServiceAlarm extends BroadcastReceiver {
	
	private static final int NOTIFICATION_EX = 1;
	private NotificationManager notificationManager;
	private static final String GPS_NOTIFICATION = "Enable GPS for McSense App to complete the sensing task.";
	private static final String BL_NOTIFICATION = "Enable Bluetooth for McSense App to complete the sensing task.";
	
	private final String REMINDER_BUNDLE = "ServiceReminderBundle";
	
	// this constructor is called by the alarm manager.
	public ServiceAlarm() {
	}

	// you can use this constructor to create the alarm.
	// Just pass in the main activity as the context,
	// any extras you'd like to get later when triggered
	// and the timeout
	public ServiceAlarm(Context context, Bundle extras, int timeoutInSeconds) {
		Log.d("McSense", "ServiceAlarm started.");
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, ServiceAlarm.class);
		intent.putExtra(REMINDER_BUNDLE, extras);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		//Every 1 min - 60*1000
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 60*1000, 
				pendingIntent);
//		Toast.makeText(context, "Service Alarm is set", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {	
//		Toast.makeText(context, "Service Alarm went off", Toast.LENGTH_SHORT).show();
		//check if there are any In Progress Sensing tasks for the current day.
		ArrayList<JTask> acceptedTaskList = AppUtils.loadTasks("IP",context);
		Log.d("McSense", "load accepted tasks : "+acceptedTaskList.size());
		//get last saved suspended list
		ArrayList<JTask> suspendedList = AppUtils.getLastSavedTabList(AppConstants.SUSPENDED, context);
		
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		Log.d(AppConstants.TAG, "Hour: "+currentHour);
		//check for available accepted list
		//then check if there is any suspended list
		//then start in-progress tasks anytime and long-term tasks only after 12 noon
		if(acceptedTaskList != null && acceptedTaskList.size()>0){
			for(JTask acptTask : acceptedTaskList){
				if(suspendedList == null || !suspendedList.contains(acptTask)){
					if(!(acptTask.getParentTaskId() != 0 && currentHour<12)){
						if(acptTask.getTaskType().equals("campusSensing")){
							//stop the sensing service.  This alarm is set to do this every hour.
//							context.stopService(new Intent(context, SensingService.class));
							//restart it for this task if not in suspended list
//							iniSensingService(context, acptTask);
							if(AppUtils.isGPSEnabled(context)){
								if(!AppUtils.isAccelGPSAlarmExist(context)){
									Bundle bundle = new Bundle();
									// add extras here..
									ArrayList<JTask> taskList = new ArrayList();
									taskList.add(acptTask);
									bundle.putParcelableArrayList("task", taskList);
									AccelGPSAlarm alarm = new AccelGPSAlarm(context, bundle, 30);
									Log.d("McSense", "AccelGPSAlarm started.");
								}
							} else {
								//stop bluetooth alarm
								AppUtils.stopAccelGPSAlarm(context);
								notifyUser(context,GPS_NOTIFICATION);
								AppUtils.addToSuspendedList(acptTask, context);
							}
						} else if(acptTask.getTaskType().equals("bluetooth")){
							if(AppUtils.isBluetoothEnabled(context)){
								if(!AppUtils.isBluetoothAlarmExist(context)){
									Bundle bundle = new Bundle();
									// add extras here..
									ArrayList<JTask> taskList = new ArrayList();
									taskList.add(acptTask);
									bundle.putParcelableArrayList("task", taskList);
									BluetoothAlarm alarm = new BluetoothAlarm(context, bundle, 30);
									Log.d("McSense", "BluetoothAlarm started.");
								} 
							} else {
								//stop bluetooth alarm
								AppUtils.stopBluetoothAlarm(context);
								notifyUser(context,BL_NOTIFICATION);
								AppUtils.addToSuspendedList(acptTask, context);
							}
						}
					}
				} else{
					//upload data if task already expired. this scenario occurs, before BL task 5min period occurs after task expired.
					if(AppUtils.hasTaskExpired(context, acptTask)){
						if(acptTask.getTaskType().equals("campusSensing")){
							AppUtils.uploadCampusSensedData(context,acptTask);
						} else if(acptTask.getTaskType().equals("bluetooth")){
							AppUtils.uploadBluetoothSensedData(context,acptTask);
						}
					}
				}
			}
		}
	}

	protected void iniSensingService(Context context, JTask currentTask) {
		LocationManager mlocManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		boolean isGPS = mlocManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
		if(!isGPS){
			Toast.makeText(context, "Enable GPS for McSense App to complete the sensing task!!", Toast.LENGTH_SHORT).show();
			notifyUser(context,GPS_NOTIFICATION);
//			enableGPSSettings(context);
		} else{
			Log.d("McSense", "Sensing service started.");
			Intent intent = new Intent(context, SensingService.class);
			intent.putExtra("JTask", currentTask);
			context.startService(intent);
		}
	}

	private void enableGPSSettings(Context context) {
		final Context ctxt = context;
		AlertDialog.Builder alert = new AlertDialog.Builder(ctxt);
		alert.setTitle("Switch on GPS?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
//						ctxt.TaskActivity.this.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
					}
				});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
//						showToast("McSense App cannot sense with out GPS!!");
						Toast.makeText(ctxt, "McSense App cannot sense with out GPS!!", Toast.LENGTH_SHORT).show();
						ctxt.stopService(new Intent(ctxt, SensingService.class));
					}
				});

		alert.show();
	}
	
	protected void notifyUser(Context context, String message) {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.ic_launcher;
	    CharSequence tickerText = "McSense";
	    long when = System.currentTimeMillis();
	
	    Notification notification = new Notification(icon, tickerText, when);
	
//	    Context context = getApplicationContext();
	    CharSequence contentTitle = "Mcsense";
	    CharSequence contentText = message;
	    Intent notificationIntent = new Intent(context, Main.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	
	    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
	    notification.flags = Notification.FLAG_AUTO_CANCEL;
//	    long[] vibrate = {0,100,200,300};
//	    notification.vibrate = vibrate;
	    notificationManager.notify(NOTIFICATION_EX, notification);
	}
}
