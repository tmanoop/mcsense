package com.mcsense.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcsense.json.JTask;

public class TaskAdapter extends ArrayAdapter<JTask> {
	private ArrayList<JTask> items;

    public TaskAdapter(Context context, int textViewResourceId, ArrayList<JTask> items) {
            super(context, textViewResourceId, items);
            this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            JTask t = items.get(position);
            System.out.println("Task: "+t.getTaskDescription());
            if (t != null) {
                    TextView tt = (TextView) v.findViewById(R.id.toptext);
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    ImageView iv = (ImageView) v.findViewById(R.id.icon);
                    if (tt != null) {
                    	if(t.getTaskDescription().startsWith("No"))
                    		tt.setText(t.getTaskDescription());
                    	else
                    		tt.setText("Name: "+t.getTaskName());                            
                    }
                    if(bt != null){
                    	if(t.getTaskType()!=null && !t.getTaskType().equals("")){
                    		if(t.getTaskType().equals("Server not reachable")){
                    			bt.setText(t.getTaskType());
                    			bt.setTextColor(Color.YELLOW);
                    		} else
                    			bt.setText("Type: "+ t.getTaskType());
                    	}	
                    }
                    if(iv != null){
//                    	SharedPreferences settings = getContext().getSharedPreferences(AppConstants.PREFS_NAME, 0);
//                    	String taskID = settings.getString("taskID", "");
//                    	String status = settings.getString("status", "");
                    	
//                    	String elapsedTime = settings.getString("elapsedTime", "");
//        				long elapsedTimeMillis = Long.parseLong(elapsedTime);
//        				float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
//        				int min = Math.round(elapsedTimeMin);
//        				System.out.println("taskID: "+taskID.trim()+" status: "+AppConstants.status);
//        				if(min<3){
//        					iv.setImageResource(R.drawable.ic_menu_stop);
//        				}

//                    	int tID = Integer.parseInt(taskID);
                    	if(t.getTaskStatus()!=null && t.getTaskStatus().equals("E")){
//                    		System.out.println("failed taskID: "+taskID.trim());
                    		iv.setImageResource(R.drawable.ic_menu_stop);
                    	}
//                    	if(AppConstants.status.equals("E") && tID == t.getTaskId())
//                    		iv.setImageResource(R.drawable.ic_menu_stop);
                    	else if(t.getTaskStatus()!=null && t.getTaskStatus().equals("C"))
                    		iv.setImageResource(R.drawable.ic_menu_tick);
                    	else if(t.getTaskStatus()!=null && t.getTaskStatus().equals("U"))
                    		iv.setImageResource(R.drawable.ic_menu_upload);
                    	if(t.getTaskType()!=null && t.getTaskType().equals("photo") &&
                    			t.getTaskStatus()!=null && (t.getTaskStatus().equals("P") || t.getTaskStatus().equals("IP")))
                    		iv.setImageResource(R.drawable.ic_action_photo);
                    }
            }
            return v;
    }
    
}