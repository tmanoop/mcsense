package com.mcsense.app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;

public class AppUtils {
	private static ProgressDialog pDialog;
	
	public static boolean isServerAvailable(){
		boolean isAvailable;
		
		try {
			isAvailable = InetAddress.getByName(AppConstants.ip).isReachable(2000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAvailable = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAvailable = false;
		}
		
		return isAvailable;
	}
	
	public static boolean checkInternetConnection(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    // test for connection
	    if (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	        return true;
	    } else {
	        return false;
	    }
	}

	public static void showProgressWheel(Context context){
		pDialog = ProgressDialog.show(context, "Loading..", "Please wait", true,false);
	}
	
	public static void endProgressWheel(){
		pDialog.dismiss();
	}
}
