package com.developers.hack.cs.kagerou.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developers.hack.cs.kagerou.MyDBEntity;
import com.developers.hack.cs.kagerou.MyDBHelper;
import com.developers.hack.cs.kagerou.MyDao;
import com.developers.hack.cs.kagerou.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KagerouMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = KagerouMapFragment.class.getSimpleName();

    private static final int REQUEST_PERMISSION = 3;

    private MyDao dao;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private boolean mResolvingError = false;
    private Location location;
    private long lastLocationTime;
    private LatLng mNowLatLng;
    private Activity mContext;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private FloatingActionButton fab;

    private GoogleMap mMap;
    Circle circle;

    double lat;
    double lng;

    int firstTime=0;

    public static KagerouMapFragment getInstance() {
        KagerouMapFragment kagerouMapFragment = new KagerouMapFragment();
        return kagerouMapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        MyDBHelper helper = new MyDBHelper(getContext(), null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        dao = new MyDao(db);
        if(firstTime==0){
            dao.insert(35.681298,139.76624689999994,800,"0xaaa8ab39","0xe1a8ab39");
            dao.insert(35.6581003,139.70174169999996,700,"0xaa359f79","0xe1359f79");
            dao.insert(35.633998,139.715828,950,"0xaa832f8d","0xe1832f8d");
            dao.insert(35.6284713,139.73875969999995,1000,"0xaa892e2f","0xe1892e2f");
            dao.insert(35.6953874,139.7000494,1300,"xaa3551a0","0xe13551a0");
            firstTime=1;
        }
        mContext = getActivity();
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(16);

        fusedLocationProviderApi = LocationServices.FusedLocationApi;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, null);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click fab");
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                mTransaction.add(R.id.container, new PostFragment());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       List<MyDBEntity> entityList = dao.findAll();
                                       for(MyDBEntity entity:entityList) {
                                           Random random = new Random();
                                           double addLat = (random.nextDouble()-0.5) / 5000;
                                           double addLng = (random.nextDouble()-0.5) / 5000;
                                           lat = entity.getLatValue() + addLat;
                                           lng = entity.getLngValue() + addLng;
                                           entity.setLatValue(lat);
                                           entity.setLngValue(lng);
                                           circle.setCenter(new LatLng(entity.getLatValue(), entity.getLatValue()));
                                       }
                                   }
                               });
                           }
                       },
                1800, 1800);
    }

    @Override
    public void onStart() {
        super.onStart();
        startFusedLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopFusedLocation();
    }

    private void startFusedLocation() {
        Log.d(TAG, "startFused");
        //Connect the Client
        if (!mResolvingError) {
            //Connect the Client
            mGoogleApiClient.connect();
        } else {
            Log.d(TAG, String.valueOf(mResolvingError));
        }
    }

    private void stopFusedLocation() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        Log.d(TAG, "stopFusedLocation");
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

        List<MyDBEntity> entityList = dao.findAll();
        for(MyDBEntity entity:entityList) {
            final CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(entity.getLatValue(), entity.getLngValue()))
                    .radius(entity.getRadiusValue())
                    .strokeWidth(5)
                    .strokeColor(Integer.valueOf(entity.getOColorValue()))
                    .fillColor(Integer.valueOf(entity.getIColorValue()));
            circle = mMap.addCircle(circleOptions);
            circle.setClickable(true);
        }

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                Log.d(TAG, "onClickCircle");
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onConnected: return");
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

            return;
        }
        Location fusedLocation = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);

        if (fusedLocation != null && fusedLocation.getTime() > 20000) {
            location = fusedLocation;
            String textLog = "";
            textLog += "---------- onConnected \n";
            textLog += "Latitude=" + String.valueOf(location.getLatitude()) + "\n";
            textLog += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
            textLog += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
            textLog += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
            textLog += "Time=" + String.valueOf(location.getTime()) + "\n";
            textLog += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
            textLog += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Now"));

            Log.d(TAG, textLog);
        } else {
            //バックグラウンドから戻ると例外発生することがある
            try {
                //
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                // Schedule a Thread to unregister location listeners
                Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                    @Override
                    public void run() {
                        fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, KagerouMapFragment.this);
                    }
                }, 60000, TimeUnit.MILLISECONDS);


            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(mContext, "例外が発生、位置情報のPermissionを許可していますか？", Toast.LENGTH_SHORT);
                toast.show();

                //MainActivityに戻す
//                finish();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        lastLocationTime = location.getTime() - lastLocationTime;
        String textLog = "";
        textLog += "---------- onLocationChanged \n";
        textLog += "Latitude=" + String.valueOf(location.getLatitude()) + "\n";
        textLog += "Longitude=" + String.valueOf(location.getLongitude()) + "\n";
        textLog += "Accuracy=" + String.valueOf(location.getAccuracy()) + "\n";
        textLog += "Altitude=" + String.valueOf(location.getAltitude()) + "\n";
        textLog += "Time=" + String.valueOf(location.getTime()) + "\n";
        textLog += "Speed=" + String.valueOf(location.getSpeed()) + "\n";
        textLog += "Bearing=" + String.valueOf(location.getBearing()) + "\n";
        textLog += "time= " + String.valueOf(lastLocationTime) + " msec \n";
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Now"));

        Log.d(TAG, "onLocationChanged: " + textLog);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");

        if (mResolvingError) {
            // Already attempting to resolve an error.
            Log.d("", "Already attempting to resolve an error");

            return;
        } else if (connectionResult.hasResolution()) {

        } else {
            mResolvingError = true;
        }
    }
}