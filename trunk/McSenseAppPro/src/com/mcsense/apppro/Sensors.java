package com.mcsense.apppro;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class Sensors extends MapActivity implements SensorEventListener {
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;
    private static LocationManager mlocManager;
    private static LocationListener mlocListener;
    private static final int NOTIFICATION_EX = 1;
	private NotificationManager notificationManager;
    
    public Sensors() {
        
    }

    TextView acelValues;
    TextView gpsValues;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startService(new Intent(this, SensingService.class));
		
        startGPSSensing();
		
		startAccelerometerSensing();

		loadNotificationIcon();
//        View v = null;
//        LayoutInflater vi = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        v = vi.inflate(R.layout.sensor, null);
        
		setContentView(R.layout.sensor);
        
        TableLayout tb = (TableLayout) findViewById(R.id.tableLayoutMap); //new TableLayout(this);
        
        TextView acelTitle = new TextView(this);
        acelTitle.setText("Accelerometer readings: ");
        acelTitle.setTextColor(Color.YELLOW);
        acelValues = new TextView(this);

        tb.addView(acelTitle,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tb.addView(acelValues,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        TextView gpsTitle = new TextView(this);
        gpsTitle.setText("GPS readings: ");
        gpsTitle.setTextColor(Color.YELLOW);
        gpsValues = new TextView(this);
        
        tb.addView(gpsTitle,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tb.addView(gpsValues,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
//        startMaps();
//        MapView myMap = new MapView(getApplicationContext(), "0ouC8wAL11PTw8tuk-Iuj5cnjys-ssv_72BbeZQ");
//		myMap.displayZoomControls(true);
//        
//		MyLocationOverlay myLocOverlay = new MyLocationOverlay(this, myMap);
//		myLocOverlay.enableMyLocation();
//		myMap.getOverlays().add(myLocOverlay);
//		
        
       
        
//        setContentView(R.layout.sensor);
        MapView myMap = (MapView) findViewById(R.id.map_view);       
        myMap.setBuiltInZoomControls(true);
        myMap.setLayoutParams(new LayoutParams(500, 400));

        
        MyLocationOverlay myLocOverlay = new MyLocationOverlay(this, myMap);
		myLocOverlay.enableMyLocation();
		myMap.getOverlays().add(myLocOverlay);
		myMap.getController().setCenter(myMap.getMapCenter());
		myMap.getController().animateTo(myMap.getMapCenter());
		myMap.getController().setZoom(18);
//        tb.addView(myMap,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
//        addContentView(tb,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
	}
    
    private void loadNotificationIcon() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.ic_launcher;
	    CharSequence tickerText = "McSense";
	    long when = System.currentTimeMillis();
	
	    Notification notification = new Notification(icon, tickerText, when);
	
	    Context context = getApplicationContext();
	    CharSequence contentTitle = "Mcsense";
	    CharSequence contentText = "Sense the world";
	    Intent notificationIntent = new Intent(this, Main.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	
	    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	
	    notificationManager.notify(NOTIFICATION_EX, notification);
	}
    
    private void startMaps() {
    	Intent i = new Intent(getApplicationContext(), GMapsActivity.class);
        startActivity(i);
	}



	private void startAccelerometerSensing() {
    	mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
	}



	private void startGPSSensing() {
		
		enableGPS();
		
    	mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		mlocListener = new MyInnerLocationListener(getApplicationContext());
		
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}

	private void turnGPSOn(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}

	private void turnGPSOff(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}

	private void enableGPS() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Switch on GPS?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						turnGPSOn();
					}
				});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("McSense App cannot sense with out GPS!!");
						onBackPressed();
					}
				});

		alert.show();
	}


	@Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, SensingService.class));
        mSensorManager.unregisterListener(this);
        mlocManager.removeUpdates(mlocListener);
        if (isFinishing()) {
	        notificationManager.cancel(NOTIFICATION_EX);
	    }
        turnGPSOff();
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
    	
    	String acelVals = "x: "+x+"\n y: "+y+"\n z: "+z;
    	
    	acelValues.setText(acelVals);
    	writeToFile(acelVals,"accel_file");
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

	private void writeToFile(String values, String fileName) {
		String FILENAME = fileName;//"accel_file"
		String acelValues = values;

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
	
	class MyInnerLocationListener implements LocationListener {
		Context appContext;
		public MyInnerLocationListener(Context context) {
			appContext = context;
		}
		
		@Override
		public void onLocationChanged(Location loc) {
			loc.getLatitude();

			loc.getLongitude();

			String gpsVals = "My current location is: " +	"\n Latitude = " + loc.getLatitude() +	"\n Longitude = " + loc.getLongitude();
			
			gpsValues.setText(gpsVals);
	    	writeToFile(gpsVals,"gps_file");
			
//			Toast.makeText(appContext,	gpsVals,	Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			Toast.makeText(appContext,"Gps Disabled",Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(appContext,"Gps Enabled",Toast.LENGTH_SHORT ).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
