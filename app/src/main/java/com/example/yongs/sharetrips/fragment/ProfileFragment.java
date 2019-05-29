package com.example.yongs.sharetrips.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.activity.CountryActivity;
import com.example.yongs.sharetrips.activity.ModifyActivity;
import com.example.yongs.sharetrips.activity.ThemeActivity;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.users.RetrofitUsers;
import com.example.yongs.sharetrips.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.email)
    TextView email;


    @BindView(R.id.location_plus)
    ImageView location_plus;
    @BindView(R.id.theme_plus)
    ImageView theme_plus;
    @BindView(R.id.location_content)
    TextView location_content;
    @BindView(R.id.theme_content)
    TextView theme_content;

    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> themeList = new ArrayList<String>();
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> themeAdapter;

    RetrofitUsers mRetrofitUsers;

    User mUser;

    private String mId;

    private static final String TAG = ProfileFragment.class.getSimpleName();


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        mRetrofitUsers = RetrofitUsers.getInstance(getActivity().getBaseContext()).createBaseApi();

        setProfile();

        setClickEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setProfile();
    }

    private void setProfile() {
        mId = getActivity().getIntent().getStringExtra("id");

        mRetrofitUsers.getUser(mId, new ApiCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
            }

            @Override
            public void onSuccess(int code, Object receiveData) {
                Log.i(TAG, String.valueOf(code));
                mUser = (User) receiveData;
                username.setText(mUser.getUsername());
                email.setText(mUser.getEmail());
                location_content.setText(mUser.getCountry());
                theme_content.setText(mUser.getTheme());
            }

            @Override
            public void onFailure(int code) {
                Log.i(TAG, String.valueOf(code));
            }
        });
    }

    private void setClickEvent() {
        username.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                intent.putExtra("content", "이름");
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                intent.putExtra("content", "이메일");
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        location_plus.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CountryActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
            }

        });

        theme_plus.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        location_content.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CountryActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        theme_content.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });
    }
}
