package com.mcsense.app;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.mcsense.json.JTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

public class AppsTraffic {

	private static final String TAG = "AppsTraffic";
	private static final String LAST_INSTALL_CHECK = "LastInstalledPackagesCheck";

	/**
	 * Function that dumps network traffic of the whole device and of the single
	 * applications on file.
	 * 
	 * @param context
	 */
	public void dumpTrafficStats(Context context, JTask currentTask) {
		
		Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
		String filename = "sensing_file" + currentTask.getTaskId();

		SharedPreferences settings = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
		String lastInstalledPkgs = settings.getString(LAST_INSTALL_CHECK, "1970-01-01 01:00:00");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date lastInstallCheck = sdf.parse(lastInstalledPkgs);
			Calendar lastCheckCal = GregorianCalendar.getInstance();
			lastCheckCal.setTime(lastInstallCheck);
			Calendar nowCal = GregorianCalendar.getInstance();
			long difInDays = ((nowCal.getTime().getTime() - lastCheckCal.getTime().getTime())/(1000*60*60*12));
			if (difInDays > 1) {
				getInstalledPackages(context, currentTimestamp, filename);
				SharedPreferences.Editor settingsEditor = settings.edit();
				String newLastCheck = sdf.format(nowCal.getTime());
				settingsEditor.putString(LAST_INSTALL_CHECK, newLastCheck);
				settingsEditor.commit();
			}
		} catch (ParseException e) {
			Log.e("AppsTraffic", e.getMessage());
		}

		TrafficSnapshot snapshot = new TrafficSnapshot(context);
		String deviceTraffic = String.format("Timestamp:" + currentTimestamp + ",TrafficRecord device;tx:%d;rx:%d"
				+ " \n", snapshot.device.tx, snapshot.device.rx);
		Log.d(TAG, deviceTraffic);
		AppUtils.writeToFile(context, deviceTraffic, filename);

		List<String> result = new LinkedList<String>();
		for (Integer uid : snapshot.apps.keySet()) {
			TrafficRecord tr = snapshot.apps.get(uid);
			if (tr.rx == -1 && tr.tx == -1) {
				// Skip empty traffic records
				continue;
			}
			String appTraffic = String.format("Timestamp:%s;TrafficRecord app:%s;tx:%d;rx:%d\n",
					currentTimestamp.toString(), tr.tag, tr.tx, tr.rx);
			result.add(appTraffic);
			Log.d(TAG, appTraffic);
		}
		AppUtils.writeListToFile(context, result, filename);
	}

	private void getInstalledPackages(Context context, Timestamp currentTimestamp, String filename) {
		final PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> result = new LinkedList<String>();
		for (ApplicationInfo packageInfo : packages) {
			String installedPkg = String.format("Timestamp:%s;InstalledPkg:%s\n", currentTimestamp.toString(), packageInfo.packageName);
			result.add(installedPkg);
		}
		AppUtils.writeListToFile(context, result, filename);
	}

	private class TrafficSnapshot {
		TrafficRecord device = null;
		HashMap<Integer, TrafficRecord> apps = new HashMap<Integer, TrafficRecord>();

		TrafficSnapshot(Context ctxt) {
			device = new TrafficRecord();

			HashMap<Integer, String> appNames = new HashMap<Integer, String>();

			for (ApplicationInfo app : ctxt.getPackageManager().getInstalledApplications(0)) {
				appNames.put(app.uid, app.packageName);
			}

			for (Integer uid : appNames.keySet()) {
				apps.put(uid, new TrafficRecord(uid, appNames.get(uid)));
			}
		}
	}

	private class TrafficRecord {
		long tx = 0;
		long rx = 0;
		String tag = null;

		TrafficRecord() {
			tx = TrafficStats.getTotalTxBytes();
			rx = TrafficStats.getTotalRxBytes();
		}

		TrafficRecord(int uid, String tag) {
			tx = TrafficStats.getUidTxBytes(uid);
			rx = TrafficStats.getUidRxBytes(uid);
			this.tag = tag;
		}
	}
}
