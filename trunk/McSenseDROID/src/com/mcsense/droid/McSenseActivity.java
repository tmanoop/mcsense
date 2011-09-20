package com.mcsense.droid;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

import android.util.Base64;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class McSenseActivity extends Activity {

//	protected static final String ip = "192.168.1.3";
//	protected static final String ip = "10.1.168.50";
	protected static final String ip = "manoop.dyndns.org";
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.camera:
	        photo(item);
	        return true;
	    case R.id.help:
	        showHelp();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void showHelp() {
		showToast("Help");
		String imageInSD = "/sdcard/TeamImage.jpg";
		uploadPhoto(BitmapFactory.decodeFile(imageInSD));
	}

	private void photo(MenuItem item) {
		showToast("Photo");
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		File image = new File(Environment.getExternalStorageDirectory(),"TeamImage.jpg");
		camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        this.startActivityForResult(camera, PICTURE_RESULT);
        showToast("Photo Saved!!"+Uri.fromFile(image));
//	    Intent myIntent = new Intent(item.getIntent());
//        startActivity(myIntent);
	}

	private static final int PICTURE_RESULT = 1;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		showToast("requestCode: "+requestCode);
		showToast("resultCode: "+resultCode);
		Bitmap image = null;
	    if(requestCode == PICTURE_RESULT){
	        if(resultCode == Activity.RESULT_OK) {
	            if(data!=null){
	                image = BitmapFactory.decodeFile(data.getExtras().get(MediaStore.Images.Media.TITLE).toString());
	                showToast("Photo Saved!!");
//	                grid.add(image);            
//	                images.addItem(image);
	            }
	            if(data==null){
//	                Toast.makeText(Team_Viewer.this, "no data.", Toast.LENGTH_SHORT).show();
	            }
	        }
	        else if(resultCode == Activity.RESULT_CANCELED) {
//	            Toast.makeText(Team_Viewer.this, "Picture could not be taken.", Toast.LENGTH_SHORT).show();
	        	showToast("RESULT_CANCELED!!");
	        	
	        }
	        showToast("data:!!"+data);
	        preview(image);
//	        uploadPhoto();
	    }
	}
	
	private void preview(Bitmap image){
		showToast("preview!!");
		ImageView bmImage = (ImageView)findViewById(R.id.imageview);
		   BitmapFactory.Options bmOptions;
		   bmOptions = new BitmapFactory.Options();
		   bmOptions.inSampleSize = 1;
		   String imageInSD = "/sdcard/TeamImage.jpg";
		   Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
//		   bmImage.setImageBitmap(bitmap);
		   showToast("preview end!!"+bitmap);
		   scrollDown();
		   uploadPhoto(bitmap);
	}
	
	private void uploadPhoto(Bitmap bitmap){
		showToast("uploading!!");
		// http servlet call
		HttpClient httpclient = new DefaultHttpClient();
		String providerURL = "http://"+ip+":10080/McSenseWEB/pages/PhotoServlet";
		HttpPost httppost = new HttpPost(providerURL);
		HttpResponse response = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		//read data
		EditText et1 = (EditText) findViewById(R.id.editText1);
		EditText et2 = (EditText) findViewById(R.id.editText2);
		
//		String imageInSD = Environment.getExternalStorageDirectory()+"/TeamImage.jpg";
		String imageInSD = "/sdcard/TeamImage.jpg";
		//Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
		bitmap = BitmapFactory.decodeFile(imageInSD);
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		String ba1="";
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
	        byte [] ba = bao.toByteArray();
//			ba1 = Base64.encodeToString(ba,0);
	        ArrayList<String> baList = getBitmapEncodedString(ba);
	        ba1 = baList.get(0);
	        
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			showToast("uploading error!! "+e1.getMessage());
		}
		
		// Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("taskDesc", et1.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("id", et2.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("type", "mobile"));
        nameValuePairs.add(new BasicNameValuePair("image",ba1));
        
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
			bao.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read task from servlet
		String resp = sb.toString();
		System.out.println(resp);
		
		tv.append("Submitted: " + resp + " \r\n");
		scrollDown();
	}
	
	private ArrayList<String> getBitmapEncodedString(byte[] ba) {
		ArrayList<String> ba1 = new ArrayList<String>();
		int chunkSize = 128;
        for(int i =0; i<ba.length;i=i+chunkSize){
//        	int k=0;
//        	int nextSize = i+chunkSize;
//        	if(ba.length<i+chunkSize)
//        		nextSize = ba.length;
//        	byte [] tempBA = new byte[nextSize];
//        	for(int j =i; j<nextSize;j++){
//        		tempBA[k++] = ba[j];		        	
//        	}
//        	ba1 = ba1 + Base64.encodeToString(tempBA,0);
        	try {
				ba1.add(Base64.encodeToString(ba,i,i+chunkSize,Base64.DEFAULT));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast("uploading error!! \r\n"+e.getMessage());
			}
        }
		return ba1;
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
				String providerURL = "http://"+ip+":10080/McSenseWEB/pages/ClientServlet";
				HttpPost httppost = new HttpPost(providerURL);
				HttpResponse response = null;
				InputStream is = null;
				StringBuilder sb = new StringBuilder();
				
				//read data
				EditText et1 = (EditText) findViewById(R.id.editText1);
				EditText et2 = (EditText) findViewById(R.id.editText2);
				
				// Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("taskDesc", et1.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("id", et2.getText().toString()));
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
				
				tv.append("Publish: " + task + " \r\n");
				scrollDown();
			}
		});
	}
	
	public void pick() {
		Button signButton = (Button) findViewById(R.id.button1);
		signButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText et2 = (EditText) findViewById(R.id.editText2);
				String id = et2.getText().toString();
				
				Context context = getApplicationContext();
				// http servlet call
				HttpClient httpclient = new DefaultHttpClient();
				String providerURL = "http://"+ip+":10080/McSenseWEB/pages/ProviderServlet";
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
