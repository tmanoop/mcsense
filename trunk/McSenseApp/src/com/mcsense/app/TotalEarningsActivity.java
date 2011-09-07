package com.mcsense.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class TotalEarningsActivity extends Activity {
	
	TextView textview;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textview = new TextView(this);
        textview.setText("Below are the Total Earnings: \r\n");
        textview.setMovementMethod(new ScrollingMovementMethod());
        
        setContentView(textview);
        
        loadEarnings();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		loadEarnings();
	}
	
	private void loadEarnings() {
		String FILENAME = "hello_file";
		String string = "$100";

		try {
			//writing
			FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(string.getBytes());
			
			//reading
			FileInputStream fis = openFileInput(FILENAME);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String earnings = new String(buffer);
			textview.append(earnings+" \r\n");
			
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
