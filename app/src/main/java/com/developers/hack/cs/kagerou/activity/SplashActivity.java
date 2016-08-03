package com.developers.hack.cs.kagerou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.developers.hack.cs.kagerou.R;

public class SplashActivity extends Activity {
    private static final String TAG=SplashActivity.class.getSimpleName();

    SharedPreferences pref;

    int loginJudge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pref = getSharedPreferences("kagerou", Context.MODE_PRIVATE);

        Handler hdl = new Handler();
        hdl.postDelayed(new splash(), 5000);
    }

    class splash implements Runnable {
        @Override
        public void run() {
            loginJudge=pref.getInt(getString(R.string.login_judge),0);
            if(loginJudge==1) {
                Log.d(TAG,"loginをパス");
                Intent intent = new Intent(getApplication(), BaseActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }else{
                Log.d(TAG,"login画面へ");
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }
    }
}