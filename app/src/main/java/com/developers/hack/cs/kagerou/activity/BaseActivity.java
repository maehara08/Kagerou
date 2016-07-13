package com.developers.hack.cs.kagerou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.developers.hack.cs.kagerou.R;
import com.developers.hack.cs.kagerou.fragment.KagerouMapFragment;

public class BaseActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.frame_container, KagerouMapFragment.getInstance());
        mTransaction.commit();
    }
}
