package com.developers.hack.cs.kagerou.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.developers.hack.cs.kagerou.R;
import com.developers.hack.cs.kagerou.model.KagerouCircle;
import com.developers.hack.cs.kagerou.util.MySQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by riku_maehara on 16/08/05.
 */
public class MyCircleFragment extends Fragment {
    private static final String TAG = MyCircleFragment.class.getSimpleName();

    private ListView mListView;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    SQLiteDatabase mKagerouDB;
    SharedPreferences preferences;
    private final String DB_NAME = "kagerou.db";
    ArrayList<KagerouCircle> kagerouList = new ArrayList<KagerouCircle>();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(), DB_NAME, null, 1);
        mKagerouDB = mySQLiteOpenHelper.getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_circle, null);
        mListView = (ListView) view.findViewById(R.id.listViewMyCircle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        mListView.setAdapter(adapter);
        preferences = getActivity().getSharedPreferences("kagerou", Context.MODE_PRIVATE);
        ArrayList<KagerouCircle> arrayList = mySQLiteOpenHelper.loadCircleDB(mKagerouDB);

        for (KagerouCircle circle : arrayList) {
            Log.d(TAG, "onCreateView: " + circle.getTitle());
            String s = circle.getName();
            String s1 = preferences.getString(getResources().getString(R.string.login_user_name), "nono");
            Log.d(TAG, "onCreateView: " + s1 + "111111111111" + s);
            if (s1.equals(s)) {
                adapter.add(circle.getTitle());
                kagerouList.add(circle);
            }
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KagerouCircle circle=kagerouList.get(position);

                postDeleteCircle(circle.getName(), String.valueOf(circle.getCircle_id()));
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(MyCircleFragment.this).commit();
//                Bundle bundle = new Bundle();
//                bundle.putString(getString(R.string.post_title), circle.getTitle());
//                bundle.putString(getString(R.string.post_date), circle.getCreated_at());
//                bundle.putString(getString(R.string.post_name), circle.getName());
//                bundle.putString(getString(R.string.post_content), circle.getContent());
//                bundle.putString(getString(R.string.post_circle_id), String.valueOf(circle.getCircle_id()));
//                DetailFragment detailFragment = new DetailFragment();
//                detailFragment.setArguments(bundle);
//                transaction
//                        .add(R.id.listview_container, detailFragment)
//                        .addToBackStack(null)
//                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void postDeleteCircle(String name ,String circle_id) {
        OkHttpClient client = new OkHttpClient();
        if (circle_id == null) {
            Log.d(TAG, "circle_id null");
            return;
        }

        String result = null;

        RequestBody formBody = new FormBody.Builder()
                .add("circle_id", circle_id)
                .add("name", name)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint) + "/maps/my/circles/delete")
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
}
