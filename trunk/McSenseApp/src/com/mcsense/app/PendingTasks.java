package com.mcsense.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcsense.json.JTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PendingTasks extends ListActivity {
	TextView textview;
	ArrayList<JTask> taskList;
	TaskAdapter taskAdapter;
	private ProgressDialog pDialog;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        textview = new TextView(this);
//        textview.setText("Below are the Pending Tasks: \r\n");
//        textview.setMovementMethod(new ScrollingMovementMethod());
//        
//        setContentView(textview);
//        
//        loadPendingTasks();
//        loadPendingTaskListView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
	
	@Override
	protected void onResume(){
		super.onResume();
		taskList = new ArrayList<JTask>();
		downloadTasks();
	}
	
	private void loadPendingTaskListView() {
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
		      // When clicked, show a toast with the TextView text
//		      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
		    TextView tt = (TextView) view.findViewById(R.id.toptext);
//		      showToast("Selected: "+tt.getText());
		      JTask t = taskList.get(position);
		      if(!t.getTaskDescription().equals("No Accepted Tasks"))
		    	  loadTask(t);
		      else
		    	  showToast(""+tt.getText());
		    }
		  });
	}

	protected void loadTask(JTask t) {
		Intent i = new Intent(getApplicationContext(), TaskActivity.class);
		i.putExtra("JTask", t);
		i.putExtra("tab_type", "pending");
        startActivity(i);
	}
	
	public void downloadTasks() {
	    pDialog = ProgressDialog.show(this, "Loading Tasks..", "Please wait", true,false);
	    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    Thread thread = new Thread(new Runnable() {
	    	 public void run() {
	    		 	// add downloading code here
	    		 	taskList = AppUtils.loadTasks("IP",getApplicationContext());
	    		 	filterLongTermTasks();
	    		    handler.sendEmptyMessage(0);
	    		}     
	    	 });
	    thread.start();
	}

	protected void filterLongTermTasks() {
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		Log.d(AppConstants.TAG, "Hour: "+currentHour);
		for(JTask temp : taskList){
			//donot show longterm tasks before noon and they are started from 12 noon by service alarm
			if(temp.getParentTaskId() != 0 && currentHour<12){
				taskList.remove(temp);
			}	
		}
		if(taskList.size()==0){
			String desc = "No Accepted Tasks";
			JTask t = new JTask(0,desc); 
			taskList = new ArrayList<JTask>();
			taskList.add(t);
		}
	}

	private Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        pDialog.dismiss();
	        // handle the result here
	        loadPendingTaskListView();
			taskAdapter.notifyDataSetChanged();
	    }
	};
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
}
