package com.mcsense.app;

import java.util.ArrayList;

import com.mcsense.json.JTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {
	
	private static final int NOTIFICATION_EX = 1;
	private NotificationManager notificationManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		notificationManager.cancel(NOTIFICATION_EX);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.d("McSense", "starting notification service");
		super.onStart(intent, startId);
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				
				//check time and stop service
				// Get current time
				 long start = System.currentTimeMillis();
				// Get elapsed time in milliseconds
				 long elapsedTimeMillis = System.currentTimeMillis()-start;
				// Get elapsed time in minutes
				 float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
				 float nextTrigger = 5;
				 while(true){
					 elapsedTimeMillis = System.currentTimeMillis()-start;
					 
					 elapsedTimeMin = elapsedTimeMillis/(60*1000F);
					 //trigger sensing at every min
					 if(elapsedTimeMin - nextTrigger >= 0){
						// notify
						ArrayList<JTask> taskList = AppUtils.loadTasks("P",
								getApplicationContext());
						if (taskList.size() > 0
								&& !taskList.get(0).getTaskDescription()
										.equals("No Available Tasks")) {
							Log.d(AppConstants.TAG, "Notify at elapsed time: "+elapsedTimeMin);
							notifyUser();
						}

						nextTrigger++;
					 }
				 }

			}
		});
		t.start();
	}

	protected void notifyUser() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.ic_launcher;
	    CharSequence tickerText = "McSense";
	    long when = System.currentTimeMillis();
	
	    Notification notification = new Notification(icon, tickerText, when);
	
	    Context context = getApplicationContext();
	    CharSequence contentTitle = "Mcsense";
	    CharSequence contentText = "New McSense task is available.";
	    Intent notificationIntent = new Intent(this, Main.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	
	    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
	    long[] vibrate = {0,100,200,300};
	    notification.vibrate = vibrate;
	    notificationManager.notify(NOTIFICATION_EX, notification);
	}
}
