package com.developers.hack.cs.kagerou.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.developers.hack.cs.kagerou.R;

public class SplashActivity extends Activity {
    private static final String TAG=SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Handler hdl = new Handler();
        hdl.postDelayed(new splash(), 5000);
    }

    class splash implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}