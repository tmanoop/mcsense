package com.mcsense.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.mcsense.json.JTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class TotalEarningsActivity extends Activity {
	
	TextView textview;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        textview = new TextView(this);
//        textview.setText("Below are the Total Earnings: \r\n");
//        textview.setMovementMethod(new ScrollingMovementMethod());
//        
//        setContentView(textview);
        
//        loadEarnings();
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		textview = new TextView(this);
        textview.setMovementMethod(new ScrollingMovementMethod());
        
        setContentView(textview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		textview = new TextView(this);
        textview.setMovementMethod(new ScrollingMovementMethod());
        
        setContentView(textview);
		loadEarnings();
	}
	
	private void loadEarnings() {
		Double totalEarnings = 0.0;

		try {
			if(AppConstants.jTaskCompletedList==null)
				AppConstants.jTaskCompletedList = AppUtils.loadTasks("C",getApplicationContext());
			ArrayList<JTask> jTaskList = AppConstants.jTaskCompletedList;
			if(jTaskList.size()!=0 && !jTaskList.get(0).getTaskDescription()
					.equals("No Completed Tasks")){
				for(JTask task : jTaskList){
					if(task.getTaskStatus().equals("C")){
						if(task.getClientPay() != null)
							totalEarnings = totalEarnings + task.getClientPay();
					}
				}
			}			
			textview.setText("Below are the Total Earnings: \r\n $"+AppUtils.roundTwoDecimals(totalEarnings)+" \r\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
