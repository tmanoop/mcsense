package com.mcsense.app;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class HardwareData {
	
	private boolean mShouldTurnOffWifi;
	private WifiManager mWifiManager;
	private WifiDataAvailableReceiver mWifiDataReceiver;
	
	public void startWifiScan(Context context) {
		
		if (null == mWifiDataReceiver) {
			mWifiDataReceiver = new WifiDataAvailableReceiver();
		}
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
				String srStr = String.format("BSSID:%s;SSID:%s;capabilities:%s;freq:%d;level:%d", sr.BSSID.replace(':', '-'), sr.SSID, sr.capabilities, sr.frequency, sr.level);
				Log.d("HardwareData", "Wifi scan: " + srStr);
				/* TODO: insert srStr into data dump */
			}
			if (mShouldTurnOffWifi) {
				mWifiManager.setWifiEnabled(false);
			}
		}
	}
}
