package com.mcsense.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends Activity {
	private static final String PREFS_NAME = "myPref";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		setContentView(R.layout.home);
		
		loadHome();
		
	}
	
	@Override
	public void onBackPressed() {
	  super.onBackPressed();
	}
	
	private void loadHome() {

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
							showToast("passwords donot match!!");
						else {
							setLoginTokens(username, password, repassword);
							loadApp();
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

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String login = settings.getString("login", "");
		String password = settings.getString("password", "");

		if (!login.equals("") && !password.equals("")) {
			// bindToService();
			// if login succeed
			if (login.equals(username) && password.equals(pswd))
				loginInd = true;
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

	protected void setLoginTokens(String username, String password,
			String repassword) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		SharedPreferences.Editor editor = settings.edit();
		editor.putString("login", username);
		editor.putString("password", password);
		// Commit the edits!
		editor.commit();
	}

	private void bindToService() {

	}

}
