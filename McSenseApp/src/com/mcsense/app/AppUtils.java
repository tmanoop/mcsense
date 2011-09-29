package com.mcsense.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcsense.json.JTask;

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
	
	public static ArrayList<JTask> loadTasks(String stat, Context context) {
		String status = stat;
		ArrayList<JTask> jTaskList = null;
		
//		Context context = getApplicationContext();
		
		//Set http params for timeouts
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, AppConstants.timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, AppConstants.timeoutSocket);
		
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		String providerURL = "http://"+AppConstants.ip+":10080/McSenseWEB/pages/TaskServlet";
		providerURL = providerURL + "?type=mobile&providerId="+AppConstants.providerId+"&status="+status+"&htmlFormName=tasklookup";
		HttpGet httpget = new HttpGet(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		// Execute HTTP Get Request
		try {
			response = httpclient.execute(httpget);
			System.out.println("Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(sb);
				
				//Parse Response into our object
	            Type collectionType = new TypeToken<ArrayList<JTask>>(){}.getType();
				jTaskList = new Gson().fromJson(line, collectionType);
			}
			is.close();

			// read task from servlet
//			String task = sb.toString();
//			System.out.println(task);
//			
//			textview.append(task + " \r\n");
//			scrollDown();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Toast.makeText(context, "Server temporarily not available!!", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Toast.makeText(context, "Server temporarily not available!!", Toast.LENGTH_SHORT);
		}
		
		if(jTaskList==null){
			JTask t = new JTask(0,"No Tasks"); 
			jTaskList = new ArrayList<JTask>();
			jTaskList.add(t);
		}
		 
		return jTaskList;
	}
	
	public static boolean isServiceRunning(Context context){
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);


	    boolean isServiceFound = false;
	
	    for (int i = 0; i < services.size(); i++) {
	        //Log.d(Global.TAG, "Service Nr. " + i + " :" + services.get(i).service);
	        //Log.d(Global.TAG, "Service Nr. " + i + " package name : " + services.get(i).service.getPackageName());
	        Log.d("McSense", "Service Name " + i + " class name : " + services.get(i).service.getClassName());
	
	        if ("com.mcsense.app".equals(services.get(i).service.getPackageName())){
	            Log.d("McSense", "packagename is found !!!");
	            // Log.d(LOG_TAG, "SpotService" + " : " +
	            // services.get(i).service.getClassName());
	
	            if ("com.mcsense.app.SensingService".equals(services.get(i).service.getClassName())){
	                Log.d("McSense", "getClassName is found !!!");
	                isServiceFound = true;
	            }
	        }
	    }
	    return isServiceFound;
	}
	
	public static void writeToXFile(String values, String fileName) {
		String FILENAME = fileName;//"accel_file"
		String acelValues = values;

		try {
			//writing
			File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	        File file = new File(path, FILENAME+".txt");
	        //FileOutputStream fos;
	        FileWriter fw = new FileWriter(file, true);
			//fos = new FileOutputStream(file);
			//fos = openFileOutput(FILENAME, Context.MODE_WORLD_WRITEABLE);
			fw.write(acelValues);
			fw.close();
			//fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readXFile(String fileName) {
		String sensedData = "";
		try
		{               
			  File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			  File file = new File(path, fileName+".txt");
			  FileInputStream fileIn = new FileInputStream(file);
			  byte[] buffer = new byte[fileIn.available()];
			  fileIn.read(buffer);
			  sensedData = Base64.encodeToString(buffer, Base64.DEFAULT);                   
			  fileIn.close();                       
		} catch (FileNotFoundException e) {
			  e.printStackTrace();
		} catch (IOException e)	{   
		      e.printStackTrace();
		}
		return sensedData;
	}
	
	public static void writeToFile(Context context, String values, String fileName) {
		String FILENAME = fileName;//"accel_file"
		String acelValues = values;

		try {
			//writing
			FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
			fos.write(acelValues.getBytes());
//			showToast("Writing to file.."+acelValues);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readFile(Context context, String fileName) {
		String sensedData = "";
		try
		{               
		  FileInputStream fileIn;       
		  fileIn = context.openFileInput(fileName);
		  byte[] buffer = new byte[fileIn.available()];
		  fileIn.read(buffer);
		  sensedData = Base64.encodeToString(buffer, Base64.DEFAULT);                   
		  fileIn.close();                       
		} catch (FileNotFoundException e)
		{
		   e.printStackTrace();
		}
		catch (IOException e)
		{   
		    e.printStackTrace();
		}
		return sensedData;
	}
}
