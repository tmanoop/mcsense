package com.mcsense.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
 
 
public class MyStartupIntentReceiver extends BroadcastReceiver  {
 
	@Override
	public void onReceive(Context context, Intent intent) {
	    // call your app launcher activity here ....
		loadApp(context);
	}
	
	protected void loadApp(Context context) {
        Intent i = new Intent(context, Main.class);  
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);  
	}
 
}