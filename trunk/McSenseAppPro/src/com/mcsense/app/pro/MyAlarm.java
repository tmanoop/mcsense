package com.mcsense.app.pro;

import java.util.ArrayList;
import java.util.Calendar;

import com.mcsense.app.R;
import com.mcsense.json.JTask;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyAlarm extends BroadcastReceiver {
	private final String REMINDER_BUNDLE = "MyReminderBundle";

	private static final int NOTIFICATION_EX = 1;
	private NotificationManager notificationManager;

	// this constructor is called by the alarm manager.
	public MyAlarm() {
	}

	// you can use this constructor to create the alarm.
	// Just pass in the main activity as the context,
	// any extras you'd like to get later when triggered
	// and the timeout
	public MyAlarm(Context context, Bundle extras, int timeoutInSeconds) {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MyAlarm.class);
		intent.putExtra(REMINDER_BUNDLE, extras);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, timeoutInSeconds);
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 60*1000, 
				pendingIntent);
//		Toast.makeText(context, "Notification Alarm is set", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onReceive(Context context, Intent arg1) {
		try {
			String PENDING = "P";
			SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, 0);
			String notify = settings.getString("notify", "");
			if(!notify.equals("0")){
//				Toast.makeText(context, "Notification Alarm went off", Toast.LENGTH_SHORT).show();
				ArrayList<JTask> savedList = AppUtils.getLastSavedTabList(PENDING, context);
				
				ArrayList<JTask> taskList = AppUtils.loadTasks(PENDING, context);
				if (taskList.size() > 0
						&& !taskList.get(0).getTaskDescription()
								.equals("No Available Tasks")) {
					taskList.removeAll(savedList);
					if(taskList.size()>0)
						notifyUser(context);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void notifyUser(Context context) {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.ic_launcher;
	    CharSequence tickerText = "McSense";
	    long when = System.currentTimeMillis();
	
	    Notification notification = new Notification(icon, tickerText, when);
	
//	    Context context = getApplicationContext();
	    CharSequence contentTitle = "Mcsense";
	    CharSequence contentText = "New McSense task is available.";
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
