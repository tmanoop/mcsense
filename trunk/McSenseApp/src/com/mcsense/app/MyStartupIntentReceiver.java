package com.mcsense.app;

import java.util.ArrayList;

import com.mcsense.json.JTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
 
 
 
public class MyStartupIntentReceiver extends BroadcastReceiver  {
 
	@Override
	public void onReceive(Context context, Intent intent) {
	    // call pending sensing service here ....
		SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
//		String taskIDString = settings.getString("taskID", "");
//		String status = settings.getString("status", "");
//		//get last saved suspended list
//		ArrayList<JTask> suspendedList = AppUtils.getLastSavedTabList(AppConstants.SUSPENDED, context);
//		int taskId = 0;
////		int sensingTaskID = 0;
//		if(!taskIDString.equals(""))
//			taskId = Integer.parseInt(taskIDString);
//		if(status.equals("IP")){
//			boolean suspend = false;
//			for(JTask temp : suspendedList){
//				if(temp.getTaskId() == taskId)
//					suspend = true;
//			}
//			if(suspend != true)
//				iniSensingService(context);
//		}
		
		//start notification service always on reboot
//		iniNotificationService(context);
		AppUtils.loadLoginUser(context);
		if(!AppUtils.isAlarmExist(context))
			iniNotificationAlarm(context);
		if(!AppUtils.isUploadAlarmExist(context))
			iniUploadAlarm(context);
		if(!AppUtils.isServiceAlarmExist(context))
			iniServiceAlarm(context);
		if (!AppUtils.isScreenStatusReceiverRegistered(true))
			iniScreenStatusReceiver(context);
	}
	
	private void iniScreenStatusReceiver(Context context) {
		AppMonitorScreenStatusReceiver amssr = new AppMonitorScreenStatusReceiver();
		context.getApplicationContext().registerReceiver(amssr, new IntentFilter(Intent.ACTION_SCREEN_ON));
		context.getApplicationContext().registerReceiver(amssr, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	
	private void iniServiceAlarm(Context context) {
		Bundle bundle = new Bundle();
		// add extras here..
		ServiceAlarm alarm = new ServiceAlarm(context, bundle, 120);
	}

	private void iniUploadAlarm(Context context) {
		Bundle bundle = new Bundle();
		// add extras here..
		UploadAlarm alarm = new UploadAlarm(context, bundle, 30);
	}

	private void iniNotificationAlarm(Context context) {
		Bundle bundle = new Bundle();
		// add extras here..
		MyAlarm alarm = new MyAlarm(context, bundle, 30);
	}
	
	private void iniNotificationService(Context context) {
		if(!AppUtils.isNotifiyServiceRunning(context)){
			Intent intentNotify = new Intent(context, NotificationService.class);
//			intent.putExtra("JTask", currentTask);
			context.startService(intentNotify);
		}
	}

	protected void loadApp(Context context) {
        Intent i = new Intent(context, Main.class);  
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);  
	}
 
	protected void iniSensingService(Context context) {
		LocationManager mlocManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		boolean isGPS = mlocManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
//		if(!isGPS)
//			enableGPSSettings(context);
//		else{
			Intent intent = new Intent(context, SensingService.class);
//			intent.putExtra("JTask", currentTask);
			context.startService(intent);
//		}
	}
	
	private void enableGPSSettings(final Context context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Switch on GPS?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
					}
				});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("McSense pending task cannot sense with out GPS!!",context);
						context.stopService(new Intent(context, SensingService.class));
					}
				});

		alert.show();
	}
	
	public void showToast(String msg, Context context) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}