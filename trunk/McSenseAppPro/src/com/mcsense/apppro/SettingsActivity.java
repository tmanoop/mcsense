package com.mcsense.apppro;

import com.mcsense.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SettingsActivity extends Activity {
	
	private CheckBox autoLogin, notify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		loadUserDetails();
	}

	private void loadUserDetails() {
		TableLayout tb = (TableLayout) findViewById(R.id.tableLayout1); //new TableLayout(this);
        
        TextView emailIdText = new TextView(this);
        emailIdText.setText("EmailId: ");
        
        SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		String emailID = settings.getString("emailID", "");
        
		TextView emailId = new TextView(this);
        emailId.setText(emailID);
        emailId.setTextColor(Color.YELLOW);

        tb.addView(emailIdText,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tb.addView(emailId,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        TextView providerIdText = new TextView(this);
        providerIdText.setText("ProviderId: ");
        
		String provId = settings.getString("providerId", "");
        TextView providerId = new TextView(this);
        providerId.setText(provId);
        providerId.setTextColor(Color.YELLOW);
        
        tb.addView(providerIdText,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tb.addView(providerId,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        TextView meidText = new TextView(this);
        meidText.setText("MEID: ");
        
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String MEID = telephonyManager.getDeviceId();
        TextView meidView = new TextView(this);
        meidView.setText(MEID);
        meidView.setTextColor(Color.YELLOW);
        
        tb.addView(meidText,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tb.addView(meidView,  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        //read and store auto login and notifications radio buttons
        addListenerOnRadioButtons();
	}

	private void addListenerOnRadioButtons() {
		autoLogin = (CheckBox) findViewById(R.id.autoLogin);
		notify = (CheckBox) findViewById(R.id.notify);
		
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		String autoLoginValue = settings.getString("autoLogin", "");
		String notifyValue = settings.getString("notify", "");
		
		if(autoLoginValue.equals("0"))
			autoLogin.setChecked(false);
		if(notifyValue.equals("0"))
			notify.setChecked(false);
		
		autoLogin.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
	                //is chkIos checked?
			if (!((CheckBox) v).isChecked()) {
				Toast.makeText(SettingsActivity.this,
			 	   "Disabled auto login!!", Toast.LENGTH_LONG).show();
				storeCheckBoxValue("autoLogin","0");
				storeCheckBoxValue("login","");
			} else {
				SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
				String emailID = settings.getString("emailID", "");
				storeCheckBoxValue("login",emailID);
				storeCheckBoxValue("autoLogin","");
			}
	 
		  }
		});
	 
		notify.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
		                //is chkIos checked?
				if (!((CheckBox) v).isChecked()) {
					Toast.makeText(SettingsActivity.this,
				 	   "Disabled new notifications!!", Toast.LENGTH_LONG).show();
					storeCheckBoxValue("notify","0");
				} else {
					storeCheckBoxValue("notify","");
				}
		 
			  }
			});
	}

	protected void storeCheckBoxValue(String checkBox, String value) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(checkBox, value);
		editor.commit();
	}
	
	

}
