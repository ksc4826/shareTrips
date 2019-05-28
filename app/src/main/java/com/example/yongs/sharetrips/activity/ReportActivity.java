package com.example.yongs.sharetrips.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.reports.RetrofitReports;
import com.example.yongs.sharetrips.model.Report;
import com.example.yongs.sharetrips.utils.ImageAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.title_edit)
    EditText title;
    @BindView(R.id.image_button)
    Button button;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.location_edit)
    EditText location;
    @BindView(R.id.content_edit)
    EditText content;

    private static final String TAG = ReportActivity.class.getSimpleName();
    private static final int SELECT_PICTURE = 1;

    RetrofitReports mRetrofitReports;

    public static ImageAnalysisThread mThread;

    String mTitle;
    public static String mLocation;
    String mContent;

    Uri mUri;
    String mImagePath;

    Report mReport = new Report();
    Context mContext;
    private ImageAnalysis mImageAnalysis = new ImageAnalysis();

    private Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 0){
                String name = mLocation.split(",")[0];
                double d1 = Double.parseDouble(mLocation.split(",")[1]);
                double d2 = Double.parseDouble(mLocation.split(",")[2]);
                Geocoder geocoder = new Geocoder(mContext);
                List<Address> locationPath = null;
                try {
                    locationPath = geocoder.getFromLocation(d1,d2,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(locationPath!=null){
                    Log.d(TAG,locationPath.get(0).getAddressLine(0));
                    location.setSingleLine(false);
                    location.setText(name+" - "+locationPath.get(0).getAddressLine(0));
                }else{
                    location.setText(name);
                }
            }
        }
    };

    public class  ImageAnalysisThread extends HandlerThread {
        private static final int IMAGE_ANALYSIS = 0;
        public  Handler mHandler;

        public ImageAnalysisThread() {
            super("ImageAnalysisThread");
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper()){
                @Override
                public void handleMessage(Message msg){
                    switch(msg.what){
                        case IMAGE_ANALYSIS:
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(0,mLocation));
                    }
                }
            };
        }

        public void imageAnalysis(String filePath){
            File file = new File(filePath);
            try {
                mImageAnalysis.callCloudVision(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file)),getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        mRetrofitReports = RetrofitReports.getInstance(this).createBaseApi();

        mContext = getApplicationContext();;

        mThread = new ImageAnalysisThread();
        mThread.start();

        setToolbar();

        setEdit();

        setButton();
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("여행 기록하기");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setEdit()
    {
        title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        location.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        content.setSingleLine(false);
        content.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
    }

    private void setButton(){
        button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                getValue();

                if(!mReport.checkInput()) {
                    if (mTitle == null) {
                        Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (mImagePath == null) {
                        Toast.makeText(this, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (mLocation == null) {
                        Toast.makeText(this, "위치를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    HashMap<String,Object> parameters = new HashMap<>();

                    parameters.put("title",mTitle);
                    parameters.put("location",mLocation);
                    parameters.put("content",mContent);
                    parameters.put("writer",getIntent().getStringExtra("username"));

                    mRetrofitReports.postReport(mImagePath, parameters, new ApiCallback() {
                        @Override
                        public void onError(Throwable t) {
                            Log.e(TAG,t.toString());
                        }

                        @Override
                        public void onSuccess(int code, Object receiveData) {
                            Log.i(TAG,String.valueOf(code));
                            Log.i(TAG,String.valueOf(receiveData));
                            finish();
                        }

                        @Override
                        public void onFailure(int code) {
                            Log.i(TAG,String.valueOf(code));
                        }
                    });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri mUri = data.getData();
                Log.d(TAG,"Image Uri:"+mUri);
                if(null != mUri){
                    mImagePath = getPathFromURI(mUri);
                    Log.d(TAG,"Image Path:"+mImagePath);
                    image.setImageURI(mUri);
                    button.setVisibility(View.GONE);
                    mThread.imageAnalysis(mImagePath);
                }
            }
        }
    }

    private String getPathFromURI(Uri contentUri){
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) { return cursor.getString(columnIndex); }
        } finally {
            cursor.close();
        }

        return null;
    }

    private void getValue(){
        if(title.getText().toString().length() != 0){
            mTitle = title.getText().toString();
            mReport.setTitle(mTitle);
        }
        if(location.getText().toString().length() != 0){
            mLocation = location.getText().toString();
            mReport.setLocation(mLocation);
        }
        if(content.getText().toString().length() != 0){
            mContent = content.getText().toString();
            mReport.setContent(mContent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThread.quit();
    }
}
