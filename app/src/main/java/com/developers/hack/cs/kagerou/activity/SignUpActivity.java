package com.developers.hack.cs.kagerou.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.developers.hack.cs.kagerou.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        userName_editText = (EditText) findViewById(R.id.userName_editText);
        password_editText = (EditText) findViewById(R.id.password_editText);
        age_editText = (EditText) findViewById(R.id.age_editText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        signUp_button = (Button) findViewById(R.id.signUp_button);
        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "signUp_button_onClick");
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        postSignUp();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Log.d(TAG, "onPostExecute");
                    }
                }.execute();
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, "onCheckedChanged");

        if (checkedId != -1) {
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            String gender = radioButton.getText().toString();
            Log.d(TAG, gender);
        } else {
            Log.d(TAG, "not select");
        }
    }

    private void postSignUp() {
        OkHttpClient client = new OkHttpClient();
        final String userName = userName_editText.getText().toString();
        final String password = password_editText.getText().toString();
        final String age = age_editText.getText().toString();
        int checkedId = radioGroup.getCheckedRadioButtonId();
        String mSex = "";

        if (userName == null || password == null || age == null || checkedId == -1) {
            return;
        }

        if (checkedId == R.id.male_radioButton) {
            mSex = "male";
        } else if (checkedId == R.id.female_radioButton) {
            mSex = "female";
        }

        String result = null;
        RequestBody formbody = new FormBody.Builder()
                .add("name", userName)
                .add("password", password)
                .add("age", age)
                .add("sex", mSex)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint)+"/login/signup")
                .post(formbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplication(), BaseActivity.class);
                    startActivity(intent);
//                    finish();
                    showToast("SignUp 成功");
                } else {
                    showToast("SignUp 失敗");
                }
                response.close();
            }
        });
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                          }
                      }
        );
    }
}
