package com.mcsense.app;

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
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcsense.json.JTask;

public class CompletedTasks extends ListActivity {
	TextView textview;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        textview = new TextView(this);
//        textview.setText("Below are the Completed Tasks: \r\n");
//        textview.setMovementMethod(new ScrollingMovementMethod());
        
        //setContentView(textview);
        
//        loadCompletedTasks();
//        loadCompleteTaskListView();
    }
	
	@Override
	protected void onResume(){
		super.onResume();
        loadCompleteTaskListView();
	}

	private void loadCompleteTaskListView() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		//TaskAdapter taskAdapter = new TaskAdapter(this, R.layout.list_item, AppConstants.getTaskList());
		TaskAdapter taskAdapter = new TaskAdapter(this, R.layout.list_item, loadCompletedTasks());
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
		      showToast("Selected: "+tt.getText());
		    }
		  });
	}
	
	private ArrayList<JTask> loadCompletedTasks() {
		String id = "";//task id
		String status = "C";
		

		ArrayList<JTask> jTaskList = null;
		
		Context context = getApplicationContext();
		
		//Set http params for timeouts
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, AppConstants.timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, AppConstants.timeoutSocket);
		
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		String providerURL = "http://"+AppConstants.ip+":10080/McSenseWEB/pages/TaskServlet";
		providerURL = providerURL + "?type=mobile&id="+id+"&status="+status+"&htmlFormName=tasklookup";
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
			showToast("Server temporarily not available!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast("Server temporarily not available!!");
		}
		
		if(jTaskList==null){
			JTask t = new JTask(0,"No Tasks"); 
			jTaskList = new ArrayList<JTask>();
			jTaskList.add(t);
		}
		
		return jTaskList;
	}
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
}
