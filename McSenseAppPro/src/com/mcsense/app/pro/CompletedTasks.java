package com.mcsense.app.pro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcsense.app.R;
import com.mcsense.json.JTask;

public class CompletedTasks extends ListActivity {
	TextView textview;
	ArrayList<JTask> taskList;
	TaskAdapter taskAdapter;
	private ProgressDialog pDialog;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        textview = new TextView(this);
//        textview.setText("Below are the Completed Tasks: \r\n");
//        textview.setMovementMethod(new ScrollingMovementMethod());
        
        //setContentView(textview);
        
//        loadCompletedTasks();
//        loadCompleteTaskListView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
	
	@Override
	protected void onResume(){
		super.onResume();
		taskList = new ArrayList<JTask>();
		downloadTasks();
	}

	private void loadCompleteTaskListView() {
		//cache in static variables for later usage
		AppConstants.jTaskCompletedList = taskList;
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
		    JTask t = taskList.get(position);
		      if(!t.getTaskDescription().equals("No Completed Tasks"))
		    	  loadTask(t);
		      else
		    	  showToast(""+tt.getText());
		    }
		  });
	}
	
	protected void loadTask(JTask t) {
		Intent i = new Intent(getApplicationContext(), TaskActivity.class);
		i.putExtra("JTask", t);
		i.putExtra("tab_type", "completed");
        startActivity(i);
	}
	
	public void downloadTasks() {
	    pDialog = ProgressDialog.show(this, "Loading Tasks..", "Please wait", true,false);
	    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    Thread thread = new Thread(new Runnable() {
	    	 public void run() {
	    		 	// add downloading code here
	    		 	taskList = AppUtils.loadTasks("C",getApplicationContext());
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
	        loadCompleteTaskListView();
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
