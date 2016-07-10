package com.developers.hack.cs.kagerou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.developers.hack.cs.kagerou.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,MapsActivity.getInstance()).commit();
    }
}
