package com.developers.hack.cs.kagerou.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.developers.hack.cs.kagerou.R;
import com.developers.hack.cs.kagerou.model.KagerouCircle;
import com.developers.hack.cs.kagerou.util.MySQLiteOpenHelper;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KagerouMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = KagerouMapFragment.class.getSimpleName();

    private static final int REQUEST_PERMISSION = 3;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private boolean mResolvingError = false;
    private Location location;
    private long lastLocationTime;
    private LatLng mNowLatLng;
    private Activity mContext;
    private final String DB_NAME = "kagerou.db";
    SQLiteDatabase mKagerouDB;
    MySQLiteOpenHelper mySQLiteOpenHelper;
    String jsonDataString;
    Circle[] circles;
    int all = 0;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private FloatingActionButton fab;

    private GoogleMap mMap;
    private OnLoadFinishListener mListener;
    Circle circle;

    double lat = 35.6585805;
    double lng = 139.7454329;
    private Handler mHandler;
    ArrayList<KagerouCircle> mCircleList;

    public static KagerouMapFragment getInstance() {
        KagerouMapFragment kagerouMapFragment = new KagerouMapFragment();
        return kagerouMapFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        mContext = getActivity();
        mHandler = new Handler(Looper.getMainLooper());
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
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(), DB_NAME, null, 1);
        mKagerouDB = mySQLiteOpenHelper.getWritableDatabase();
        mListener = new OnLoadFinishListener() {

            @Override
            public void onLoadFinish() {
// もしくはLooperでメインスレッドを指定して生成
                mHandler = new Handler(Looper.getMainLooper());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // ここに処理
                            createCircle();
                            for (Circle myCircle: circles) {
                                myCircle.remove();
                            }
                            Log.d(TAG, "End createCircle");
                            final Handler handler = new Handler();
                            Timer timer = new Timer(false);
//                            if(all == 0){
                                timer.schedule(new TimerTask() {
                                                   @Override
                                                   public void run() {
                                                       handler.post(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               for (Circle myCircle: circles) {
                                                                   myCircle.remove();
                                                               }
                                                               createCircle();
                                                               for (int i = 0; i < circles.length; i++) {
                                                                   Log.d(TAG, "moveCircle: " + i + ",Lat:" + mCircleList.get(i).getLng() + ",Lng:" + mCircleList.get(i).getLat());
                                                                   circles[i].setCenter(new LatLng(mCircleList.get(i).getLng(), mCircleList.get(i).getLat()));
                                                               }
                                                               mySQLiteOpenHelper.updateCircleDB(mKagerouDB);
                                                               mySQLiteOpenHelper.hitCircle(mKagerouDB);
                                                               mCircleList = mySQLiteOpenHelper.loadCircleDB(mKagerouDB);
                                                           }
                                                       });
                                                   }
                                               },
                                        0, 10000);

                            }
 //                   }
                });
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_maps, null);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click fab");
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.flagment_key_lat), String.valueOf(location.getLatitude()));
                bundle.putString(getString(R.string.flagment_key_lng), String.valueOf(location.getLongitude()));
                PostFragment postFragment = new PostFragment();
                postFragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, postFragment)
                        .addToBackStack(null)
                        .commit();
                mySQLiteOpenHelper.updateCircleDB(mKagerouDB);
