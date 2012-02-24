package com.mcsense.app;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class GMapsActivity extends MapActivity {
    
    private MapView mapView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);
        
        mapView = (MapView) findViewById(R.id.map_view);       
        mapView.setBuiltInZoomControls(true);
        
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
