package com.mcsense.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.mcsense.json.JTask;

public class TaskActivity extends Activity {
	private static final int PICTURE_RESULT = 9;
	LinearLayout linear;
	Bitmap bitmap;
	JTask currentTask;
	Context context;
	private ProgressDialog pDialog;

	TextView text;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        linear = new LinearLayout(this);
//
//        linear.setOrientation(LinearLayout.VERTICAL);
//
//        text = new TextView(this);
//
//
//        text.setText("This is an example for the Bright Hub!");
//
//        linear.addView(text);

//        setContentView(linear);
		
		context = getApplicationContext();
        setContentView(R.layout.task);
        
        JTask jTaskObjInToClass = getIntent().getExtras().getParcelable("JTask");
//        showToast(jTaskObjInToClass.getTaskDescription());
        text = (TextView) findViewById(R.id.txtTest);
        text.setText(jTaskObjInToClass.getTaskName());
        currentTask = jTaskObjInToClass;
//        loadTaskDetails(jTaskObjInToClass);
        
//        iniAccel();
        
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadTaskDetails(currentTask);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.gc();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}
	
	private void loadTaskDetails(JTask jTask) {
//		LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.linearLayout1);
		//get calling tab type
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
		final String tab_type = getIntent().getExtras().getString("tab_type");
		
		if(tab_type.equals("completed")){
			TextView clientPayTextView = (TextView) findViewById(R.id.clientPayTextView); 
			clientPayTextView.setText("Dollars earned:");
			
			String startTime = settings.getString("startTime", "");			
			TextView start = (TextView) findViewById(R.id.start); 
			start.setText(startTime);
			
			TextView textElapsed = (TextView) findViewById(R.id.textElapsed);
			textElapsed.setText("Task Completed on: ");
			
			String completedTime = settings.getString("completedTime", "");

			TextView elapsed = (TextView) findViewById(R.id.elapsed);
			elapsed.setText(completedTime);
		} else if(tab_type.equals("pending")){
			String taskIDString = settings.getString("taskID", "");
			String status = settings.getString("status", "");
			int sensingTaskID = 0;
			if(!taskIDString.equals(""))
				sensingTaskID = Integer.parseInt(taskIDString);
			if(jTask.getTaskId() == sensingTaskID){//&& status.equals("IP")
				String startTime = settings.getString("startTime", "");
				String elapsedTime = settings.getString("elapsedTime", "");			
				
				TextView start = (TextView) findViewById(R.id.start); 
				start.setText(startTime);
				
				if(!elapsedTime.equals("") && status.equals("IP")){
					TextView elapsed = (TextView) findViewById(R.id.elapsed);
					float elapsedTimeMin = Long.parseLong(elapsedTime)/(60*1000F);
					int min = Math.round(elapsedTimeMin);
					if(min < 60 )
						elapsed.setText(min +" mins");
					else{
						int hour = min / 60;
						int rem_min = min % 60;
						elapsed.setText(hour +" Hours " + rem_min +" mins");
					}
						
				}
			}
		}
		
		TextView clientPay = (TextView) findViewById(R.id.clientPay); 
		clientPay.setText("$"+jTask.getClientPay());
		
		TextView taskStatus = (TextView) findViewById(R.id.taskStatus);
		String tskStat = jTask.getTaskStatus();
		if(tskStat.equals("P"))
			tskStat = "Available";
		else if(tskStat.equals("IP"))
			tskStat = "Accepted";
		else if(tskStat.equals("C"))
			tskStat = "Completed";
		taskStatus.setText(tskStat);
		
		TextView taskType = (TextView) findViewById(R.id.taskType); 
		taskType.setText(jTask.getTaskType());
		
		TextView duration = (TextView) findViewById(R.id.duration); 
		duration.setText(jTask.getTaskDuration()+" mins");
		
		
		final TextView taskDesc = (TextView) findViewById(R.id.taskDescription); 
		taskDesc.setText(jTask.getTaskDescription());
		
		final Button button = (Button) findViewById(R.id.button1); 
		
		//get task ID
		final String taskID = jTask.getTaskId()+"";
		final String tskType = jTask.getTaskType();
		String buttonName = "";
		if(tab_type.equals("completed")){
        	button.setVisibility(View.INVISIBLE);
        } else if(tab_type.equals("new")){
        	buttonName = "Accept Task";
        } else if(tab_type.equals("pending")){
			if(tskType.equals("campusSensing")){
				if(AppUtils.isServiceRunning(getApplicationContext()))
					buttonName = "Stop Sensing";
				else
					buttonName = "Re-start Sensing";
			} else if(tskType.equals("photo")){
				buttonName = "Take Photo";
			}
		}
		button.setText(buttonName);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(tab_type.equals("new")){
            		//check for already running service
            		if(tskType.equals("campusSensing") && AppUtils.isServiceRunning(getApplicationContext())){
            			showToast("Another Sensing Task is already running.");
            			taskDesc.setText(currentTask.getTaskDescription());
//            			taskDesc.append("\t\n \t\n Task not accepted. \n Another Sensing Task is already running.");
            			String appendText = "\t\n \t\n Task not accepted. \n Another Sensing Task is already running.";
            			Spannable WordtoSpan = new SpannableString(appendText);

            			WordtoSpan.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, appendText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            			taskDesc.append(WordtoSpan);
            		} else {
            			//call server to accept the task and update its status
                		String resp = acceptTask(taskID);
                		showToast(resp);
                		if(resp.equals("Accepted")){
                    		if(tskType.equals("campusSensing"))
                        		iniSensingService();
                		}
                		finish();
            		}          		
            	} else if(tab_type.equals("pending")){
            		if(tskType.equals("photo")){
//            			showToast("Start Camera");
                		bitmap = null;
                		photo();
            		} else if(tskType.equals("campusSensing")){
            			String buttonName = button.getText().toString();
            			if(buttonName.equals("Stop Sensing")){
            				stopService(new Intent(TaskActivity.this, SensingService.class));
            				SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME, 0);
            				String startTimeMillisecs = settings.getString("startTimeMillisecs", "");
            				long start = Long.parseLong(startTimeMillisecs);
            				long elapsedTimeMillis = System.currentTimeMillis()-start;
            				float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
            				int min = Math.round(elapsedTimeMin);
            				if(min<3){
            					AppConstants.status = "E";
            					AppConstants.failedTaskList.add(currentTask.getTaskId()+"");
            					System.out.println("currentTaskId: "+currentTask.getTaskId()+" min: "+min+" status: "+AppConstants.status);
            				}
            					logSensingElapsedTime(elapsedTimeMillis, currentTask.getTaskId(), "E");
            				uploadSensedData();
            				button.setText("Re-start Sensing");
            			} else if(buttonName.equals("Re-start Sensing")){
            				iniSensingService();
            				button.setText("Stop Sensing");
            			}
            		}
            	}
            }
        });
