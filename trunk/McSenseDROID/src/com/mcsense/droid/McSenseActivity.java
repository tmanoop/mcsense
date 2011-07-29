package com.mcsense.droid;

import java.io.BufferedReader;
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
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class McSenseActivity extends Activity {

	TextView tv;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		TextView titleView = new TextView(this);
//		titleView.setText("Click on below button to pick one sensing task. ");
		setContentView(R.layout.main);

		// get textview object
		tv = (TextView) findViewById(R.id.textView1);
		tv.setMovementMethod(new ScrollingMovementMethod());
		
		publish();
		pick();
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

	public void publish() {
		Button signButton = (Button) findViewById(R.id.button2);
		signButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = getApplicationContext();
				
				// http servlet call
				HttpClient httpclient = new DefaultHttpClient();
				String providerURL = "http://10.1.169.42:10080/McSenseWEB/pages/ClientServlet";
				HttpPost httppost = new HttpPost(providerURL);
				HttpResponse response = null;
				InputStream is = null;
				StringBuilder sb = new StringBuilder();
				
				//read data
				EditText et = (EditText) findViewById(R.id.editText1);
				
				// Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("taskDesc", et.getText().toString()));
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
				String task = sb.toString();
				System.out.println(task);
				
				tv.append("Sensing Task Read: " + task + " \r\n");
				scrollDown();
			}
		});
	}
	
	public void pick() {
		Button signButton = (Button) findViewById(R.id.button1);
		signButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = getApplicationContext();
				// http servlet call
				HttpClient httpclient = new DefaultHttpClient();
				String providerURL = "http://10.1.169.42:10080/McSenseWEB/pages/ProviderServlet";
				providerURL = providerURL + "?type=mobile";
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

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// read task from servlet
				String task = sb.toString();
				System.out.println(task);
				
				tv.append("Sensing Task Read: " + task + " \r\n");
				scrollDown();
			}
		});
	}
	
	public void scrollDown(){
		final ScrollView scroll = (ScrollView) findViewById(R.id.verticalScrollView1);
		scroll.post(new Runnable() {
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
	}
}
