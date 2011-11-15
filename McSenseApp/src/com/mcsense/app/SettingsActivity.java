package com.mcsense.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SettingsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		loadUserDetails();
	}

	private void loadUserDetails() {
		TableLayout tb = (TableLayout) findViewById(R.id.tableLayout1); //new TableLayout(this);
        
        TextView emailIdText = new TextView(this);
        emailIdText.setText("EmailId: ");
        
        SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		String login = settings.getString("login", "");
        
		TextView emailId = new TextView(this);
        emailId.setText(login);
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

	}

}
