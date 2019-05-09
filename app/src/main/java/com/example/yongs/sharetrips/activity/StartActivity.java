package com.example.yongs.sharetrips.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yongs.sharetrips.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Handler handler = new Handler() {
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                //startActivity(new Intent(StartActivity.this,LoginActivity.class));
                startActivity(new Intent(StartActivity.this,MainActivity.class));
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,2000);
    }
}
