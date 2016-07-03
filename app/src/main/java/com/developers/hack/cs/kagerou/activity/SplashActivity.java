package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.developers.hack.cs.kagerou.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);
        Handler hdl = new Handler();

        hdl.postDelayed(new splash(),5000);

//        button = (Button)findViewById(R.id.button);
//        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == button){
            Intent intent = new Intent(this,MainActivity.class);
                startActivityForResult(intent,0);
        }
    }
    class splash implements Runnable{
        @Override
        public void run() {
            Intent intent = new Intent(getApplication(), MainActivity.class);

            startActivity(intent);

            SplashActivity.this.finish();
        }
    }
}
