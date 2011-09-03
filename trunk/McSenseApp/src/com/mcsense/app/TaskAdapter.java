package com.mcsense.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	private ArrayList<Task> items;

    public TaskAdapter(Context context, int textViewResourceId, ArrayList<Task> items) {
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
            Task t = items.get(position);
            System.out.println("Task: "+t.taskDescription);
            if (t != null) {
                    TextView tt = (TextView) v.findViewById(R.id.toptext);
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    if (tt != null) {
                          tt.setText("Name: "+t.taskDescription);                            
                    }
                    if(bt != null){
                          bt.setText("Status: "+ t.taskStatus);
                    }
            }
            return v;
    }
    
}