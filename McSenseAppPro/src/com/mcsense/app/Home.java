package com.mcsense.app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mcsense.security.SimpleCrypto;

public class Home extends Activity {
	private static final String PREFS_NAME = "myPref";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		loadHome();
		
	}
	
	@Override
	public void onBackPressed() {
	  super.onBackPressed();
	  finish();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
	
	private void loadHome() {
		setContentView(R.layout.home);
		
		Button loginButton = (Button) findViewById(R.id.button1);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showLoginDialogue();
			}
		});

		Button regButton = (Button) findViewById(R.id.button2);
		regButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showRegDialogue();
			}
		});
	}

	protected void loadApp() {
		this.finish();
		Intent i = new Intent(getApplicationContext(), Main.class);
        startActivity(i);
	}

	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

	protected void showRegDialogue() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.register, null);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Please Register to McSense");
		// alert.setMessage("Enter your email and password");
		// Set an EditText view to get user input
		alert.setView(textEntryView);
		AlertDialog loginPrompt = alert.create();

		final EditText input1 = (EditText) textEntryView
				.findViewById(R.id.username);
		final EditText input2 = (EditText) textEntryView
				.findViewById(R.id.password);
		final EditText input3 = (EditText) textEntryView
				.findViewById(R.id.repassword);

		alert.setPositiveButton("Register",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String username = input1.getText().toString();
						String password = input2.getText().toString();
						String repassword = input3.getText().toString();
						if (!password.equals(repassword))
							showToast("Passwords do not match!!");
						else {
							boolean loginInd = setLoginTokens(username, password, repassword);
							if (loginInd) {
								// if login pass, load app
								loadApp();
							} else{
								showToast("Registration Failed!!");
								showRegDialogue();
							}
						}
					}
				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();
	}

	private boolean checkLoginTokens(String username, String pswd) {
		boolean loginInd = false;

		String result = bindToServer(username, pswd, "login");		
		try {
			// if login succeed, then int is parsed, or exception is thrown. Make it more robust when get time. otherwise this may be vulnerable to some login attacks.
			int providerId = Integer.parseInt(result);
			SharedPreferences settings = getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("login", username);
			editor.putString("emailID", username);
			editor.putString("providerId", providerId + "");
			editor.commit();
			loginInd = true;
		} catch (Exception e) {
			loginInd = false;
		}		
		return loginInd;
	}

	private void showLoginDialogue() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.userpasslayout,
				null);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Please Login to McSense");
		// alert.setMessage("Enter your email and password");
		// Set an EditText view to get user input
		alert.setView(textEntryView);
		AlertDialog loginPrompt = alert.create();

		final EditText input1 = (EditText) textEntryView
				.findViewById(R.id.username);
		final EditText input2 = (EditText) textEntryView
				.findViewById(R.id.password);

		alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String username = input1.getText().toString();
				String password = input2.getText().toString();
				boolean loginInd = checkLoginTokens(username, password);
				if (loginInd) {
					// if login pass, load app
					loadApp();
				} else{
					showToast("Login Failed!!");
					showLoginDialogue();
				}
				
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();

	}

	protected boolean setLoginTokens(String username, String password,
			String repassword) {
		String result = bindToServer(username, password, "register");
		try {
			// if login succeed
			int providerId = Integer.parseInt(result);
			SharedPreferences settings = getSharedPreferences(PREFS_NAME,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("login", username);
			editor.putString("providerId", providerId + "");
			editor.commit();
		} catch (Exception e) {
			if(result.equals("exist"))
				showToast("Email or Phone already registered!!");
			return false;
		}
		return true;
	}

	private String bindToServer(String emailId, String password, String reqType) {
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = AppConstants.ip+"/McSenseWEB/LoginServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		//get meid
		String meid = AppUtils.getMeid(getApplicationContext());
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("taskDesc", currentTask.getTaskDescription()));
        nameValuePairs.add(new BasicNameValuePair("emailId", emailId));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("meid", meid));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        nameValuePairs.add(new BasicNameValuePair("reqType", reqType));
        
		// Execute HTTP Get Request
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			Log.d(AppConstants.TAG, "Reading response...");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				Log.d(AppConstants.TAG, ""+sb);
			}
			is.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast("Server not reachable");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast("Server not reachable");
		}

		// read task from servlet
		String resp = sb.toString().trim();
		Log.d(AppConstants.TAG, resp);
		
		return resp;
//		showToast("Uploaded: \r\n");
	}
	
}
