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
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcsense.apache.ObjectSerializer;
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
	    //&& cm.getActiveNetworkInfo().isAvailable()
        //&& cm.getActiveNetworkInfo().isConnected()
	    if (cm.getActiveNetworkInfo() != null) {
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
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/TaskServlet";
		providerURL = providerURL + "?type=mobile&providerId="+AppConstants.providerId+"&status="+status+"&htmlFormName=tasklookup";
		HttpGet httpget = new HttpGet(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		String serverMessage = "";
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
				if (!line.equals("No Tasks")) {
					sb.append(line + "\n");
					System.out.println("sb:"+sb);
					//Parse Response into our object
					Type collectionType = new TypeToken<ArrayList<JTask>>() {
					}.getType();
					jTaskList = new Gson().fromJson(line, collectionType);
				}
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
			serverMessage = "Server not reachable";
//			Toast.makeText(context, "Server temporarily not available!!", Toast.LENGTH_SHORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			serverMessage = "Server not reachable";
//			Toast.makeText(context, "Server temporarily not available!!", Toast.LENGTH_SHORT);
		}
		
		if(serverMessage.equals("Server not reachable")){
			if(!status.equals("P"))
				jTaskList = getLastSavedTabList(status, context);
		} else {
			if(!status.equals("P"))
				cacheTabList(jTaskList, status, context);
//				saveArrayList(jTaskList, status, context);
		}
		
		if(jTaskList==null || jTaskList.size()==0){
			String desc = "";
			if(status.equals("P"))
				desc = "No Available Tasks";
			else if(status.equals("IP"))
				desc = "No Accepted Tasks";
			else if(status.equals("C"))
				desc = "No Completed Tasks";
			JTask t = new JTask(0,desc); 
			t.setTaskType(serverMessage);
			jTaskList = new ArrayList<JTask>();
			jTaskList.add(t);
		}
		 
		return jTaskList;
	}
	
	public static void cacheTabList(ArrayList<JTask> jTaskList, String tabName, Context context) {
		Log.d(AppConstants.TAG, "start cacheTabList");
		//save the task list to preference
		SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		try {
			editor.putString(tabName, new Gson().toJson(jTaskList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	public static void saveArrayList(ArrayList<JTask> jTaskList, String tabName, Context context) {

	    final File file = new File(context.getCacheDir(), tabName);
	    FileOutputStream outputStream = null;
	    ObjectOutputStream objectOutputStream = null;

	    try {
	        outputStream = new FileOutputStream(file);
	        objectOutputStream  = new ObjectOutputStream(outputStream);

	        objectOutputStream.writeObject(jTaskList);
	    }

	    catch(Exception e) {
	        e.printStackTrace();
	    }
	    finally {
	        try {
	            if(objectOutputStream != null) {
	                objectOutputStream.close();
	            }
	            if(outputStream != null) {
	                outputStream.close();
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public static ArrayList<JTask> getLastSavedTabList(String tabName, Context context) {
		Log.d(AppConstants.TAG, "start getLastSavedTabList");
		//get the task list from preference
		SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
		ArrayList<JTask> jTaskList = null;
        try {
        	String jsonString = prefs.getString(tabName, null);
        	Type collectionType = new TypeToken<ArrayList<JTask>>() {
			}.getType();
			jTaskList = new Gson().fromJson(jsonString, collectionType);
//        	jTaskList = (ArrayList<JTask>) ObjectSerializer.deserialize(prefs.getString(tabName, ObjectSerializer.serialize(new ArrayList<JTask>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(AppConstants.TAG, "ArrayList size: "+jTaskList.size());
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
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm";

	  public static String currentTime() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());
	
	  }
}
