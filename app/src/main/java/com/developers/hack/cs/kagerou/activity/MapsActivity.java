package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.developers.hack.cs.kagerou.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static String TAG="tag";

    private GoogleMap mMap;
    Circle circle;

    double lat=35.6585805;
    double lng=139.7454329;

    double lat2=34.6585805;
    double lng2=138.7454329;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tokyo_tower = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(tokyo_tower).title("Tokyo Tower"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tokyo_tower));

        //circle
        final CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lng))
//                .center(new LatLng(Lat, Lng))
                .radius(100)
                .strokeWidth(5)
                .strokeColor(0xe1285577)
                .fillColor(0xaa2f7b8e);

        circle=mMap.addCircle(circleOptions);
    }

    @Override
    public void onResume(){
        super.onResume();

        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       Random random = new Random();
                                       double addLat=(random.nextDouble())/10000;
                                       double addLng=(random.nextDouble())/10000;
                                       Log.d("tag","addLat:"+addLat);
                                       Log.d("tag","addLng:"+addLng);
                                       lat=lat+addLat;
                                       lng=lng+addLng;
                                       circle.setCenter(new LatLng(lat,lng));
                                   }
                               });
                           }
                       },
                1800,1800);
    }
}
