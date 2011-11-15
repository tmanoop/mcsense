package com.mcsense.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.widget.Toast;

import com.mcsense.json.JTask;

public class AcelSensor implements SensorEventListener {
	Context context;
	JTask currentTask;
	String taskIDString;
	public AcelSensor(Context cntxt, JTask currTask){
		context = cntxt;
		currentTask = currTask;
		SharedPreferences settings = cntxt.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		taskIDString = settings.getString("taskID", "");
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
    	float y = event.values[1];
    	float z = event.values[2];
    	
    	Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
    	
    	String acelVals = "Timestamp:"+currentTimestamp+",TaskId:"+taskIDString+",ProviderId:"+AppConstants.providerId+",x:"+x+",y:"+y+",z:"+z+"; \n";
    	
    	AppUtils.writeToFile(context, acelVals,"sensing_file"+taskIDString);
//    	AppUtils.writeToXFile(acelVals,"sensing_file"+currentTask.getTaskId());
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}
