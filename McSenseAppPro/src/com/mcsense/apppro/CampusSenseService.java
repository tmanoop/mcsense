package com.mcsense.apppro;

import java.sql.Timestamp;
import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mcsense.apppro.MyLocation.LocationResult;
import com.mcsense.json.JTask;

public class CampusSenseService extends Service {

	private static SensorManager mSensorManager;
	private static Sensor mAccelerometer;
	private static AcelSensor acelSensorListener;
	private static MyLocation myLocation;
	private JTask currentTask;
	private static int taskId;
	Context appContext;
	int sensingDuration;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		appContext = this.getApplicationContext();
		startAccelerometerSensing();
		myLocation = new MyLocation();
//		startGPSSensing();
	}

	@Override
	public void onDestroy() {
		Log.d("McSense", "stopping service");
//		showToast("sensing service stopped");
		if(mSensorManager!=null)
			mSensorManager.unregisterListener(acelSensorListener);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Log.d("McSense", "starting srvice");
//		showToast("sensing now");
		
		//get Task
		JTask jTaskObjInToClass = intent.getExtras().getParcelable("JTask");
		currentTask = jTaskObjInToClass;
		taskId = currentTask.getTaskId();
		
		startSensing();
		Thread thread = new Thread(new Runnable() {
	    	 public void run() {
	    		 	// add stopping code here. collect accel data for 5secs
					long startDisc = System.currentTimeMillis();
					long stopDisc = System.currentTimeMillis();
					while((stopDisc - startDisc) < 5000){
						stopDisc = System.currentTimeMillis();
					}
	    		 	stopSensing();
	    		}     
	    	 });
	    thread.start();
//		stopSensing();		
	}
	
	protected void stopSensing() {
		if(mSensorManager!=null)
			mSensorManager.unregisterListener(acelSensorListener,mAccelerometer);
		mSensorManager = null;
		stopService(new Intent(getApplicationContext(), CampusSenseService.class));
	}

	private void startSensing() {
		startAccelerometerSensing();
		acelSensorListener = new AcelSensor(getApplicationContext(),currentTask);
		mSensorManager.registerListener(acelSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		myLocation.getLocation(this, locationResult);
	}

	private void startAccelerometerSensing() {
		if(mSensorManager==null){
			mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
			mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
        
	}
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	public LocationResult locationResult = (new LocationResult(){
	    @Override
	    public void gotLocation(final Location loc){
	        //Got the location!
	    	Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
//			String gpsVals = "My current location is: " +	"\n Latitude = " + loc.getLatitude() +	"\n Longitude = " + loc.getLongitude();
	    	String gpsVals = "";
	    	if(loc!=null)
				gpsVals = "Timestamp:"+currentTimestamp+",TaskId:"+taskId+",ProviderId:"+AppConstants.providerId+",Latitude:" + loc.getLatitude() +	",Longitude:" + loc.getLongitude()+ ",Speed:" + loc.getSpeed()+" \n";
			
			AppUtils.writeToFile(getApplicationContext(), gpsVals,"sensing_file"+taskId);
//			AppUtils.writeToXFile(gpsVals,"sensing_file"+taskId);
			AppConstants.gpsLocUpdated = true;
	    }
	});
}
