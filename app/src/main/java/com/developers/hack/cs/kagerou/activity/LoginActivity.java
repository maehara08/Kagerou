package com.developers.hack.cs.kagerou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * siginin
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button login_button;
    private Button signupButton;
    private EditText nameEditText;
    private EditText passwordEditText;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

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
        signupButton = (Button) findViewById(R.id.signUp_button);
        nameEditText = (EditText) findViewById(R.id.user_name_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        pref = getSharedPreferences("kagerou", Context.MODE_PRIVATE);
        editor = pref.edit();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "login_button_onClick");
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected String doInBackground(Void... params) {
                        postLogin();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Log.d(TAG, "onPostExecute");
                    }
                }.execute();

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "signUp_button_onClick");
                Intent intent = new Intent(getApplication(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void postLogin() {
        OkHttpClient client = new OkHttpClient();
        final String userName = nameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        if (userName == null || password == null) {
            return;
        }

        String result = null;
        RequestBody formBody = new FormBody.Builder()
                .add("name", userName)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint)+"/login/signin")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG,"Login 成功");
                    editor.putInt(getString(R.string.login_judge),1);
                    editor.putString(getString(R.string.login_user_name),userName);
                    editor.putString(getString(R.string.login_user_pass),password);
                    editor.commit();
                    Intent intent = new Intent(getApplication(), BaseActivity.class);
                    startActivity(intent);
                    finish();
                    showToast("Login 成功");
                } else {
                    Log.d(TAG,"Login 失敗");
                    showToast("Login 失敗");
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
        });
    }
}
