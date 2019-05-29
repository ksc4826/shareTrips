package com.example.yongs.sharetrips.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.reports.RetrofitReports;
import com.example.yongs.sharetrips.api.users.RetrofitUsers;
import com.example.yongs.sharetrips.fragment.ProfileFragment;
import com.example.yongs.sharetrips.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ThemeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    String mId;

    RetrofitUsers mRetrofitUsers;

    ListView mThemeListView;
    ArrayList<String> mThemeList;
    ArrayAdapter<String > mThemeAdapter;
    AdapterView.OnItemClickListener mListener;

    private static final String TAG = ThemeActivity.class.getSimpleName();

    private static final String[] THEME = new String[]{
            "음식", "쇼핑", "관광지", "자연", "랜드마크", "문화/예술"
    };

    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("관심테마");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        ButterKnife.bind(this);

        mRetrofitUsers = RetrofitUsers.getInstance(this).createBaseApi();

        mThemeListView = findViewById(R.id.list);

        setToolbar();

        setClickListener();

        setThemeList();

    }

    private void setThemeList(){
        mThemeList = new ArrayList<String>();
        for(int i=0;i<THEME.length;i++){
            mThemeList.add(THEME[i]);
        }
        mThemeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mThemeList);
        mThemeListView.setAdapter(mThemeAdapter);

        mThemeListView.setOnItemClickListener(mListener);
    }

    private void setClickListener(){
        mListener= new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                mId = getIntent().getStringExtra("id");

                User user = new User();
                user.setTheme(mThemeList.get(position));
                mRetrofitUsers.patchTheme(mId,user, new ApiCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG,t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receiveData) {
                        Log.i(TAG,String.valueOf(code));
                        finish();
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.i(TAG,String.valueOf(code));
                    }
                });
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}