//                Log.d(TAG,"updateCircleDB: start");
//                mySQLiteOpenHelper.updateCircleDB(mKagerouDB);
//                mFragmentManager = getFragmentManager();
//                mTransaction = mFragmentManager.beginTransaction();
//                mTransaction.add(R.id.frame_container, new PostFragment());
//                mTransaction.addToBackStack(null);
//                mTransaction.commit();
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
                                       Random random = new Random();
                                       double addLat = (random.nextDouble()) / 10000;
                                       double addLng = (random.nextDouble()) / 10000;
                                       lat = lat + addLat;
                                       lng = lng + addLng;
                                       circle.setCenter(new LatLng(lat, lng));
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
        all = 0;
    }
    @Override
    public void onStop() {
        super.onStop();
        stopFusedLocation();
        all = 1;
        Log.d(TAG," onstop" + all);
        for (Circle myCircle: circles) {
            myCircle.remove();
        }
        mySQLiteOpenHelper.resetCircleTable(mKagerouDB);
        mySQLiteOpenHelper.loadCircleDB(mKagerouDB);
        for (int i = 0; circles.length > i; i++) {
            circles[i].remove();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mySQLiteOpenHelper.close();
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

        // Add a marker in Tokyo Tower and move the camera
        LatLng tokyo_tower = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(tokyo_tower).title("Tokyo Tower"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tokyo_tower));

        //circle
        final CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(100)
                .strokeWidth(5)
                .strokeColor(0xe1285577)
                .fillColor(0xaa2f7b8e);
//        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
//            @Override
//            public void onCircleClick(Circle circle) {
//                Log.d(TAG, "onClickCircle");
//                mFragmentManager = getFragmentManager();
//                mTransaction = mFragmentManager.beginTransaction();
//                mTransaction.add(R.id.frame_container, new DetailFragment());
//                mTransaction.addToBackStack(null);
//                mTransaction.commit();
//
//                Log.d(TAG, "End of onClickCircle"+circle.getId()+"-----"+circle.getZIndex());
//            }
//        });
        circle = mMap.addCircle(circleOptions);
        circle.setClickable(true);
    }

    void sendRequest(Location location) {
        OkHttpClient client = new OkHttpClient();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint) + "/maps/get_near/" + longitude + "/" + latitude)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response respxonse) throws IOException {
                if (respxonse.isSuccessful()) {
                    Log.d(TAG, "成功");
                    jsonDataString = respxonse.body().string();
                    mySQLiteOpenHelper.resetCircleTable(mKagerouDB);
                    try {
                        mySQLiteOpenHelper.insertCircleDB(jsonDataString, mKagerouDB);
                        mCircleList = mySQLiteOpenHelper.loadCircleDB(mKagerouDB);
                        mListener.onLoadFinish();
                        Log.d(TAG, "onResponse: END");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "失敗");
                }
                respxonse.close();

            }
        });
    }

    void getComments() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint) + "/maps/get_comments/")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response respxonse) throws IOException {
                if (respxonse.isSuccessful()) {
                    Log.d(TAG, "getComments 成功");
                    String jsondata = respxonse.body().string();
                    mySQLiteOpenHelper.resetCommentDB(mKagerouDB);
                    try {
                        mySQLiteOpenHelper.insertCommentDB(jsondata, mKagerouDB);
                        mySQLiteOpenHelper.loadCommentDB(mKagerouDB);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "getComments 失敗");
                }
                respxonse.close();

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

            sendRequest(location);
            getComments();
            Log.d(TAG, "onPostExecute");
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

    public void createCircle() {
        //circle
        Log.d(TAG, "createCircle: ");
        circles = new Circle[mCircleList.size()];
        if (mCircleList != null) {
            for (int i = 0; i < mCircleList.size(); i++) {
                Log.d(TAG, "createCircle: " + i);
                final CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(mCircleList.get(i).getLng(), mCircleList.get(i).getLat()))
                        .radius(mCircleList.get(i).getRadius() * 20)
                        .strokeWidth(5)
                        .strokeColor(0xe1285577)
                        .fillColor(0xaa2f7b8e);

                Log.d(TAG, mCircleList.get(i).getLng() + "と" + mCircleList.get(i).getLat());

//                final CircleOptions circleOptions = new CircleOptions().center(new LatLng(lat, lng))
//                        .radius(100)
//                        .strokeWidth(5)
//                        .strokeColor(0xe1285577)
//                        .fillColor(0xaa2f7b8e);
                final ArrayList<KagerouCircle> CircleList = mCircleList;
                final KagerouCircle kagerouCircle = mCircleList.get(i);
                Circle circleTemp;

                circleTemp = mMap.addCircle(circleOptions);
                circleTemp.setClickable(true);
                circleTemp.setZIndex(i);
                circles[i] = circleTemp;
            }
            mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                    @Override
                    public void onCircleClick(Circle circle) {
                        Log.d(TAG, "onClickCircle");
                        int zIndex = (int)circle.getZIndex();
                        String title = mCircleList.get(zIndex).getTitle();
                        String date = mCircleList.get(zIndex).getCreated_at();
                        String name = mCircleList.get(zIndex).getName();
                        String content = mCircleList.get(zIndex).getContent();
                        String circle_id = String.valueOf(mCircleList.get(zIndex).getCircle_id());

                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.post_title), title);
                        bundle.putString(getString(R.string.post_date), date);
                        bundle.putString(getString(R.string.post_name), name);
                        bundle.putString(getString(R.string.post_content), content);
                        bundle.putString(getString(R.string.post_circle_id), circle_id);
                        DetailFragment detailFragment = new DetailFragment();
                        detailFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction()
                                .add(R.id.frame_container, detailFragment)
                                .addToBackStack(null)
                                .commit();
                    Log.d(TAG, "End of onClickCircle");
                }
            });
        }
    }

    public interface OnLoadFinishListener {
        void onLoadFinish();
    }
}