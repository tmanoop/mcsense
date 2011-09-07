package com.mcsense.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.mcsense.json.JTask;

public class TaskActivity extends Activity {
	private static final int PICTURE_RESULT = 1;
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
        text.setText(jTaskObjInToClass.getTaskName());
        
        loadTaskDetails(jTaskObjInToClass);
        
//        iniAccel();
        
    }
	
	private void loadTaskDetails(JTask jTask) {
//		LinearLayout linearLayout =  (LinearLayout)findViewById(R.id.linearLayout1);
		
		TextView taskID = (TextView) findViewById(R.id.taskID); 
		taskID.setText(jTask.getTaskId()+"");
		
		TextView taskStatus = (TextView) findViewById(R.id.taskStatus); 
		taskStatus.setText(jTask.getTaskStatus());
		
		TextView taskType = (TextView) findViewById(R.id.taskType); 
		taskType.setText(jTask.getTaskType());
		
		TextView providerID = (TextView) findViewById(R.id.providerID); 
		providerID.setText(jTask.getProviderPersonId()+"");
		final Button button = (Button) findViewById(R.id.button1); 
		
		final String tskType = jTask.getTaskType();
		String buttonName = "";
		if(tskType.equals("campusSensing"))
			buttonName = "Start Sensing";
		else if(tskType.equals("photo"))
			buttonName = "Take Photo";
		button.setText(buttonName);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(tskType.equals("campusSensing")){
            		iniAccel();
            	} else if(tskType.equals("photo")){
//            		showToast("Start Camera");
            		photo();
            	}
            }
        });
		
//		TextView acelValues = (TextView) findViewById(R.id.acelValues); 
//		String acelVal = getAccelValues();
//		acelValues.setText(acelVal);
//		linearLayout.addView(t);
		
	}
	
	private void photo() {
//		showToast("Photo");
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		File image = new File(Environment.getExternalStorageDirectory(),"TeamImage.jpg");
		camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        this.startActivityForResult(camera, PICTURE_RESULT);
        showToast("Photo Saved!!"+Uri.fromFile(image));
//	    Intent myIntent = new Intent(item.getIntent());
//        startActivity(myIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
//		showToast("requestCode: "+requestCode);
//		showToast("resultCode: "+resultCode);
		Bitmap image = null;
	    if(requestCode == PICTURE_RESULT){
	        if(resultCode == Activity.RESULT_OK) {
	            if(data!=null){
	                image = BitmapFactory.decodeFile(data.getExtras().get(MediaStore.Images.Media.TITLE).toString());
//	                showToast("Photo Saved!!");
//	                grid.add(image);            
//	                images.addItem(image);
	            }
	            if(data==null){
//	                Toast.makeText(Team_Viewer.this, "no data.", Toast.LENGTH_SHORT).show();
	            }
	        }
	        else if(resultCode == Activity.RESULT_CANCELED) {
//	            Toast.makeText(Team_Viewer.this, "Picture could not be taken.", Toast.LENGTH_SHORT).show();
//	        	showToast("RESULT_CANCELED!!");
	        	
	        }
//	        showToast("data:!!"+data);
	        preview(image);
//	        uploadPhoto();
	    }
	}
	
	private void preview(Bitmap image){
//		showToast("preview!!");
		ImageView bmImage = (ImageView)findViewById(R.id.imageView1);//new ImageView(this);//
		   BitmapFactory.Options bmOptions;
		   bmOptions = new BitmapFactory.Options();
		   bmOptions.inSampleSize = 1;
		   String imageInSD = "/sdcard/TeamImage.jpg";
		   Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
		   bmImage.setImageBitmap(bitmap);
		   
//		   TableLayout tb = new TableLayout(this);
//		   
//		   tb.addView(bmImage,  new LayoutParams(1000,600));
		   
//		   addContentView(bmImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		   showToast("preview end!!"+bitmap);
//		   scrollDown();
//		   uploadPhoto(bitmap);
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
