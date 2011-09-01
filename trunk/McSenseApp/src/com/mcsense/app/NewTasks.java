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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewTasks extends Activity {
	
	Button pickButton;
	TextView textview;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout panel = new LinearLayout(this);
 		panel.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
 				LayoutParams.WRAP_CONTENT));
 		panel.setOrientation(LinearLayout.VERTICAL);
     
        pickButton = new Button(this);
        pickButton.setText("Refresh");
        pickButton.setLayoutParams(new LayoutParams(
        	    ViewGroup.LayoutParams.WRAP_CONTENT,
        	    ViewGroup.LayoutParams.WRAP_CONTENT));
        panel.addView(pickButton);
        
        textview = new TextView(this);
        textview.setText("Below are the New Tasks:  \r\n");
        textview.setMovementMethod(new ScrollingMovementMethod());
        panel.addView(textview);
        
        setContentView(panel);
		pick();
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
	    //Restore state here
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
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
