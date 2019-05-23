package com.example.yongs.sharetrips.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ThemeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    String mId;

    RetrofitUsers mRetrofitUsers;



    ListView themeListView;
    ArrayList<String> themeList;
    ArrayAdapter<String > adapter;


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


        themeListView = findViewById(R.id.list);
        ButterKnife.bind(this);
        setToolbar();

        themeList = new ArrayList<>();
        themeList.add("음식");
        themeList.add("쇼핑");
        themeList.add("관광지");
        themeList.add("자연");
        themeList.add("랜드마크");
        themeList.add("문화/예술");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,themeList);
        themeListView.setAdapter(adapter);

        themeListView.setOnItemClickListener(listener);
    }



    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(ThemeActivity.this, themeList.get(position), Toast.LENGTH_SHORT).show();

            mId = getIntent().getStringExtra("id");



            mRetrofitUsers.PostTheme(mId,themeList.get(position), new ApiCallback() {
                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onSuccess(int code, Object receiveData) {

                    finish();
                }

                @Override
                public void onFailure(int code) {

                }
            });
/**/
            //finish();

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}