//		TextView acelValues = (TextView) findViewById(R.id.acelValues); 
//		String acelVal = getAccelValues();
//		acelValues.setText(acelVal);
//		linearLayout.addView(t);
		
	}
	
	protected void logSensingElapsedTime(long elapsed,int taskID, String status) {
		SharedPreferences settings = getSharedPreferences(AppConstants.PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("taskID", taskID + "");
		editor.putString("status", status);
		editor.putString("elapsedTime", elapsed+"");
		System.out.println("status: "+status+"taskID: "+ taskID);
		AppConstants.status = status;
		editor.putString("completedTime", AppUtils.currentTime());
		editor.commit();
	}
	
	protected void iniSensingService() {
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		boolean isGPS = mlocManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
		if(!isGPS)
			enableGPSSettings();
		else{
			Intent intent = new Intent(this, SensingService.class);
			intent.putExtra("JTask", currentTask);
			startService(intent);
			finish();
		}
	}

	private void enableGPSSettings() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Switch on GPS?");
		alert.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						TaskActivity.this.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
					}
				});

		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						showToast("McSense App cannot sense with out GPS!!");
						stopService(new Intent(TaskActivity.this, SensingService.class));
					}
				});

		alert.show();
	}
	
	protected String acceptTask(String taskID) {

		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/ProviderServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("taskStatus", "Accepted"));
        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
        nameValuePairs.add(new BasicNameValuePair("taskId", currentTask.getTaskId()+""));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        
		// Execute HTTP Get Request
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			System.out.println("Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(sb);
			}
			is.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read task from servlet
		String resp = sb.toString();
		System.out.println(resp);
		return resp.trim();
