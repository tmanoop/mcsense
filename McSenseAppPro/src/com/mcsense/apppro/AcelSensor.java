package com.mcsense.apppro;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.widget.Toast;

public class AcelSensor implements SensorEventListener {
	Context context;
	private List<String> result;
	ReentrantLock lock;
	
	public AcelSensor(Context cntxt){
		context = cntxt;
		result = new LinkedList<String>();
		lock = new ReentrantLock();
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
    	lock.lock();
    	result.add(acelVals);
    	lock.unlock();
	}
	
	public List<String> getResult() {
		lock.lock();
		List<String> clonedRes = new LinkedList<String>(result);
		lock.unlock();
		return clonedRes;
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
