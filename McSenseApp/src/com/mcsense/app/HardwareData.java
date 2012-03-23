package com.mcsense.app;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

public class HardwareData {

	private static final String TAG = "HardwareData";
	
	private BatteryReceiver mBatteryReceiver;
	private boolean mShouldTurnOffWifi;
	private WifiManager mWifiManager;
	private WifiDataAvailableReceiver mWifiDataReceiver;

	/**
	 * Turns on wifi interface (if it is off) and start a wifi scan. WiFi
	 * interface will be turned off after scan if it was off.
	 * 
	 * @param context
	 *            Application context.
	 */
	public void startWifiScan(Context context) {

		mWifiDataReceiver = new WifiDataAvailableReceiver();
		context.registerReceiver(mWifiDataReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		if (null == mWifiManager) {
			mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		if (null != mWifiManager) {
			if (!mWifiManager.isWifiEnabled()) {
				mWifiManager.setWifiEnabled(true);
				mShouldTurnOffWifi = true;
			} else {
				mShouldTurnOffWifi = false;
			}
			mWifiManager.startScan();
		}
	}

	private class WifiDataAvailableReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			context.unregisterReceiver(this);
			List<ScanResult> scanResults = mWifiManager.getScanResults();
			for (ScanResult sr : scanResults) {
				String srStr = String.format("BSSID:%s;SSID:%s;capabilities:%s;freq:%d;level:%d",
						sr.BSSID.replace(':', '-'), sr.SSID, sr.capabilities, sr.frequency, sr.level);
				Log.d(TAG, "Wifi scan: " + srStr);
				/* TODO: insert srStr into data dump */
			}
			if (mShouldTurnOffWifi) {
				mWifiManager.setWifiEnabled(false);
			}
		}
	}

	/**
	 * Starts a one-shot battery measurement. Battery level is asynchronously
	 * provided by Android. This method listens for the first battery level
	 * change and then stops.
	 * 
	 * @param context
	 */
	public void startBatteryMonitoring(Context context) {
		mBatteryReceiver = new BatteryReceiver();
		context.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}

	private class BatteryReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			context.unregisterReceiver(this);
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
			int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			String statusStr = null;
			switch (status) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				statusStr = "charging";
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				statusStr = "discharging";
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				statusStr = "full";
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				statusStr = "not charging";
				break;
			default:
				statusStr = "unknown";
			}

			String pluggedStr = null;
			switch (plugged) {
			case BatteryManager.BATTERY_PLUGGED_AC:
				pluggedStr = "ac";
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				pluggedStr = "usb";
				break;
			default:
				pluggedStr = "no";
			}
			String stBatt = String.format("level:%d;scale:%d;temp:%d;voltage:%d,plugged:%s,status:%s", level, scale, temp, voltage, pluggedStr, statusStr);
			Log.d(TAG, stBatt);
			/* TODO: insert stBatt into data dump */
		}
	}
}
