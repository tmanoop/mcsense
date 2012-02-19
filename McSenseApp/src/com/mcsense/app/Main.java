package com.mcsense.app;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends TabActivity {

	private WifiManager wifiManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		String login = settings.getString("login", "");
		//String password = settings.getString("password", "");

		if (login.equals(""))
			loadHome();
		else{
			setContentView(R.layout.main);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			loadLoginUser();
			if(!AppUtils.checkInternetConnection(this)){
				enableWiFi();				
			}
			loadTabs();
//			iniNotificationService(this.getApplicationContext());
			if(!AppUtils.isAlarmExist(getApplicationContext()))
				iniNotificationAlarm();
			if(!AppUtils.isUploadAlarmExist(getApplicationContext()))
				iniUploadAlarm();
			if(!AppUtils.isServiceAlarmExist(getApplicationContext()))
				iniServiceAlarm();
		}
	}
	
	private void iniServiceAlarm() {
		Bundle bundle = new Bundle();
		// add extras here..
		ServiceAlarm alarm = new ServiceAlarm(this, bundle, 30);
	}
	
	private void iniUploadAlarm() {
		Bundle bundle = new Bundle();
		// add extras here..
		UploadAlarm alarm = new UploadAlarm(this, bundle, 30);
	}

	private void iniNotificationAlarm() {
		Bundle bundle = new Bundle();
		// add extras here..
		MyAlarm alarm = new MyAlarm(this, bundle, 30);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.logout:
	        logout();
	        return true;
	    case R.id.settings:
	    	settings();
	        return true;
	    case R.id.help:
	        showHelp();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void iniNotificationService(Context context) {
		if(!AppUtils.isNotifiyServiceRunning(context)){
			Intent intentNotify = new Intent(context, NotificationService.class);
//			intent.putExtra("JTask", currentTask);
			context.startService(intentNotify);
		}		
	}
	
	private void settings() {
		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
	}

	private void showHelp() {
		Intent i = new Intent(getApplicationContext(), HelpActivity.class);
        startActivity(i);
	}

	private void logout() {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = settings.edit();
		editor.putString("providerId", "");	
		editor.putString("login", "");	
		editor.commit();
		finish();
	}

	private void enableWiFi() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Switch on WiFi?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						wifiManager.setWifiEnabled(true);
						AppUtils.showProgressWheel(Main.this);
						new Thread() {
				            public void run() {
				                try {
				                    // sleep the thread, whatever time you want. 
				                    sleep(8000);
				                } catch (Exception e) {
				                }
				                AppUtils.endProgressWheel();
				            }
				        }.start();
					}
				});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("McSense App cannot start with out Internet!!");
						finish();
					}
				});

		alert.show();
	}

	private void loadLoginUser() {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		String login = settings.getString("login", "");
		String providerId = settings.getString("providerId", "");
		AppConstants.providerId = providerId;
		//check for non student IDs 
		if(!login.contains("njit")){
			if(login.contains("@"))
				login = login.substring(0,login.indexOf("@"));
			//and truncate to 20char
			if(login.length() > 20){
				login = login.substring(0, 20);
			}
		}			
		
		TextView loginEmailID = (TextView)findViewById(R.id.loginEmailID);		
		loginEmailID.setText(login);
		String autoLogin = settings.getString("autoLogin", "");
		if(autoLogin.equals("0")){
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("login", "");
			editor.commit();
		}
			
	}

	@Override
	protected void onResume(){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onResume();
//		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//		String login = settings.getString("login", "");
//		//String password = settings.getString("password", "");
//
//		if (login.equals(""))
//			loadHome();
//		else{
//			setContentView(R.layout.main);
//			loadTabs();
//		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
//	@Override
//	public void onBackPressed() {
//	  super.onBackPressed();
//	}
	
	private void loadHome() {
		this.finish();
		Intent i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);
	}

	private void showCloseDialogue() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

//		alert.setTitle("Please Login to McSense");
		alert.setMessage("Are you sure you want to quit?");
		
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
					}
				});

		alert.show();
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

	private void loadTabs() {
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, NewTasks.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("new").setIndicator("Available",
				res.getDrawable(R.drawable.ic_tab_new)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, PendingTasks.class);
		spec = tabHost.newTabSpec("pending").setIndicator("Accepted",
				res.getDrawable(R.drawable.ic_tab_pending)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CompletedTasks.class);
		spec = tabHost.newTabSpec("completed").setIndicator("Completed",
				res.getDrawable(R.drawable.ic_tab_completed))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TotalEarningsActivity.class);
		spec = tabHost.newTabSpec("earnings").setIndicator("Earnings",
				res.getDrawable(R.drawable.ic_tab_dollar)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(1);
	}
}