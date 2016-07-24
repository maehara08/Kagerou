package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.developers.hack.cs.kagerou.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button login_button;
    private Button signUp_button;
    private EditText userName_editText;
    private EditText password_editText;

    public void login() {
        Log.d(TAG, "login");
        Intent intent = new Intent(getApplication(), BaseActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        login_button = (Button) findViewById(R.id.login_button);
        signUp_button = (Button) findViewById(R.id.signUp_button);
        userName_editText = (EditText) findViewById(R.id.userName_editText);
        password_editText = (EditText) findViewById(R.id.userName_editText);
        final String userName = userName_editText.getText().toString();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "login_button_onClick");
                Intent intent = new Intent(getApplication(), BaseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "signUp_button_onClick");
                Intent intent = new Intent(getApplication(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
