package com.mcsense.app;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AppMonitorService extends Service {

	public static final String START_ACTION = "com.mcsense.app.AppMonitorService.START";
	public static final String STOP_ACTION = "com.mcsense.app.AppMonitorService.STOP";
	private static final String LOGTAG = "ApplicationMonitoring";
	public static final int ACTIVITY_POLL_INTERVAL_MSEC = 1000;
	
	private AtomicBoolean mRunMonitor;
	private ActivityManager mActivityManager;
	private Thread mMonitoringThread;

	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	
	@Override
	public void onCreate() {
		Log.i(LOGTAG, "onCreate()");
		try {
			// Toast.makeText(this, "ActivityMonitor started", Toast.LENGTH_SHORT).show();
			mRunMonitor = new AtomicBoolean(true);
			mActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
			mMonitoringThread = new Thread(new TopActivityPoller(), "ActivityMonitorService monitoring thread");
			mMonitoringThread.start();

		} catch (Exception e) {
			Log.e(LOGTAG, e.getMessage());
			e.printStackTrace();
			stopSelf();
		}

	}

	@Override
	public void onDestroy() {
		mRunMonitor.set(false);
		try {
			mMonitoringThread.join(3000);
		} catch (InterruptedException e) {
			Toast.makeText(getApplicationContext(),
					String.format("Error while shutting down ActivityMonitor: %s", e.getLocalizedMessage()),
					Toast.LENGTH_SHORT).show();
		}
		// Toast.makeText(this, "ActivityMonitor Service Destroyed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// If we get killed, after returning from here, restart
		return START_STICKY;

	}


		
	/**
	 * Gets the foreground task.
	 * 
	 * @return The foreground task or null on exception.
	 */
	private RunningTaskInfo getForegroundTask() {
		ActivityManager.RunningTaskInfo result = null;
		List<ActivityManager.RunningTaskInfo> runningTasks = mActivityManager.getRunningTasks(1);
		if (runningTasks.size() > 0) {
			result = runningTasks.get(0);
		}
		return result;
	}


	/**
	 * This thread polls for the top running activity.
	 */
	private class TopActivityPoller implements Runnable {

		@Override
		public void run() {

			// run while mRunMonitoring is true
			while (mRunMonitor.get()) {
				// get the foreground application and activity
				RunningTaskInfo fgApp = getForegroundTask();
				ComponentName fgAct = null;
				if (null != fgApp) {
					fgAct = fgApp.topActivity;
				}

				/*
				 * Log the foreground application and activity if: a) we
				 * successfully got the foreground application b) we
				 * successfully got the foreground activity
				 */
				if (fgApp != null && fgAct != null) {
					String activeApp = fgApp.baseActivity.getPackageName();
					String activeAct = fgAct.getClassName();
					String logString = String.format("Active app: %s active act: %s", activeApp, activeAct); 
					Log.i(LOGTAG, logString);
					/* TODO: insert here code to write active app and activity to file */
				}

				sleepForMillis(ACTIVITY_POLL_INTERVAL_MSEC);
			}
		}


		private void sleepForMillis(long duration) {
			long endTime = System.currentTimeMillis() + duration;
			while (System.currentTimeMillis() < endTime) {
				try {
					Thread.sleep(endTime - System.currentTimeMillis());
				} catch (InterruptedException e) {
					if (!mRunMonitor.get())
						break;
				}
			}
		}
	}

}
