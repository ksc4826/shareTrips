package com.example.yongs.sharetrips.api.users;

import com.example.yongs.sharetrips.model.Report;
import com.example.yongs.sharetrips.model.User;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApiService {

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @FormUrlEncoded
    @POST("/users/join")
    Call<User> postJoin(@FieldMap HashMap<String, Object> parameters);

    @FormUrlEncoded
    @POST("/users/login")
    Call<User> postLogin(@FieldMap HashMap<String, Object> parameters);

    @PATCH("/users/{id}/username")
    Call<Void> patchUsername(@Path("id") String id, @Body User user);

    @PATCH("/users/{id}/email")
    Call<Void> patchEmail(@Path("id") String id, @Body User user);

    @DELETE("/users/{id}")
    Call<Void> deleteUser(@Path("id") String id);



    @POST("/users/{id}/theme")
    Call<Void> PostTheme(@Path("id") String id, @Body String theme);

    @POST("/users/{id}/country")
    Call<Void> PostCountry(@Path("id") String id, @Body String country);

    @DELETE("/users/{id}/theme")
    Call<Void> deleteTheme(@Path("id") String id);

    @DELETE("/users/{id}/country")
    Call<Void> deleteCountry(@Path("id") String id);

}
