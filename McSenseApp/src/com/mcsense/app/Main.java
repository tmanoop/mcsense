package com.mcsense.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends TabActivity {
	private static final String PREFS_NAME = "myPref";
	private WifiManager wifiManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String login = settings.getString("login", "");
		//String password = settings.getString("password", "");

		if (login.equals(""))
			loadHome();
		else{
			setContentView(R.layout.main);
			loadLoginUser();
			if(!AppUtils.checkInternetConnection(this)){
				enableWiFi();				
			}
			loadTabs();
		}
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
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String login = settings.getString("login", "");
		
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
	}

	@Override
	protected void onResume(){
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
		spec = tabHost.newTabSpec("new").setIndicator("New",
				res.getDrawable(R.drawable.ic_tab_new)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, PendingTasks.class);
		spec = tabHost.newTabSpec("pending").setIndicator("Pending",
				res.getDrawable(R.drawable.ic_tab_pending)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CompletedTasks.class);
		spec = tabHost.newTabSpec("completed").setIndicator("Completed",
				res.getDrawable(R.drawable.ic_tab_completed))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, TotalEarningsActivity.class);
		spec = tabHost.newTabSpec("earnings").setIndicator("Earnings",
				res.getDrawable(R.drawable.ic_tab_earnings)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}