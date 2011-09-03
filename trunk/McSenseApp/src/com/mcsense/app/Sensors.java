package com.mcsense.app;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class Sensors extends Activity implements SensorEventListener {
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;

    public Sensors() {
        
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
	}
    
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
    
    @Override
    public void onSensorChanged(SensorEvent event) {
    	float x = event.values[0];
    	float y = event.values[1];
    	float z = event.values[2];
    	
    	//showToast("x: "+x+" y: "+y+" z: "+z);
    	
    	/**
    	 * // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = (float) 0.8;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.data[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.data[2];

        linear_acceleration[0] = event.data[0] - gravity[0];
        linear_acceleration[1] = event.data[1] - gravity[1];
        linear_acceleration[2] = event.data[2] - gravity[2];
    	 */
    }

	public static List<Sensor> getSupportedSensors() {
		return mSensorManager.getSensorList(Sensor.TYPE_ALL);
	}
}
