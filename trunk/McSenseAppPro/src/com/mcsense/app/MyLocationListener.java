package com.mcsense.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import com.mcsense.json.JTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

class MyLocationListener implements LocationListener {
	Context context;
	JTask currentTask;
	public MyLocationListener(Context appContext, JTask currTask) {
		context = appContext;
		currentTask = currTask;
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		loc.getLatitude();

		loc.getLongitude();

		Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
//		String gpsVals = "My current location is: " +	"\n Latitude = " + loc.getLatitude() +	"\n Longitude = " + loc.getLongitude();
		String gpsVals = "Timestamp:"+currentTimestamp+",TaskId:"+currentTask.getTaskId()+",ProviderId:"+currentTask.getProviderPersonId()+",Latitude:" + loc.getLatitude() +	",Longitude:" + loc.getLongitude()+ ",Speed:" + loc.getSpeed()+" \n";
		
		AppUtils.writeToFile(context, gpsVals,"sensing_file"+currentTask.getTaskId());
//		AppUtils.writeToXFile(gpsVals,"sensing_file"+currentTask.getTaskId());
		AppConstants.gpsLocUpdated = true;
//		Toast.makeText(appContext,	gpsVals,	Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Toast.makeText(context,"Gps Disabled",Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(context,"Gps Enabled",Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

}