package com.developers.hack.cs.kagerou.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.developers.hack.cs.kagerou.R;
import com.developers.hack.cs.kagerou.activity.MesuredListView;
import com.developers.hack.cs.kagerou.util.MySQLiteOpenHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailFragment extends Fragment {
    private static final String TAG = DetailFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String DB_NAME = "kagerou.db";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String title;
    private String date;
    private String name;
    private String content;
    private String circle_id;

    TextView titleTextView;
    TextView dateTextView;
    TextView nameTextView;
    TextView contentTextView;

    Button helpButton;

    MySQLiteOpenHelper mySQLiteOpenHelper;
    SQLiteDatabase mKagerouDB;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(), DB_NAME, null, 1);
            mKagerouDB = mySQLiteOpenHelper.getWritableDatabase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        titleTextView = (TextView) view.findViewById(R.id.title);
        dateTextView = (TextView) view.findViewById(R.id.date);
        nameTextView = (TextView) view.findViewById(R.id.username);
        helpButton = (Button) view.findViewById(R.id.help_button);
        contentTextView = (TextView) view.findViewById(R.id.main_text);

            title = getArguments().getString(getString(R.string.post_title));
            date = getArguments().getString(getString(R.string.post_date));
            name = getArguments().getString(getString(R.string.post_name));
            content = getArguments().getString(getString(R.string.post_content));
            circle_id = getArguments().getString(getString(R.string.post_circle_id));
        titleTextView.setText(title);
        dateTextView.setText(date);
        nameTextView.setText(name);
        contentTextView.setText(content);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: START");
                Log.d(TAG, "onClick circle_id: " + circle_id);
                mySQLiteOpenHelper.helpButtonPush(mKagerouDB, circle_id);
                Log.d(TAG, "onClick circle_id: " + circle_id);
                postHelpPush();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        Log.d(TAG, "postHelp");
                        postHelpPush();
                        return null;
                    }
                };
            }
        });

        MesuredListView mesuredListView = (MesuredListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        mesuredListView.setAdapter(adapter);
        for (int i = 0; i < 100; i++) {
            adapter.add("item = " + String.valueOf(i + 1));
            Log.d(TAG, "TEST:make list");
        }
        return view;
    }

    public void postHelpPush() {
        OkHttpClient client = new OkHttpClient();
        final String circle_id = getArguments().getString(getString(R.string.post_circle_id));
        if (circle_id == null) {
            Log.d(TAG, "circle_id null");
            return;
        }

        String result = null;

        RequestBody formBody = new FormBody.Builder()
                .add("circle_id", circle_id)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint) + "/maps/circle/help")
                .post(formBody)
                .build();
        Log.d(TAG, getString(R.string.endpoint));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "postHelpPush 成功");
                }
                response.close();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
