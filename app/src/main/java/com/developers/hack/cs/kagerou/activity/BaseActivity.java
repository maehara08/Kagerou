package com.developers.hack.cs.kagerou.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.developers.hack.cs.kagerou.R;
import com.developers.hack.cs.kagerou.fragment.KagerouMapFragment;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.frame_container, KagerouMapFragment.getInstance());
        mTransaction.commit();
        mNavigationView = (NavigationView)findViewById(R.id.drawer_navigationview);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_item_menu_notification:
                        Log.d(TAG,"drawer_item_menu_notification");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
