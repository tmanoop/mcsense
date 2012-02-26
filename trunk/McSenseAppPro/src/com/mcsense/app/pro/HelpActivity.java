package com.mcsense.app.pro;

import com.mcsense.app.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.help);
	}
}
