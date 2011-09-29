package com.mcsense.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcsense.json.JTask;

public class NewTasks extends ListActivity{
	
	Button pickButton;
	TextView textview;
	ArrayList<JTask> taskList;
	TaskAdapter taskAdapter;
	private ProgressDialog pDialog;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //OK button
//		ImageButton button = (ImageButton) findViewById(R.id.imgbtn2);
//		button.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				// Perform action on click
//				showToast("Loading..");
//				taskList = loadNewTasks();
//				taskAdapter.notifyDataSetChanged();
//			}
//		});
        
//        LinearLayout panel = new LinearLayout(this);
// 		panel.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
// 				LayoutParams.WRAP_CONTENT));
// 		panel.setOrientation(LinearLayout.VERTICAL);
//     
//        pickButton = new Button(this);
//        pickButton.setText("Refresh");
//        pickButton.setLayoutParams(new LayoutParams(
//        	    ViewGroup.LayoutParams.WRAP_CONTENT,
//        	    ViewGroup.LayoutParams.WRAP_CONTENT));
//        panel.addView(pickButton);
//        
//        textview = new TextView(this);
//        textview.setText("Below are the New Tasks:  \r\n");
//        textview.setMovementMethod(new ScrollingMovementMethod());
//        panel.addView(textview);
//        
//        setContentView(panel);
        
//        loadNewTaskListView();
//		pick();
		
    }
	
	private void loadNewTaskListView() {
//		taskList = new ArrayList<JTask>();
////		taskList = loadNewTasks();
//		downloadTasks();
//		if(taskList.size()==0){
//			JTask t = new JTask(0,"No New Tasks"); 
//			taskList.add(t);
//		}
		//TaskAdapter taskAdapter = new TaskAdapter(this, R.layout.list_item, AppConstants.getTaskList());
		taskAdapter = new TaskAdapter(this, R.layout.list_item, taskList);
		setListAdapter(taskAdapter);
//		taskAdapter.notifyDataSetChanged();
		
		  ListView lv = getListView();
		  lv.setTextFilterEnabled(true);
		  lv.setAdapter(taskAdapter);
		  
		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	//When clicked, show a toast with the TextView text
		    	//Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
		    	TextView tt = (TextView) view.findViewById(R.id.toptext);
		    	JTask t = taskList.get(position);
			      if(!t.getTaskDescription().equals("No New Tasks"))
			    	  loadTask(t);
			      else
			    	  showToast(""+tt.getText());
		    }
		  });
	}

	protected void loadTask(JTask t) {
		Intent i = new Intent(getApplicationContext(), TaskActivity.class);
		i.putExtra("JTask", t);
		i.putExtra("tab_type", "new");
        startActivity(i);
	}
	
	public void downloadTasks() {
	    pDialog = ProgressDialog.show(this, "Loading Tasks..", "Please wait", true,false);
	    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    Thread thread = new Thread(new Runnable() {
	    	 public void run() {
	    		// add downloading code here
	    			taskList = loadNewTasks();
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
	        loadNewTaskListView();
			taskAdapter.notifyDataSetChanged();
	    }
	};
	
	private ArrayList<JTask> loadNewTasks() {
		
		ArrayList<JTask> jTaskList = null;
		
		String id = "101";
		
		Context context = getApplicationContext();
		
		//Set http params for timeouts
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, AppConstants.timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, AppConstants.timeoutSocket);
		
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		String providerURL = "http://"+AppConstants.ip+":10080/McSenseWEB/pages/ProviderServlet";
		providerURL = providerURL + "?type=mobile&id="+id;
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
				
				if(!line.equals("No New Tasks")){
					sb.append(line + "\n");
					System.out.println(sb);
					
					//Parse Response into our object
		            Type collectionType = new TypeToken<ArrayList<JTask>>(){}.getType();
					jTaskList = new Gson().fromJson(line, collectionType);
				} 
//				else {
//					JTask t = new JTask(0,line); 
//					jTaskList = new ArrayList<JTask>();
//					jTaskList.add(t);
//				}
				
			}
			is.close();

			// read task from servlet
//			String task = sb.toString();
//			System.out.println(task);
//			
//			textview.append("Sensing Task Read: " + task + " \r\n");
//			scrollDown();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			showToast("Server temporarily not available!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			showToast("Server temporarily not available!!");
		}
		
		if(jTaskList==null){
			JTask t = new JTask(0,"No New Tasks"); 
			jTaskList = new ArrayList<JTask>();
			jTaskList.add(t);
		}
		return jTaskList;
	}

	private void iniAccel() {
		Intent i = new Intent(getApplicationContext(), Sensors.class);
        startActivity(i);
	}

	@Override
	protected void onPause()
	{
	super.onPause();
	    //Save state here
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		taskList = new ArrayList<JTask>();
//		taskList = loadNewTasks();
		downloadTasks();
//		loadNewTaskListView();
	}
	
	@Override
	public void onBackPressed() {
//		moveTaskToBack(true);
	  //showCloseDialogue();
	  super.onBackPressed();
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
	
	public void pick() {
		//Button signButton = (Button) findViewById(R.id.button1);
		pickButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//EditText et2 = (EditText) findViewById(R.id.editText2);
				//String id = et2.getText().toString();
				String id = "101";
				
				Context context = getApplicationContext();
				// http servlet call
				HttpClient httpclient = new DefaultHttpClient();
				String providerURL = "http://"+AppConstants.ip+":10080/McSenseWEB/pages/ProviderServlet";
				providerURL = providerURL + "?type=mobile&id="+id;
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
					}
					is.close();

					// read task from servlet
					String task = sb.toString();
					System.out.println(task);
					
					textview.append("Sensing Task Read: " + task + " \r\n");
//					scrollDown();
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Server temporarily not available!!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Server temporarily not available!!");
				}
			}
		});
	}
	
//	public void scrollDown(){
//		final ScrollView scroll = (ScrollView) findViewById(R.id.verticalScrollView1);
//		scroll.post(new Runnable() {
//            public void run() {
//                scroll.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });
//	}
}
