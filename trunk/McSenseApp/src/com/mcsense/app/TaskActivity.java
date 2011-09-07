package com.mcsense.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcsense.json.JTask;

public class TaskActivity extends Activity {
	LinearLayout linear;

	TextView text;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        linear = new LinearLayout(this);
//
//        linear.setOrientation(LinearLayout.VERTICAL);
//
//        text = new TextView(this);
//
//
//        text.setText("This is an example for the Bright Hub!");
//
//        linear.addView(text);

//        setContentView(linear);
        setContentView(R.layout.task);
        
        JTask jTaskObjInToClass = getIntent().getExtras().getParcelable("JTask");
//        showToast(jTaskObjInToClass.getTaskDescription());
        text = (TextView) findViewById(R.id.txtTest);
        text.setText(jTaskObjInToClass.getTaskDescription());
        
        loadTaskDetails(jTaskObjInToClass);
        
//        iniAccel();
        
    }
	
	private void loadTaskDetails(JTask jTask) {
//		LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.linearLayout1);
		
//		TextView taskID = (TextView) findViewById(R.id.taskID); 
//		taskID.setText(jTask.getTaskId());
		
		TextView taskStatus = (TextView) findViewById(R.id.taskStatus); 
		taskStatus.setText(jTask.getTaskStatus());
		
		TextView taskType = (TextView) findViewById(R.id.taskType); 
		taskType.setText(jTask.getTaskType());
		
//		TextView providerID = (TextView) findViewById(R.id.providerID); 
//		providerID.setText(jTask.getProviderPersonId());
		final Button button = (Button) findViewById(R.id.button1); 
		button.setText("Accelerometer Test");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iniAccel();
            }
        });
		
		TextView acelValues = (TextView) findViewById(R.id.acelValues); 
		String acelVal = getAccelValues();
		acelValues.setText(acelVal);
//		linearLayout.addView(t);
		
	}
	
	private String getAccelValues() {
		String FILENAME = "accel_file";
		String acelValues = "";
		try {
			//reading
			FileInputStream fis = openFileInput(FILENAME);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			acelValues = new String(buffer);
			
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return acelValues;
	}

	private void iniAccel() {
		Intent i = new Intent(getApplicationContext(), Sensors.class);
        startActivity(i);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	public void showToast(String msg) {
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
}
