package com.mcsense.app;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This receiver starts the service that monitors application usage on screen
 * unlock and shuts it down on screen lock.
 * 
 * @author gcardone
 * 
 */
public class AppMonitorScreenStatusReceiver extends BroadcastReceiver {

	private static final String LOGTAG = "AppMonitorScreenStatusReceiver";
	public static final AtomicBoolean isRegistered = new AtomicBoolean(false);

	public static boolean enableMonitor = false;

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d(LOGTAG, "Screen unlock detected");
			Intent i = new Intent(AppMonitorService.START_ACTION);
			i.setClass(context, AppMonitorService.class);
			context.startService(i);
		} else if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)) {
			Log.d(LOGTAG, "Screen lock detected");
			Log.d(LOGTAG, "Screen unlock detected");
			Intent i = new Intent(AppMonitorService.STOP_ACTION);
			i.setClass(context, AppMonitorService.class);
			context.stopService(i);
		}
	}
}
