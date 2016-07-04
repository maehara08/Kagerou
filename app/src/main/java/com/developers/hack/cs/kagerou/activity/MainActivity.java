package com.developers.hack.cs.kagerou.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.developers.hack.cs.kagerou.R;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSION = 10;
    private static final int REQUEST_CODE = 1;
    private LocationManager LocationManager;

    TextView textView_x;
    TextView textView_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_x = (TextView) findViewById(R.id.textView_x);
        textView_y = (TextView) findViewById(R.id.textView_y);
        LocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (CheckPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

        } else {
            if (isGpsEnable()) locationStart();
        }
    }

    protected void locationStart() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
//
//            return;
//        }
        Log.d(TAG, "LocationManagerStart");

        LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.01f, this);
        Location location = LocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            textView_x.setText("Latitude" + location.getLatitude());
            textView_y.setText("Logitude" + location.getLongitude());
        }
    }

    protected boolean isGpsEnable() {
        final boolean gpsEnabled = LocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(settingsIntent, REQUEST_CODE);
        }
        return gpsEnabled;
    }

    protected boolean CheckPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationStart();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        textView_x.setText("Latitude" + location.getLatitude());
        textView_y.setText("Logitude" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged");
        TextView textView = (TextView) (findViewById(R.id.provider));
        TextView textView1 = (TextView) findViewById(R.id.status);
        textView.setText(provider);
        switch (status) {
            case LocationProvider.AVAILABLE:
                textView1.setText(provider + "が利用できます");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                textView1.setText(provider + "が圏外で利用できません");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                textView1.setText("一時的に利用できません");
                break;
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled");
    }
}
