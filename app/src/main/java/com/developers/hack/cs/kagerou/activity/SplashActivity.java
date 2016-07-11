package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.developers.hack.cs.kagerou.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Handler hdl = new Handler();
        hdl.postDelayed(new splash(), 5000);
    }

    class splash implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(getApplication(), MainActivity.class);

            startActivity(intent);

            SplashActivity.this.finish();
        }
    }
}