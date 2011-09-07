package com.mcsense.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
                    if (tt != null) {
                    	if(t.getTaskDescription().equals("No New Tasks"))
                    		tt.setText(t.getTaskDescription());
                    	else
                    		tt.setText("Name: "+t.getTaskDescription());                            
                    }
                    if(bt != null){
                    	if(t.getTaskStatus()!=null)
                    		bt.setText("Status: "+ t.getTaskStatus());
                    }
            }
            return v;
    }
    
}