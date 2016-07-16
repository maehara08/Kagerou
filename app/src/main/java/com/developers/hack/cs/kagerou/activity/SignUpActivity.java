package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.developers.hack.cs.kagerou.R;

public class SignUpActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private Button signUp_button;
    private EditText userName_editText;
    private EditText password_editText;
    private EditText age_editText;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        signUp_button = (Button)findViewById(R.id.signUp_button);
        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"signUp_button_onClick");
                Intent intent = new Intent(getApplication(),MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
