package com.david0926.drop.Interface;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RetrofitRegisterInterface {

    @POST("v1/user")
    @Multipart
    Call<ResponseBody> CreateUser(
//            @Body RegisterModel rm
            @PartMap Map<String, RequestBody> params
    );

    @POST("v1/auth")
    Call<ResponseBody> Login(
            @Body LoginModel lm
    );

}