//		showToast("Submitted: " + resp + " \r\n");
	}

	private void photo() {
//		showToast("Photo");
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File image = new File(Environment.getExternalStorageDirectory(),"McSenseImage.jpg");
		camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

        this.startActivityForResult(camera, PICTURE_RESULT);
//        showToast("Photo Saved!!"+Uri.fromFile(image));
//	    Intent myIntent = new Intent(item.getIntent());
//        startActivity(myIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		showToast("requestCode: "+requestCode);
//		showToast("resultCode: "+resultCode);
		Log.d("McSense", "requestCode: "+requestCode+" resultCode: "+resultCode);
		Bitmap image = null;
	    if(requestCode == PICTURE_RESULT){
	        if(resultCode == Activity.RESULT_OK) {
	            if(data!=null){
	                image = BitmapFactory.decodeFile(data.getExtras().get(MediaStore.Images.Media.TITLE).toString());
//	                preview(image);
//	                showToast("Photo Saved!!");
//	                grid.add(image);            
//	                images.addItem(image);
	            }
	            if(data==null){
//	                Toast.makeText(Team_Viewer.this, "no data.", Toast.LENGTH_SHORT).show();
	            }
	            preview(image);
	        }
	        else if(resultCode == Activity.RESULT_CANCELED) {
//	            Toast.makeText(Team_Viewer.this, "Picture could not be taken.", Toast.LENGTH_SHORT).show();
//	        	showToast("RESULT_CANCELED!!");
	        	
	        }
//	        showToast("data:!!"+data);
//	        preview(image);
//	        uploadPhoto();
	    }
	}
	
	private void preview(Bitmap image){
//		showToast("preview!!");
		ImageView bmImage = (ImageView)findViewById(R.id.imageView1);//new ImageView(this);//
		   BitmapFactory.Options bmOptions;
		   bmOptions = new BitmapFactory.Options();
		   bmOptions.inSampleSize = 8;
		   bmOptions.requestCancelDecode();
		   String imageInSD = "/sdcard/McSenseImage.jpg";
		   bitmap = BitmapFactory.decodeFile(imageInSD);
		   bmImage.setImageBitmap(bitmap);
		   
		   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		   //add upload button
		   addUploadButton();
//		   uploadPhoto();
	}
	
	private void addUploadButton() {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout1);
		System.out.println("adding upload button");
		Button button = new Button(this); 
		button.setText("Upload Photo");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	uploadProgress();
            }
        });
        RelativeLayout.LayoutParams p = new 
        RelativeLayout.LayoutParams( 
        		RelativeLayout.LayoutParams.FILL_PARENT, 
        		RelativeLayout.LayoutParams.WRAP_CONTENT 
                    ); 
		rl.addView(button,p);
		//hide take photo button
		Button takePhotoButton = (Button) findViewById(R.id.button1);
		takePhotoButton.setVisibility(View.INVISIBLE);
	}

	public void uploadProgress() {
	    pDialog = ProgressDialog.show(this, "Uploading Photo..", "Please wait", true,false);
	    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    Thread thread = new Thread(new Runnable() {
	    	 public void run() {
	    		// add downloading code here
	    		 	uploadPhoto();
	    		    handler.sendEmptyMessage(0);
	    		}     
	    	 });
	    thread.start();
	}
	
	private Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        pDialog.dismiss();
	        // handle the result here
			finish();
	    }
	};
	
	private void uploadPhoto(){
//		showToast("uploading!!");
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/PhotoServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		//read data
//		TextView taskIDTxt = (TextView) findViewById(R.id.taskID);;
//		TextView taskDescTxt = (TextView) findViewById(R.id.txtTest);
		
//		String imageInSD = Environment.getExternalStorageDirectory()+"/McSenseImage.jpg";
//		String imageInSD = "/sdcard/McSenseImage.jpg";
		//Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
//		bitmap = BitmapFactory.decodeFile(imageInSD);
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		String ba1="";
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
	        byte [] ba = bao.toByteArray();
			ba1 = Base64.encodeToString(ba,0);
//	        ArrayList<String> baList = getBitmapEncodedString(ba);
//	        ba1 = baList.get(0);
	        
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
//			showToast("uploading error!! "+e1.getMessage());
		}
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("taskDesc", currentTask.getTaskDescription()));
        nameValuePairs.add(new BasicNameValuePair("taskId", currentTask.getTaskId()+""));
        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        nameValuePairs.add(new BasicNameValuePair("image",ba1));
        
		// Execute HTTP Get Request
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			System.out.println("Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(sb);
			}
			is.close();
			bao.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read task from servlet
		String resp = sb.toString();
		System.out.println(resp);
		
//		showToast("Uploaded: \r\n");
	}
	
	private ArrayList<String> getBitmapEncodedString(byte[] ba) {
		ArrayList<String> ba1 = new ArrayList<String>();
		int chunkSize = 256;
        for(int i =0; i<ba.length;i=i+chunkSize){
        	try {
        		int nextSize = i+chunkSize;
            	if(ba.length<i+chunkSize)
            		nextSize = ba.length;
            	System.out.println("nextSize: "+nextSize);
				String temp = Base64.encodeToString(ba,i,nextSize,Base64.DEFAULT);
				ba1.add(temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast("uploading error!! \r\n"+e.getMessage());
			}
        }
		return ba1;
	}
	
	private String getAccelValues() {
		String FILENAME = "accel_file";
		String acelValues = "";
		try {
			//reading
			FileInputStream fis = openFileInput(FILENAME);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			acelValues = new String(buffer);
			
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return acelValues;
	}

	private void iniAccel() {
		Intent i = new Intent(getApplicationContext(), Sensors.class);
        startActivity(i);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	protected void uploadSensedData() {
		// "accel_file"+currentTask.getTaskId()
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/pages/ProviderServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		//read sensed data file and set as string
		String sensedData = AppUtils.readFile(getApplicationContext(),"sensing_file"+currentTask.getTaskId());
//		String sensedData = AppUtils.readXFile("sensing_file"+currentTask.getTaskId());
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("taskStatus", "Completed"));
        nameValuePairs.add(new BasicNameValuePair("providerId", AppConstants.providerId));
        nameValuePairs.add(new BasicNameValuePair("taskId", currentTask.getTaskId()+""));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        nameValuePairs.add(new BasicNameValuePair("sensedData", sensedData));
        
		// Execute HTTP Get Request
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			System.out.println("Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(sb);
			}
			is.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read task from servlet
		String resp = sb.toString();
		System.out.println(resp);
		finish();
	}
}
