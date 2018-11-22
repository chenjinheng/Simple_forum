package com.example.libarary.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.MapView;
import com.example.libarary.R;

public class MapActivity extends AppCompatActivity {
    MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
    }
}
