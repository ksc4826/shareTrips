package com.example.yongs.sharetrips.api.reports;

import com.example.yongs.sharetrips.model.Report;

import org.json.JSONObject;

import java.nio.Buffer;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportApiService {

    @GET("/reports/{username}")
    Call<List<Report>> getReport(@Path("username") String username);

    @GET("/reports/all")
    Call<List<Report>> getReports();

    @GET("/reports/images/{username}/{reportID}")
    Call<ResponseBody> getImage(@Path("username") String username, @Path("reportID") int reportID);

    @GET("/reports/images/{reportID}")
    Call<ResponseBody> getImage(@Path("reportID") int reportID);


    @Multipart
    @POST("/reports")
    Call<ResponseBody> postReport(
            @PartMap() Map<String,Object> report,
            @Part MultipartBody.Part file
    );

    @GET("/reports/search")
    Call<List<Report>> getSearch(@Query("keyword") String keyword);

    @GET("/reports/recommend")
    Call<JSONObject> getRecommend(@Query("username") String username);
}
