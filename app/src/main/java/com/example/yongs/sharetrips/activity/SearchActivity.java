package com.example.yongs.sharetrips.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.reports.RetrofitReports;
import com.example.yongs.sharetrips.model.Report;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.keyword)
    EditText keyword;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.close_btn)
    ImageButton close_btn;
    @BindView(R.id.search_btn)
    Button search;

    String mKeyword;
    String mLocation;
    RetrofitReports mRetrofitReports;
    List<Report> mReportList;

    private static final String TAG = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setEdit();
    }

    @OnClick(R.id.close_btn)
    void close(){
        finish();
    }

    @OnClick(R.id.search_btn)
    void search(){
        getValue();

        mRetrofitReports.getSearch(mKeyword, mLocation, new ApiCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receiveData) {
                Log.i(TAG, String.valueOf(code));

                mReportList = (List<Report>) receiveData;

                /*Intent intent = new Intent(this, searchResult.class);
                intent.putExtra("reportList",mReportList);
                startActivity(intent);*/

            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

    private void setEdit(){
        keyword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    private void getValue(){
        if(keyword.getText().toString().length() != 0){
            mKeyword = keyword.getText().toString();
        }
        if(location.getText().toString().length() != 0){
            mLocation = location.getText().toString();
        }
    }
}
