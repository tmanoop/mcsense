package com.mcsense.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class Sensors extends Activity implements SensorEventListener {
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;

    public Sensors() {
        
    }

    TextView textView;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        TextView textTitle = new TextView(this);
        textTitle.setText("Accelerometer readings: ");
//        setContentView(textTitle);
        textView = new TextView(this);
//        setContentView(textView);
        TableLayout tb = new TableLayout(this);
        tb.addView(textTitle,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tb.addView(textView,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addContentView(tb,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
    	textView.setText("x: "+x+" y: "+y+" z: "+z);
    	writeToFile(x,y,z);
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

	private void writeToFile(float x, float y, float z) {
		String FILENAME = "accel_file";
		String acelValues = "x: "+x+"\n y: "+y+"\n z: "+z;

		try {
			//writing
			FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(acelValues.getBytes());
			
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Sensor> getSupportedSensors() {
		return mSensorManager.getSensorList(Sensor.TYPE_ALL);
	}
}
