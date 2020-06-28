package com.david0926.drop.Interface;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface DROPRetrofitInterface {

    @Multipart
    @POST("v1/user")
    Call<ResponseBody> CreateUser(
//            @Body RegisterModel rm
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part photo
    );

    @POST("v1/auth")
    Call<ResponseBody> Login(
            @Body LoginModel lm
    );

    @POST("v1/post")
    Call<ResponseBody> CreateGroup(
            @Header("x-access-token") String token,
            @Body String group
    );

    @POST("v1/group/join")
    Call<ResponseBody> JoinGroup(
            @Header("x-access-token") String token,
            @Body String group
    );

    @GET("v1/group/user")
    Call<ResponseBody> MyGroups(
            @Header("x-access-token") String token
    );

    @POST("v1/auth")
    Call<ResponseBody> getToken(
            @Body LoginModel lm
    );

}
