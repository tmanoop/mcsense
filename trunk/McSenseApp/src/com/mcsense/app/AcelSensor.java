package com.mcsense.app;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.widget.Toast;

public class AcelSensor implements SensorEventListener {
	Context context;
	List<String> result;
	
	public AcelSensor(Context cntxt){
		context = cntxt;
		result = new LinkedList<String>();
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
    	float y = event.values[1];
    	float z = event.values[2];

    	Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    	
    	String acelVals = "Timestamp:"+currentTimestamp+",x:"+x+",y:"+y+",z:"+z+"; \n";
    	result.add(acelVals);
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
