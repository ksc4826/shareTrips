package com.example.yongs.sharetrips.api.reports;

import android.content.Context;
import android.net.Uri;

import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.common.Common;
import com.example.yongs.sharetrips.model.Report;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitReports {

    private ReportApiService mReportApiService;
    public static String mBaseUrl = Common.BaseURL;
    private static Context mContext;
    private static Retrofit mRetrofit;

    private RetrofitReports(Context context) {
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl)
                .build();
    }

    public static RetrofitReports getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitReports(mContext);
    }

    public RetrofitReports createBaseApi() {
        mReportApiService = create(ReportApiService.class);
        return this;
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null");
        }
        return mRetrofit.create(service);
    }

    public void postReport(String filePath, HashMap<String, Object> parameters, final ApiCallback callback) {
        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        mReportApiService.postReport(parameters,body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getReport(String username, final ApiCallback callback){
        mReportApiService.getReport(username).enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getReports(final ApiCallback callback){
        mReportApiService.getReports().enqueue(new Callback<List<Report>>(){

            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void getImage(String username, int reportID, final ApiCallback callback){
        mReportApiService.getImage(username,reportID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getImage(int reportID, final ApiCallback callback){
        mReportApiService.getImage(reportID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getSearch(String keyword, final ApiCallback callback){
        mReportApiService.getSearch(keyword).enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getRecommend(String username, final ApiCallback callback){
        mReportApiService.getRecommend(username).enqueue(new Callback<JSONObject>(){

            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}