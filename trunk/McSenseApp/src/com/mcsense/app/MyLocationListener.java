//package com.mcsense.app;
//
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.widget.Toast;
//
//public class MyLocationListener implements LocationListener {
//	Context appContext;
//	public MyLocationListener(Context context) {
//		appContext = context;
//	}
//	
//	@Override
//	public void onLocationChanged(Location loc) {
//		loc.getLatitude();
//
//		loc.getLongitude();
//
//		String Text = "My current location is: " +	"Latitud = " + loc.getLatitude() +	"Longitud = " + loc.getLongitude();
//		
//		acelValues.setText(Text);
//    	writeToFile(acelVals,"accel_file");
//		
//		Toast.makeText(appContext,	Text,	Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public void onProviderDisabled(String arg0) {
//		Toast.makeText(appContext,"Gps Disabled",Toast.LENGTH_SHORT ).show();
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//		Toast.makeText(appContext,"Gps Enabled",Toast.LENGTH_SHORT ).show();
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//		
//	}
//
//}
