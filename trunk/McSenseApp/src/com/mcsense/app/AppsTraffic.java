package com.mcsense.app;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.util.Log;

public class AppsTraffic {

	private static final String TAG = "AppsTraffic";

	/**
	 * Function that dumps network traffic of the whole device and of the single
	 * applications on file.
	 * 
	 * @param context
	 */
	public void dumpTrafficStats(Context context) {
		TrafficSnapshot snapshot = new TrafficSnapshot(context);
		String deviceTraffic = String.format("TrafficRecord device;tx:%d;rx:%d", snapshot.device.tx, snapshot.device.rx);
		Log.d(TAG, deviceTraffic);
		/* TODO: write deviceTraffic to file */
		
		for (Integer uid : snapshot.apps.keySet()) {
			TrafficRecord tr = snapshot.apps.get(uid);
			String appTraffic = String.format("TrafficRecord app:%s;tx:%d;rx:%d", tr.tag, tr.tx, tr.rx);
			Log.d(TAG, appTraffic);
			/* TODO: write appTraffic to file */
		}
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
