package com.mcsense.app;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mcsense.app.MyLocation.LocationResult;
import com.mcsense.json.JTask;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

public class BluetoothService extends Service {
	
	private JTask currentTask;
    private BluetoothAdapter mBluetoothAdapter = null;
    private List<BluetoothDevice> mNewBluetoothDevices = null;
    private String currentLocation = "";

	private final String REMINDER_BUNDLE = "BluetoothServiceReminderBundle";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//initialize BL stuff
		initializeBluetoothStuff(getApplicationContext());
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        getApplicationContext().unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
//		Toast.makeText(getApplicationContext(), "Bluetooth Scan started", Toast.LENGTH_SHORT).show();
		
		//get Task
		JTask jTaskObjInToClass = intent.getExtras().getParcelable("JTask");
		currentTask = jTaskObjInToClass;
		
		//get GPS location
		Runnable start = new Runnable( ) {
		    public void run( ) {
		    	MyLocation myLocation = new MyLocation();
				myLocation.getLocation(getApplicationContext(), locationResult);
		    }
		};
		start.run();
		
		//check if BL radio is on
		if (mBluetoothAdapter.isEnabled()) {
			//start BL disc
			mBluetoothAdapter.startDiscovery();
			//receiver handles the logging stuff after completing discovery
		} else {
			stopService(new Intent(getApplicationContext(), BluetoothService.class));
		}
	}
	
	private void initializeBluetoothStuff(Context context) {
		// Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();   
        
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            AppUtils.stopBluetoothAlarm(context);
        }
        
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
        
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);
        
        //Initialize new bluetooth devices list to hold them
        mNewBluetoothDevices = new ArrayList<BluetoothDevice>();
	}

	// The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                
                if (!mNewBluetoothDevices.contains(device))
                    mNewBluetoothDevices.add(device);
                
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//            	Toast.makeText(context, "Found Bluetooth Devices: "+mNewBluetoothDevices.size(), Toast.LENGTH_SHORT).show();
        		String discDevices = "";
            	if (mNewBluetoothDevices.size() != 0) {
            		for (int i=0;i< mNewBluetoothDevices.size();i++) {
                        BluetoothDevice bt = mNewBluetoothDevices.get(i);
//                        tv.append(bt.getAddress()+" - "+bt.getName()+" \r\n");
                        //prepare string
                        discDevices = discDevices + bt.getAddress()+ "-"+ bt.getBluetoothClass() + ";"; 
            		}            		
            	}
            	
            	//log list of discovered devices string to file
        		Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            	
            	String acelVals = "Timestamp:"+currentTimestamp+",TaskId:"+currentTask.getTaskId()+",ProviderId:"+AppConstants.providerId+currentLocation+",NumOfDevices:"+mNewBluetoothDevices.size()+",discDevices:"+discDevices+" \n";
            	
            	AppUtils.writeToFile(context, acelVals,"sensing_file"+currentTask.getTaskId());            		
        		
        		//clear the array for next scan
        		mNewBluetoothDevices.clear();
            	
            	stopService(new Intent(context, BluetoothService.class));
            }
        }                
    };
    
    public LocationResult locationResult = (new LocationResult(){
	    @Override
	    public void gotLocation(final Location loc){
	        //Got the location!
//	    	Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	    	if(loc != null)
	    		currentLocation = ",Latitude:" + loc.getLatitude() +	",Longitude:" + loc.getLongitude()+ ",Speed:" + loc.getSpeed();
			
			AppConstants.gpsLocUpdated = true;
	    }
	});
}
