package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.developers.hack.cs.kagerou.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == button){
            Intent intent = new Intent(this,MainActivity.class);
                startActivityForResult(intent,0);
        }
    }
}
