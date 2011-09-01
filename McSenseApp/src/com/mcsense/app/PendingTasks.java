package com.mcsense.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class PendingTasks extends Activity {
	TextView textview;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textview = new TextView(this);
        textview.setText("Below are the Pending Tasks: \r\n");
        textview.setMovementMethod(new ScrollingMovementMethod());
        
        setContentView(textview);
        
        loadPendingTasks();
    }

	private void loadPendingTasks() {
		String id = "";//task id
		String status = "P";
		
		Context context = getApplicationContext();
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
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
			}
			is.close();

			// read task from servlet
			String task = sb.toString();
			System.out.println(task);
			
			textview.append(task + " \r\n");
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
		
	}
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
}
