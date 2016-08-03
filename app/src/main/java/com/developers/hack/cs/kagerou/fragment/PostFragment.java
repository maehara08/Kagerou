package com.developers.hack.cs.kagerou.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developers.hack.cs.kagerou.R;
import com.developers.hack.cs.kagerou.activity.MainActivity;
import com.developers.hack.cs.kagerou.activity.SignUpActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    private static final String TAG = PostFragment.class.getSimpleName();

    Button post;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private EditText titleEditText;
    private EditText contentEditText;
    Bundle bundle = getArguments();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String lng;
    private String lat;

    private OnFragmentInteractionListener mListener;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            lng = getArguments().getString(getString(R.string.flagment_key_lng));
            lat =getArguments().getString(getString(R.string.flagment_key_lat));
            Log.d(TAG,"onCreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        post = (Button) view.findViewById(R.id.post);
        titleEditText = (EditText) view.findViewById(R.id.title_editText);
        contentEditText = (EditText) view.findViewById(R.id.content_editText);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "PostButton Click!");
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        postCircle();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Log.d(TAG, "onPostExecute");
                    }
                }.execute();
            }
        });
        return view;
    }

    private void postCircle() {

        OkHttpClient client = new OkHttpClient();
        final String userName = "TODO"; // TODO
        final String title = titleEditText.getText().toString();
        final String content = contentEditText.getText().toString();
//        final double longitude = bundle.getDouble("longitude"); // 緯度 TODO
//        final double latitude = bundle.getDouble("latitude");// 経度 TODO

        if (title == null || content == null) {
            return;
        }
        String result = null;
        RequestBody formbody = new FormBody.Builder()
                .add("name", userName)
                .add("title", title)
                .add("content", content)
//                .add("longitude", String.valueOf(longitude))
                .add("longitude", lng)
//                .add("latitude", String.valueOf(latitude))
                .add("latitude", lat)
                .build();
        Request request = new Request.Builder()
                .url(getString(R.string.endpoint) + "/maps/add_circle")
                .post(formbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                showToast("投稿 失敗");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    removeThis();
                    showToast("投稿 成功");
                } else {
                    showToast("投稿 失敗");
                }
            }
        });
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast toast = Toast.makeText(getActivity().getApplication(), message, Toast.LENGTH_SHORT);
//                toast.show();
            }
        });
    }

    public void removeThis() {
        mTransaction.remove(this);
        mTransaction.commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
